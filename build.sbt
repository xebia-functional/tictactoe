import scala.util.Properties
import sbt.internal.util.complete.DefaultParsers
import com.typesafe.sbt.packager.docker.*
import sbt.util.CacheImplicits.*
import sbtExtensions.*
import Dependencies.Compile.*
import Dependencies.{SbtPlugins, Versions}
import java.nio.file.Paths

import scala.util.Try
import scala.sys.process.*

Global / onChangedBuildSource := ReloadOnSourceChanges
ThisBuild / organization := "com.xebia-functional"

ThisBuild / scmInfo := Some(
  ScmInfo(
    url("https://github.com/xebia-functional/tictactoe"),
    "scm:git@github.com:xebia-functional/tictactoe.git"
  )
)

ThisBuild / scalaVersion := "3.2.2"
ThisBuild / semanticdbEnabled := true
ThisBuild / scalafixDependencies += SbtPlugins.organizeImports

lazy val commonJVM = common.jvm
lazy val commonJS = common.js

lazy val `scaladays-workshop-2023` = project
  .in(file("."))
  .settings(
    name := "scaladays-workshop-2023",
    version := "0.1.0-SNAPSHOT",
    dockerComposeCommand := {
      Paths
        .get(Try("which docker-compose".!!).recoverWith { case _ =>
          Try("where docker-compose".!!)
        }.get)
        .toFile
    },
    dockerRegistryHostPort := {
      Properties
        .envOrElse("SCALADAYS_WORKSHOP_DOCKER_REGISTRY_HOST_PORT", "5000")
        .toInt
    },
    dockerComposeFile := (Compile / baseDirectory).value / "src" / "docker-compose.yml",
    dockerComposeUp := Def
      .sequential(
        server / Docker / setupBuildX,
        server / Docker / publishLocal,
        tttClient / Universal / stage,
        Def.task(
          streams.value.log.info(
            "Docker compose file after environment variable interpolation:"
          )
        ),
        Def.task(
          streams.value.log.info(
            Process(
              Seq(
                "docker-compose",
                "-f",
                s"${dockerComposeFile.value.getAbsolutePath()}",
                "config"
              ),
              None,
              "SCALADAYS_WORKSHOP_DOCKER_REGISTRY_HOST_PORT" -> s"${dockerRegistryHostPort.value}",
              "SCALADAYS_CLIENT_DIST" -> s"${(tttClient / Universal / stagingDirectory).value / "dist"}"
            ).!!
          )
        ),
        Def.task(
          streams.value.log.info(
            Process(
              Seq(
                "docker-compose",
                "-f",
                s"${dockerComposeFile.value.getAbsolutePath()}",
                "up",
                "-d"
              ),
              None,
              "SCALADAYS_WORKSHOP_DOCKER_REGISTRY_HOST_PORT" -> s"${dockerRegistryHostPort.value}",
              "SCALADAYS_CLIENT_DIST" -> s"${(tttClient / Universal / stagingDirectory).value / "dist"}"
            ).!!
          )
        )
      )
      .value,
    dockerComposeDown := {
      val log = streams.value.log
      log.info(
        Process(
          Seq(
            "docker-compose",
            "-f",
            s"${dockerComposeFile.value.getAbsolutePath()}",
            "down",
            "--rmi",
            "all"
          ),
          None,
          "SCALADAYS_WORKSHOP_DOCKER_REGISTRY_HOST_PORT" -> s"${dockerRegistryHostPort.value}",
          "SCALADAYS_CLIENT_DIST" -> s"${(tttClient / Universal / stagingDirectory).value / "dist"}"
        ).!!
      )
    }
  )
  .aggregate(server, tttClient)
  .dependsOn(server, tttClient)

lazy val commonDependencies = Def.setting(
  Seq(
    Dependencies.Test.munit % Test,
    Dependencies.Test.munitCats3 % Test,
    Dependencies.Test.munitScalacheck % Test,
    pureConfig,
    "io.chrisdavenport" %%% "fuuid" % Versions.fuuid,
    "io.circe" %%% "circe-core" % Versions.circe,
    "io.circe" %%% "circe-generic" % Versions.circe,
    "io.circe" %%% "circe-parser" % Versions.circe,
    "org.http4s" %%% "http4s-circe" % Versions.http4s
  )
)

lazy val common =
  crossProject(JSPlatform, JVMPlatform)
    .crossType(CrossType.Full)
    .in(file("modules/common"))
    .settings(
      libraryDependencies ++= commonDependencies.value
    )

