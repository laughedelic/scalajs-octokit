name         := "scalajs-octokit"
organization := "laughedelic"
description  := "Scala.js wrapper for the octokit/rest.js library (GitHub REST API)"

homepage := Some(url(s"https://github.com/laughedelic/${name.value}"))
scmInfo in ThisBuild := Some(ScmInfo(
  homepage.value.get,
  s"scm:git:git@github.com:laughedelic/${name.value}.git"
))

licenses := Seq("MPL-2.0" -> url("https://www.mozilla.org/en-US/MPL/2.0"))
developers := List(Developer(
  "laughedelic",
  "Alexey Alekhin",
  "laughedelic@gmail.com",
  url("https://github.com/laughedelic")
))

scalaVersion := "2.12.6"
scalacOptions ++= Seq(
  "-Yrangepos",
  "-P:scalajs:sjsDefinedByDefault",
  "-language:implicitConversions",
  "-deprecation",
  "-feature",
  "-Xlint"
)

enablePlugins(ScalaJSPlugin)

releaseEarlyWith := BintrayPublisher
releaseEarlyEnableSyncToMaven := false
releaseEarlyNoGpg := true

publishMavenStyle := true
bintrayReleaseOnPublish := !isSnapshot.value
bintrayPackageLabels := Seq("scalajs", "github", "octokit", "facades")

ghreleaseAssets := Seq()

Compile/sourceGenerators += Def.task {
  import laughedelic.sbt.octokit._, Generator._
  import upickle.default._
  import java.nio.file.Files
  import scala.collection.JavaConverters._

  val out = (Compile/sourceManaged).value / "octokit" / "rest" / "routes.scala"
  val parsed = read[Routes](file("routes-for-api-docs.json"))

  IO.createDirectory(out.getParentFile)
  Files.write(
    out.toPath,
    generateRoutes(parsed).asJava
  )
  Seq(out)
}.taskValue
