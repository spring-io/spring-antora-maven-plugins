package org.springframework.maven.antora;

/*
 * Copyright 2001-2005 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.apache.maven.execution.MavenSession;
import org.apache.maven.model.Plugin;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.BuildPluginManager;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.*;
import org.apache.maven.project.MavenProject;
import org.twdata.maven.mojoexecutor.MojoExecutor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.twdata.maven.mojoexecutor.MojoExecutor.*;

/**
 * Runs Antora within a Maven project.
 */
@Mojo(name = "antora", defaultPhase = LifecyclePhase.NONE)
public class AntoraMavenPlugin
		extends AbstractMojo {

	/**
	 * The project currently being build.
	 */
	@Parameter(defaultValue = "${project}", readonly = true)
	private MavenProject mavenProject;

	/**
	 * The current Maven session.
	 */
	@Parameter(defaultValue = "${session}", readonly = true)
	private MavenSession mavenSession;

	/**
	 * The Maven BuildPluginManager component.
	 */
	@Component
	private BuildPluginManager pluginManager;

	@Parameter
	private String version = "3.2.0-alpha.2";

	@Parameter
	private Map<String, String> environment = new HashMap<>();

	@Parameter
	private List<String> options = new ArrayList<>();

	@Parameter
	private String playbook = "antora-playbook.yml";

	private final String antoraExecutable = "node_modules/.bin/antora";

	@Parameter(name = "node.version")
	private String nodeVersion = "v18.12.1";

	@Parameter
	private List<String> packages = new ArrayList<>();

	public void execute()
			throws MojoExecutionException {
		executeMojo(
			frontendMavenPlugin(),
			goal("install-node-and-npm"),
			configuration(
				element(name("nodeVersion"), this.nodeVersion)
			),
			executionEnvironment()
		);
		executeMojo(
			frontendMavenPlugin(),
			goal("npm"),
			configuration(
				element(name("arguments"), installArguments())
			),
			executionEnvironment()
		);
		executeMojo(
				execAntoraPlugin(),
				goal("exec"),
				configuration(
					element(name("executable"), "node/node"),
					element("arguments", createExecAntoraArguments()),
					element("inheritIo", "true")
				),
				executionEnvironment()
		);
	}

	private Plugin frontendMavenPlugin() {
		return plugin(
				groupId("com.github.eirslett"),
				artifactId("frontend-maven-plugin"),
				version("1.12.1")
		);
	}

	private String installArguments() {
		StringBuffer installAntoraArgumentsValue = new StringBuffer();
		installAntoraArgumentsValue.append("install");
		if (this.packages.isEmpty()) {
			this.packages.add("@antora/atlas-extension@1.0.0-alpha.1");
			this.packages.add("@antora/collector-extension@1.0.0-alpha.3");
			this.packages.add("@asciidoctor/tabs@1.0.0-beta.3");
			this.packages.add("@springio/antora-extensions@1.5.0");
			this.packages.add("@springio/asciidoctor-extensions@1.0.0-alpha.9");
		}
		this.packages.add(0, "@antora/cli@" + this.version);
		this.packages.add(1, "@antora/site-generator-default@" + this.version);
		this.packages.forEach(dependency -> {
			installAntoraArgumentsValue.append(" ");
			installAntoraArgumentsValue.append(dependency);
		});
		return installAntoraArgumentsValue.toString();
	}

	private Plugin execAntoraPlugin() {
		return plugin(
			groupId("org.codehaus.mojo"),
			artifactId("exec-maven-plugin"),
			version("3.1.0")
		);
	}

	private Element[] createExecAntoraArguments() {
		if (this.options.isEmpty()) {
			this.options.add("--to-dir=target/antora/site");
			this.options.add("--stacktrace");
		}
		this.options.add(0, this.antoraExecutable);
		this.options.add(1, this.playbook);
		return this.options.stream()
				.map(arg -> MojoExecutor.element("argument", arg))
				.toArray(Element[]::new);
	}

	private ExecutionEnvironment executionEnvironment() {
		return MojoExecutor.executionEnvironment(
				mavenProject,
				mavenSession,
				pluginManager
		);
	}

}
