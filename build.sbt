
name := "openNlp"
organization := "edu.uic.cloud"
version := "hmr-gmendo8"
crossPaths := false
autoScalaLibrary := false
publishMavenStyle := false // Enables publishing to maven repo

scalacOptions += "-target:jvm-1.7"
javacOptions ++= Seq("-source", "1.7", "-target", "1.7")

mainClass in (Compile, run) := Some("edu.uic.cs441.gabe.Hadoop.NamedEntitiesMapReduce")
mainClass in assembly := Some("edu.uic.cs441.gabe.Hadoop.NamedEntitiesMapReduce")

assemblyOption in assembly := (assemblyOption in assembly).value.copy(includeScala = false)

assemblyOutputPath in assembly := file(".")
assemblyJarName in assembly := "mapreduce.jar"

resolvers += "Hortonworks Releases" at "[http://repo.hortonworks.com/content/repositories/releases/](http://repo.hortonworks.com/content/repositories/releases/)"

libraryDependencies += "org.apache.tika" % "tika-core" % "1.13"
libraryDependencies += "org.apache.tika" % "tika-parsers" % "1.13"
libraryDependencies += "org.apache.opennlp" % "opennlp-maxent" % "3.0.3"
libraryDependencies += "org.apache.opennlp" % "opennlp-tools" % "1.6.0"

libraryDependencies += "org.apache.hadoop" % "hadoop-common" % "2.7.3"
libraryDependencies += "org.apache.hadoop" % "hadoop-client" % "2.7.3"
