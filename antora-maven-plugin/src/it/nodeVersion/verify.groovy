File nodeDir = new File( basedir, "node" );
assert nodeDir.isDirectory()
File nodeModules = new File(basedir, "node_modules")
assert nodeModules.isDirectory()
File siteIndex = new File(basedir, "target/antora/site/test/1.0.0-SNAPSHOT/index.html")
assert siteIndex.isFile()
File nodeExec = new File(nodeDir, 'node')
String nodeVersion = "${nodeExec} --version".execute().text
assert nodeVersion.contains("v16.16.0")