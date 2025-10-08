ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.17"

lazy val root = (project in file("."))
  .settings(
  )

libraryDependencies ++= Seq(
  // cats dependencies
  "org.typelevel" %% "cats-core" % "2.13.0",
  "org.typelevel" %% "cats-effect" % "3.6.3",
  // fs2 dependencies
  "co.fs2" %% "fs2-core" % "3.12.2",
  "co.fs2" %% "fs2-io" % "3.12.2",
  // http4s dependencies
  "org.http4s" %% "http4s-core" % "0.23.32",
  "org.http4s" %% "http4s-dsl" % "0.23.32",
  "org.http4s" %% "http4s-circe" % "0.23.32",
  "org.http4s" %% "http4s-blaze-server" % "0.23.17",
  // circe dependencies
  "io.circe" %% "circe-core" % "0.14.15",
  "io.circe" %% "circe-generic" % "0.14.15",
  "io.circe" %% "circe-parser" % "0.14.15",
  // munit para testing
  "org.typelevel" %% "munit-cats-effect-3" % "1.0.7" % Test,
  // kafka dependencies
  "org.apache.kafka" % "kafka-clients" % "4.1.0"
)