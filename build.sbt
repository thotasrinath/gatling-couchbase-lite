import Dependencies._

ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.10"

lazy val root = (project in file("."))
  .enablePlugins(GitVersioning, GatlingPlugin)
  .settings(
    name := "gatling-couchbase-lite",
    idePackagePrefix := Some("io.github.gatling.couchbase"),
    libraryDependencies ++= gatling ++ gatlingCore,
    libraryDependencies ++= coucbaseLite,
    libraryDependencies += "com.fasterxml.jackson.core" % "jackson-databind" % "2.15.0",
    scalacOptions ++= Seq(
      "-encoding",
      "UTF-8", // Option and arguments on same line
      "-Xfatal-warnings", // New lines for each options
      "-deprecation",
      "-feature",
      "-unchecked",
      "-language:implicitConversions",
      "-language:higherKinds",
      "-language:existentials",
      "-language:postfixOps",
    ),
  )

