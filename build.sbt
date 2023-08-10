ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.10"

lazy val root = (project in file("."))
  .settings(
    name := "Employee-Management-System",
    idePackagePrefix := Some("org.knoldus")
  )
enablePlugins(ScoverageSbtPlugin)
libraryDependencies ++= Seq(
  "com.typesafe.scala-logging" %% "scala-logging" % "3.9.5",
  "ch.qos.logback" % "logback-classic" % "1.4.6",
  "org.scalatest" %% "scalatest" % "3.2.15" % Test,
  "mysql" % "mysql-connector-java" % "8.0.32",
  "com.typesafe" % "config" % "1.4.2"
)

libraryDependencies ++= Seq(
  "org.scalafx" %% "scalafx" % "16.0.0-R23",
  "org.openjfx" % "javafx-controls" % "19.0.2.1",
  "org.openjfx" % "javafx-fxml" % "19.0.2.1",
  "org.openjfx" % "javafx-media" % "19.0.2.1"
)

