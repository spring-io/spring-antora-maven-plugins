package org.springframework.maven.antora;

final class AntoraYmlUtil {

	private static final String DASH_SNAPSHOT = "-SNAPSHOT";

	static String componentVersionFromVersion(String version) {
		return version.replace(DASH_SNAPSHOT, "");
	}

	static String prereleaseFromVersion(String version) {
		if (version.endsWith(DASH_SNAPSHOT)) {
			return DASH_SNAPSHOT;
		}
		if (version.matches("^.*?-(M|RC)\\d+$")) {
			return Boolean.TRUE.toString();
		}
		return Boolean.FALSE.toString();
	}

	private AntoraYmlUtil() {}
}
