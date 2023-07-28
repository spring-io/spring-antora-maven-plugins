package org.springframework.maven.antora;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AntoraYmlUtilTest {

	@Test
	void prereleaseFromVersionWhenSnapshot() {
		assertThat(AntoraYmlUtil.prereleaseFromVersion("3.0.0-SNAPSHOT")).isEqualTo("-SNAPSHOT");
	}

	@Test
	void prereleaseFromVersionWhenMilestone() {
		assertThat(AntoraYmlUtil.prereleaseFromVersion("3.0.0-M1")).isEqualTo("true");
	}

	@Test
	void prereleaseFromVersionWhenRc() {
		assertThat(AntoraYmlUtil.prereleaseFromVersion("3.0.0-RC1")).isEqualTo("true");
	}

	@Test
	void prereleaseFromVersionWhenGa() {
		assertThat(AntoraYmlUtil.prereleaseFromVersion("3.0.0")).isEqualTo("false");
	}


	@Test
	void componentVersionFromVersionWhenSnapshot() {
		assertThat(AntoraYmlUtil.componentVersionFromVersion("3.0.0-SNAPSHOT")).isEqualTo("3.0.0");
	}

	@Test
	void componentVersionFromVersionWhenMilestone() {
		assertThat(AntoraYmlUtil.componentVersionFromVersion("3.0.0-M1")).isEqualTo("3.0.0-M1");
	}

	@Test
	void componentVersionFromVersionFromVersionWhenRc() {
		assertThat(AntoraYmlUtil.componentVersionFromVersion("3.0.0-RC1")).isEqualTo("3.0.0-RC1");
	}

	@Test
	void componentVersionFromVersionFromVersionWhenGa() {
		assertThat(AntoraYmlUtil.componentVersionFromVersion("3.0.0")).isEqualTo("3.0.0");
	}
}