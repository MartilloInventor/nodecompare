//addSbtPlugin("com.typesafe.play" % "sbt-plugin" % "2.6.13")
addSbtPlugin("com.typesafe.play" % "sbt-plugin" % "2.8.1")
//addSbtPlugin("com.typesafe.sbt" % "sbt-native-packager" % "1.3.3")
addSbtPlugin("com.typesafe.sbt" % "sbt-native-packager" % "1.7.0")
// This is a major difference from hyper-cms build.sbt.
addSbtPlugin("com.eed3si9n" % "sbt-assembly" % "0.14.10")
// I wonder whether Anton & Kyril ever tried to build a fat jar.
// It would have made life easier for them.
// These two web pages look helpful in getting assembly to work.
// https://alvinalexander.com/scala/sbt-how-build-single-executable-jar-file-assembly
// https://github.com/sbt/sbt-assembly
