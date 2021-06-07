import sbt.Keys.publishMavenStyle
import scala.io._

val projectVersion = Source.fromFile("project.version").mkString.trim
val scalaV = "2.13.6"
scalaVersion := "2.13.6"

lazy val describeProject = taskKey[Unit]{"Describe the goal of this project."}
describeProject := { // if I used println no method would need to be defined because println is defined in scala.io.source
  printlen ("This project tests a simple Scala project.")
  printlen ("One project is defined called mainproject. The compile task is built in the SBT. Native packaging task")
  printlen ("comes from the native packager plugin while assembly = building a fat jar comes from the assembly plugin.")
  printlen ("This message is output via the describeProject (the command name) task, which is simple and defined right in build.sbt.")
}
def printlen (s: String): Unit = {
  println(s)
}

// This webpage confirms the procedure used below.
// https://stackoverflow.com/questions/41834454/sbt-e-error-deduplicate?rq=1
// Here is the less than helpful official documentation.
// https://www.scala-sbt.org/1.x/docs/Howto-Package.html

assemblyMergeStrategy in assembly := {
  case PathList("META-INF", xs @ _*) => MergeStrategy.discard
  case x => MergeStrategy.first
}

lazy val mainProject = Project(id = "nodecompare", base = file(".")).
  settings(
    mainClass in assembly := Some("org.webdev.NodeCompare"),
    sharedSettings ++ Seq(
      name := "nodecompare",
      version := projectVersion,
      test in assembly := {},
      maintainer in Linux := "joachim.cs.martillo@gmail.com <joachim.cs.martillo@gmail.com>",
      packageSummary in Linux := "nodecompare",
      packageDescription := "node compre test",
      daemonUser in Linux := "algotrader", // user which will execute the application
      daemonGroup in Linux := (daemonUser in Linux).value, // group which will execute the application
      //disable doc generation
      publishArtifact in (Compile, packageDoc) := false,
      publishArtifact in packageDoc := false,
      sources in (Compile, doc) := Seq.empty,
      // set the main class for packaging the main jar
      // see https://alvinalexander.com/scala/sbt-how-specify-main-method-class-to-run-in-project
      mainClass in (Compile, packageBin) := Some("org.webdev.NodeCompare"),
      // Enables publishing to maven repo
      publishMavenStyle := true,
      // Do not append Scala versions to the generated artifacts
      crossPaths := false,
      // This forbids including Scala related libraries into the dependency
      autoScalaLibrary := false
    )
  ).enablePlugins(DebianPlugin)

val sharedSettings = Defaults.coreDefaultSettings ++ Seq(
  scalaVersion := scalaV,
  // force sbt to use the local maven repository. setting up the (sbt.)boot.properties file does not seem to work
  // see https://www.scala-sbt.org/1.x/docs/Launcher-Configuration.html
  // see https://www.scala-sbt.org/1.x/docs/Resolvers.html
  // see https://www.scala-sbt.org/1.x/docs/Library-Dependencies.html
  // see https://alvinalexander.com/scala/how-configure-sbt-custom-repositories-resolvers
  // see https://stackoverflow.com/questions/21566717/sbt-local-maven-repository-dependency
  // sometimes it might be necessary to use externalResolvers
  // https://stackoverflow.com/questions/7901947/sbt-access-to-local-maven-repositories

  // I am treating the local maven repository like a custom repository. It might not be necessary, but we really don't
  // want to use a maven local repository.
  // According to stack overflow maven-local is disabled.
//  resolvers ++= Seq(
//  "Local Maven Repository" at "file://"+Path.userHome.absolutePath+"/.m2/repository"
//  "Local Maven Repository" at "file://"+Path.userHome.absolutePath+"/HyperactiveProjects/HyperactiveRepository/repository"
//    "Remote Custom Repository" at "https://raw.github.com/HyperactiveInc/HyperactiveRepository/master/repository"
//  ),
  // library dependencies. (organization name) % (project name) % (version)
  // Here is the logic of converting from maven dependencies to sbt dependencies.
  // http://xerial.org/blog/2014/03/24/sbt/
  libraryDependencies ++= Seq(
//    "com.google.code.gson"%"gson"%"2.8.6",
    "org.scala-lang"%"scala-library"%"2.13.6",
    "org.scala-lang"%"scala-reflect"%"2.13.6",
    "org.yaml"%"snakeyaml"%"1.28"
//    "com.microsoft.graph"%"microsoft-graph-auth"%"0.1.0",
//    "com.microsoft.graph"%"microsoft-graph-core"%"1.0.0",
//    "com.microsoft.graph"%"microsoft-graph"%"1.7.1",
//    "mysql" % "mysql-connector-java" % "8.0.18",
//    "joda-time" % "joda-time" % "2.9.9",
//    "org.javatuples"%"javatuples"%"1.2",
//    "commons-io" % "commons-io" % "2.6"
  ),
  parallelExecution in Test := false
)



