File node = new File( basedir, "node" );
assert node.isDirectory()
File nodeModules = new File(basedir, "node_modules")
assert nodeModules.isDirectory()
File siteIndex = new File(basedir, "target/antora/site/test/1.0.0-SNAPSHOT/index.html")
assert siteIndex.isFile()

// Update the default documented value if this changes
File nodeExec = new File(node, 'node')
String nodeVersion = "${nodeExec} --version".execute().text
assert nodeVersion.contains("v18.17.1")