lazy val server =
  project
    .in(file("modules/server"))
    .settings(
      name := "scaladays-workshop-2023-server",
      resolvers += "confluent" at "https://packages.confluent.io/maven/",
      dockerRegistryHostPort := {
        Properties
          .envOrElse("SCALADAYS_WORKSHOP_DOCKER_REGISTRY_HOST_PORT", "5000")
          .toInt
      },
      generateConfigToml := {
        generateConfigTomlInManagedResourcesFrom(
          (
            (Docker / dockerRegistryHostPort).value,
            (Compile / resourceManaged).value
          )
        )
      },
      (Compile / resourceGenerators) += generateConfigToml,
      Docker / setupBuildX := {
        (Compile / resourceGenerators).value
        (Docker / setupBuildX).previous.filter(_ == "Success").getOrElse {
          val log = streams.value.log
          val dockerCommand =
            s"${(Docker / dockerExecCommand).value.mkString("")}"
          Try {
            val binFmtInstall =
              s"$dockerCommand run --privileged --rm tonistiigi/binfmt --install all"
            log.info(
              s"Setting up docker buildx appropriately: ${binFmtInstall.!!}"
            )
          }.flatMap { _ =>
            val stopRegistry = s"$dockerCommand container stop registry"
            Try(log.info(s"Stopping docker registry: ${stopRegistry.!!}"))
              .recoverWith { case _ => Try("Exception") }
          }.flatMap { _ =>
            val removeRegistry = s"$dockerCommand container rm registry"
            Try(log.info(s"removing docker registry: ${removeRegistry.!!}"))
              .recoverWith { case _ =>
                Try("Exception")
              }
          }.flatMap { _ =>
            val buildxSetup =
              s"$dockerCommand buildx create --config ${(Compile / resourceManaged).value / "docker" / "registry" / "config.toml"} --driver-opt network=host --use"
            Try(
              log.info(
                s"Setting up docker buildx appropriately: ${buildxSetup.!!}"
              )
            )
          }.recover { case e: Exception =>
            log.error(s"${e.getMessage}")
            throw e
          }.map(_ => "Success")
            .toOption
            .getOrElse("Failure")
        }
      },
      libraryDependencies ++= Dependencies.Compile.serverDeps ++ commonDependencies.value,
      dockerCommands := dockerCommands.value
        .updatedBy(
          baseImageCommand((Docker / dockerBaseImage).value).forStage("stage0"),
          c =>
            c match {

              case Cmd("FROM", args @ _*) =>
                args.contains("openjdk:8") && args.contains("stage0")
              case _ => false
            }
        )
        .updatedBy(
          baseImageCommand((Docker / dockerBaseImage).value).forStage(
            "mainstage"
          ),
          c =>
            c match {

              case Cmd("FROM", args @ _*) =>
                args.contains("openjdk:8") && args.contains("mainstage")
              case _ => false
            }
        )
        .insertAt(
          6,
          Cmd(
            "ADD",
            "--chmod=u=rX,g=rX",
            "https://raw.githubusercontent.com/vishnubob/wait-for-it/master/wait-for-it.sh",
            "/4/opt/docker/bin/wait-for-it.sh"
          )
        )
        .insertAt(10, Cmd("RUN", "stat", "/4/opt/docker"))
        .insertAt(20, ExecCmd("RUN", "chmod", "+x", "/opt/docker/bin/wait-for-it.sh"))
        .updatedBy(
          ExecCmd(
            "ENTRYPOINT",
            "/opt/docker/bin/wait-for-it.sh",
            "schema-registry:8081",
            "--timeout=30",
            "--strict",
            "--",
            "/opt/docker/bin/scaladays-workshop-2023-server"
          ),
          c =>
            c match {

              case ExecCmd("ENTRYPOINT", _) => true
              case _                        => false
            }
        ),
      scalacOptions ++= Seq("-source", "future"),
      Docker / dockerBaseImage := "eclipse-temurin:17.0.7_7-jre-jammy",
      Docker / dockerBuildOptions ++= Seq("--platform=linux/amd64"),
      Docker / dockerBuildCommand := (Docker / dockerExecCommand).value ++ Seq(
        "buildx",
        "build"
      ) ++ (Docker / dockerBuildOptions).value.filterNot(
        _ == "--force-rm"
      ) ++ Seq(".", "--load"),
      Docker / dockerBuildInit := true,
      Docker / dockerExposedPorts := Seq(8080, 8081),
      Docker / packageName := s"127.0.0.1:${(Docker / dockerRegistryHostPort).value}/${packageName.value}",
      Docker / dockerUpdateLatest := true,
      Docker / version := "latest"
    )
    .dependsOn(commonJVM)
    .enablePlugins(JavaAppPackaging)
    .enablePlugins(DockerPlugin)

lazy val tttClient =
  project
    .in(file("modules/client"))
    .enablePlugins(ScalaJSPlugin)
    .settings(scalaJSUseMainModuleInitializer := true)
    .settings(
      name := "scaladays-workshop-2023-client",
      resolvers += "http4s" at "https://mvnrepository.com/artifact/org.http4s/http4s-dom",
      Compile / resourceDirectories += (Compile / baseDirectory).value / "src" / "main" / "assets",
      libraryDependencies ++=
        commonDependencies.value ++
          Seq(
            "io.indigoengine" %%% "tyrian-io" % Versions.tyrian,
            "org.scala-js" %%% "scalajs-dom" % Versions.scalajsDom,
            "org.http4s" %%% "http4s-dom" % Versions.http4s,
            "com.github.japgolly.scalacss" %%% "core" % Versions.scalacssCore
          ),
      maintainer := "jack.viers@47Deg.com",
      Universal / mappings ++= {
        val log = streams.value.log
        val report = (Compile / fullLinkJS).value
        val outputDirectory =
          (Compile / fullLinkJS / scalaJSLinkerOutputDirectory).value
        report.data.publicModules.map { m =>
          log.info(s"moduleId: ${m.moduleID}")
          (outputDirectory / m.jsFileName) -> s"lib/${m.jsFileName}"
        }.toSeq
      },
      Universal / mappings ++= {
        val mappedResources = (Compile / resources).value
        mappedResources.filterNot(_.getName() == "custom.scss").map { r =>
          r -> s"${r.getName()}"
        }
      },
      Universal / mappings ++= {
        val mappedResources = (Compile / resources).value
        mappedResources.filter(_.getName() == "custom.scss").map { r =>
          r -> s"scss/${r.getName()}"
        }
      },
      Universal / stage := {
        val staging = (Universal / stage).value
        Process("npm i --include-dev", (Universal / stagingDirectory).value).!!
        Process("npm run build", (Universal / stagingDirectory).value).!!
        staging
      }
    )
    .dependsOn(common.js)
    .enablePlugins(UniversalPlugin)
