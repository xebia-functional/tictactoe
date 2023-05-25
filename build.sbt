Global / onChangedBuildSource := ReloadOnSourceChanges
ThisBuild / organization := "com.47deg"

ThisBuild / scmInfo := Some(
  ScmInfo(
    url("https://github.com/xebia-functional/tictactoe"),
    "scm:git@github.com:xebia-functional/tictactoe.git"
  )
)

ThisBuild / scalaVersion := "3.2.2"
ThisBuild / semanticdbEnabled := true
ThisBuild / scalafixDependencies += Dependencies.Compile.organizeImports

lazy val commonJVM = common.jvm
lazy val commonJS = common.js

lazy val `tictactoe` = project
  .in(file("."))
  .settings(
    name := "tictactoe",
    version := "0.1.0-SNAPSHOT"
  )
  .aggregate(server, tttClient)
  .dependsOn(server, tttClient)

lazy val commonDependencies = Def.setting(
  Seq(
    Dependencies.Test.munit           % Test,
    Dependencies.Test.munitCats3      % Test,
    Dependencies.Test.munitScalacheck % Test
  )
)

lazy val common =
  crossProject(JSPlatform, JVMPlatform)
    .crossType(CrossType.Full)
    .in(file("modules/common"))
    .settings(Dependencies.commonDepsSettings)

lazy val server =
  project
    .in(file("modules/server"))
    .settings(
      name := "tictactoe-server",
      resolvers += "confluent" at "https://packages.confluent.io/maven/",
      scalacOptions ++= Seq("-source", "future")
    )
    .settings(Dependencies.serverDepsSettings)
    .dependsOn(commonJVM)
    .enablePlugins(JavaAppPackaging)
    .enablePlugins(DockerPlugin)

lazy val tttClient =
  project
    .in(file("modules/client"))
    .enablePlugins(ScalaJSPlugin)
    .settings(
      name := "tictactoe-client",
      resolvers += "http4s" at "https://mvnrepository.com/artifact/org.http4s/http4s-dom",
      Compile / resourceDirectories += (Compile / baseDirectory).value / "src" / "main" / "assets",
      scalaJSLinkerConfig ~= { _.withModuleKind(ModuleKind.CommonJSModule) }
    )
    .settings(Dependencies.clientDepsSettings)
    .dependsOn(common.js)
    .enablePlugins(UniversalPlugin)
