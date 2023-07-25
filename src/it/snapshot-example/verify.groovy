File touchFile = new File( basedir, "target/classes/antora.yml" );

assert touchFile.isFile()
assert touchFile.text == """version: 1.0.0
prerelease: -SNAPSHOT"""
