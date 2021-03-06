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

val octokitV = "15.8.0"
lazy val downloadRoutesJson = settingKey[File]("Download routes.json file from the octokit/rest.js repository")

downloadRoutesJson := {
  import sys.process._
  val log = sLog.value
  val url = s"https://raw.githubusercontent.com/octokit/rest.js/v${octokitV}/lib/routes.json"
  val file = baseDirectory.value / "routes.json"
  log.info(s"Downloading routes.json v${octokitV} ...")
  val exitCode = (new java.net.URL(url) #> file).!(log)
  if (exitCode != 0) sys.error("Download failed")
  file
}

Compile/sourceGenerators += Def.task {
  import laughedelic.sbt.octokit._, Generator._
  import upickle.default._
  import java.nio.file.Files
  import scala.collection.JavaConverters._
  val log = streams.value.log

  val routesJson = downloadRoutesJson.value
  log.info(s"Parsing Github routes from  ${routesJson} ...")
  val parsed = ParseRoutesTypes(routesJson)

  val out = (Compile/sourceManaged).value / "octokit" / "rest" / "routes.scala"
  log.info(s"Writing generated routes to ${out} ...")
  IO.createDirectory(out.getParentFile)
  Files.write(
    out.toPath,
    generateRoutes(parsed).asJava
  )
  Seq(out)
}.taskValue

libraryDependencies += "com.lihaoyi" %%% "utest" % "0.6.3" % Test
testFrameworks += new TestFramework("utest.runner.Framework")

enablePlugins(ScalaJSBundlerPlugin)
npmDependencies in Test += "@octokit/rest" -> octokitV
