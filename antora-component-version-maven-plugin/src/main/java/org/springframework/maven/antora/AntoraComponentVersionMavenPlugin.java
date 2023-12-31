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

import org.apache.maven.model.Resource;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;

import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import java.io.File;

/**
 * Sets the properties {@code antora-component.version} and {@code antora-component.prerelease} to be used for
 * generating an {@code antora.yml} file.
 */
@Mojo(name = "antora-component-version", defaultPhase = LifecyclePhase.VALIDATE)
public class AntoraComponentVersionMavenPlugin
		extends AbstractMojo {
    @Parameter(defaultValue = "${project}", required = true)
	MavenProject project;

	public void execute()
			throws MojoExecutionException {
		String version = project.getVersion();
		project.getProperties().setProperty("antora-component.version", AntoraYmlUtil.componentVersionFromVersion(version));
		project.getProperties().setProperty("antora-component.prerelease", AntoraYmlUtil.prereleaseFromVersion(version));

		Resource antoraResource = new Resource();
		antoraResource.setFiltering(true);
		String antoraResources = new File(project.getBasedir(), "src/main/antora/resources").getAbsolutePath();
		antoraResource.setDirectory(antoraResources);
		project.getResources().add(antoraResource);
	}
}
