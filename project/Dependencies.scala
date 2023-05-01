import sbt._

object Dependencies {
  val gatlingVersion = "3.9.3"
  val couchbaseLiteVersion = "3.1.0"

  lazy val gatlingCore: Seq[ModuleID] = Seq(
    "io.gatling" % "gatling-core",
    "io.gatling" % "gatling-core-java",
  ).map(_ % gatlingVersion % "provided")

  lazy val gatling: Seq[ModuleID] = Seq(
    "io.gatling" % "gatling-test-framework",
    "io.gatling.highcharts" % "gatling-charts-highcharts",
  ).map(_ % gatlingVersion % "it,test")

  lazy val coucbaseLite: Seq[ModuleID] = Seq(
    "com.couchbase.lite" % "couchbase-lite-java",
  ).map(_ % couchbaseLiteVersion)

}
