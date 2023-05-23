# Scaladays 2023 - TicTacToe

The goal of this workshop is to develop a 2 player real-time strategy game using only Scala 3. In the process of this workshop, you will learn:

* Scala 3
* Scalajs
* Functional Domain Modeling
* Functional Reactive Programming in the style similar to [Elm](https://elm-lang.org/) using [Tyrian](https://github.com/purplekingdomgames/tyrian)
* Kafka consumers and producers
* Docker
* docker-compose
* sbt-native-packager
* sbt tasks
* Tagless Final
* http4s with websockets

## Prerequisites

1. A computer with a keyboard, monitor, and wireless internet connection.
2. A desktop-compatible docker installation, such as [Docker Desktop](https://www.docker.com/products/docker-desktop/), which is free for open source projects such as this one, or [Rancher Desktop](https://rancherdesktop.io/) running the docker daemon.
3. [Git](https://git-scm.com/).
4. Windows only - [Windows Subsystem for Linux](https://learn.microsoft.com/en-us/windows/wsl/install).
5. [Node](https://nodejs.org/en).
6. [The scala base installation](https://docs.scala-lang.org/getting-started/index.html).
7. Java 11+, probably installed with [SDKMAN!](https://sdkman.io/).
8. [Intellij IDEA with the scala plugin](https://docs.scala-lang.org/getting-started/intellij-track/getting-started-with-scala-in-intellij.html), [VSCode with metals](https://scalameta.org/metals/docs/editors/vscode/), or any other [metals-compatible editor](https://scalameta.org/metals/docs/).

## How to Launch Docker Desktop 

### For macOS Users

1. Open the Finder.
2. Navigate to the "Applications" folder.
3. Double-click on "Docker.app" to start Docker Desktop.

   - *Note:* A whale 🐳 icon in the top status bar signifies Docker Desktop is running and can be accessed from a terminal.

### For Windows Users

1. Click on the Windows start menu.
2. Begin typing "Docker Desktop".
3. When Docker Desktop appears in the search results, click on it to launch.

   - *Note:* A Docker icon in the Windows system tray signifies Docker Desktop is running and can be accessed from a terminal.

### For Linux Users (Using Ubuntu as an Example)

1. Click on the Ubuntu 'Show Applications' button located at the bottom of the sidebar, or press "Super+A".
2. Begin typing "Docker Desktop".
3. When Docker Desktop appears in the search results, click on it to launch.

   - *Note:* A Docker icon in the status bar signifies Docker Desktop is running and can be accessed from a terminal.

## How to Run the Project

To run the project, follow the steps below:

```bash
# Launch sbt
sbt

# Inside the sbt shell, enter the following command. This will incrementally compile the project and execute the `dockerComposeUp` task whenever a source file changes.
~scaladays-workshop-2023/dockerComposeUp
```

## Infrastructure

### build.sbt

#### Project Structure

[![](https://mermaid.ink/img/pako:eNqVkctqwzAQRX9FTLc2JMoioEIhfmwKWbl0U3chrEktrIeRx01NyL9Xdg0hu2Z3dTiagTsXaLxCEPAVZN-yt6x2jB0-ahgaaaSS05CefeiG1vcp3_BdDZ-zkc0Ghm8MK8gjIKLcaHS0siKyxlvr3ev7cWXljVUrOrA0fWHZLebLhiUWy-wllnM02nUVTQbZhg0UfIfiiWOj9tvk75metaJW8P7n-d7fPujzB_3dP3xIwGKwUqtY-GX-H0tr0WINIkaFJzma2F7trlGVI_lqcg0ICiMmMPZKEhZaxlPZe1gqTT6AOEkz4PUXfjyaBQ?type=png)](https://mermaid.live/edit#pako:eNqVkctqwzAQRX9FTLc2JMoioEIhfmwKWbl0U3chrEktrIeRx01NyL9Xdg0hu2Z3dTiagTsXaLxCEPAVZN-yt6x2jB0-ahgaaaSS05CefeiG1vcp3_BdDZ-zkc0Ghm8MK8gjIKLcaHS0siKyxlvr3ev7cWXljVUrOrA0fWHZLebLhiUWy-wllnM02nUVTQbZhg0UfIfiiWOj9tvk75metaJW8P7n-d7fPujzB_3dP3xIwGKwUqtY-GX-H0tr0WINIkaFJzma2F7trlGVI_lqcg0ICiMmMPZKEhZaxlPZe1gqTT6AOEkz4PUXfjyaBQ)

* The "scaladays-workshop-2023" project depends on the "server" and "tttClient" projects.
* The "server" project depends on the "commonJVM" project.
* The "tttClient" project depends on the "commonJS" project.

This means that changes in "commonJVM" will affect "server", and changes in "commonJS" will affect "tttClient". Both "server" and "tttClient" are part of the "scaladays-workshop-2023" project, so changes in these projects will affect the main project.

#### Intermediate/Advanced SBT concepts and documentation

1. **CrossProject**: A `crossProject` is a project that is built for both the JVM and JavaScript platforms. In this `build.sbt`, `common` is a `crossProject`. [Documentation](https://www.scala-js.org/doc/project/cross-build.html)

2. **ScalaJSPlugin**: This plugin enables the compilation of Scala code to JavaScript. It's used in the `tttClient` project. [Documentation](https://www.scala-js.org/doc/sbt-plugin.html)

3. **DockerPlugin**: This plugin is used to create Docker images for the application. It's used in the `server` project. [Documentation](https://www.scala-sbt.org/sbt-native-packager/formats/docker.html)

4. **JavaAppPackaging**: This plugin is part of the sbt-native-packager and is used to package JVM applications. It's used in the `server` project. [Documentation](https://www.scala-sbt.org/sbt-native-packager/archetypes/java_app/index.html)

5. **UniversalPlugin**: This plugin is part of the sbt-native-packager and is used to create archives containing all project files (including source files). It's used in the `tttClient` project. [Documentation](https://www.scala-sbt.org/sbt-native-packager/formats/universal.html)

6. **aggregate**: This method is used to create a list of tasks that are run on the aggregate project and all the aggregated projects. In this `build.sbt`, the `scaladays-workshop-2023` project aggregates the `server` and `tttClient` projects. [Documentation](https://www.scala-sbt.org/1.x/docs/Multi-Project.html#Aggregation)

7. **dependsOn**: This method declares that a project depends on other projects. In this `build.sbt`, the `scaladays-workshop-2023` project depends on the `server` and `tttClient` projects. [Documentation](https://www.scala-sbt.org/1.x/docs/Multi-Project.html#Classpath+dependencies)

8. **Docker / dockerCommands**: This setting allows you to customize the Dockerfile that is generated by the DockerPlugin. [Documentation](https://www.scala-sbt.org/sbt-native-packager/formats/docker.html#settings)

9. **ScalaJSLinkerConfig**: This setting allows you to configure the Scala.js linker, which is responsible for linking your Scala.js code into a single JavaScript file. [Documentation](http://www.scala-js.org/doc/project/module.html)

#### Custom Sbt tasks

1. **dockerComposeCommand**: This task is used to determine the path to the `docker-compose` command on the system. It tries to find the command using the `which` command on Unix-like systems or the `where` command on Windows systems. If the command is not found, an exception is thrown.
    ```scala
    dockerComposeCommand := {
      Paths
        .get(Try("which docker-compose".!!).recoverWith { case _ =>
          Try("where docker-compose".!!)
        }.get)
        .toFile
    }
    ```

2. **dockerRegistryHostPort**: This task is used to get the Docker registry host port from an environment variable named `SCALADAYS_WORKSHOP_DOCKER_REGISTRY_HOST_PORT`. If the environment variable is not set, it defaults to `5000`.
    ```scala
    dockerRegistryHostPort := {
      Properties
        .envOrElse("SCALADAYS_WORKSHOP_DOCKER_REGISTRY_HOST_PORT", "5000")
        .toInt
    }
    ```

3. **dockerComposeFile**: This task is used to specify the path to the `docker-compose.yml` file, which is located in the `src` directory.
    ```scala
    dockerComposeFile := (Compile / baseDirectory).value / "src" / "docker-compose.yml"
    ```

4. **dockerComposeUp**: This task is used to start the Docker Compose environment. It runs several tasks sequentially:
   - It sets up Docker BuildX in the `server` project.
   - It publishes the `server` Docker image locally.
   - It stages the `tttClient` project.
   - It logs the Docker Compose file after environment variable interpolation.
   - It starts the Docker Compose environment in detached mode.
    ```scala
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
      .value
    ```

5. **dockerComposeDown**: This task is used to stop the Docker Compose environment and remove all images. It runs the `docker-compose down --rmi all` command.
    ```scala
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
    ```

6. **generateConfigToml**: This task in the `server` project generates a `config.toml` file in the managed resources directory from a given Docker registry host port.
    ```scala
    generateConfigToml := {
        generateConfigTomlInManagedResourcesFrom(
          (
            (Docker / dockerRegistryHostPort).value,
            (Compile / resourceManaged).value
          )
        )
    }
    ```

7. **Docker / setupBuildX**: This task in the `server` project sets up Docker BuildX. It runs several commands to install `binfmt`, stop and remove the Docker registry, and set up Docker BuildX.
    ```scala
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
    }
    ```

8. **Universal / stage**: This task in the `tttClient` project stages the project and runs `npm i --dev` and `npm run build` commands in the staging directory.
    ```scala
    Universal / stage := {
        val staging = (Universal / stage).value
        Process("npm i --dev", (Universal / stagingDirectory).value).!!
        Process("npm run build", (Universal / stagingDirectory).value).!!
        staging
      }
    ```

#### A word about scalajs packaging for client resources without `sbt-web`

The `Universal / mappings` setting in the `tttClient` project is used to specify the files that should be included in the package when the project is packaged. It's a sequence of tuples, where each tuple consists of a file and its path in the package.

There are three `Universal / mappings` settings in the `tttClient` project:

1. **Mapped Resources**: This setting maps non-SCSS resources in the `resources` directory to the root of the package.

    ```scala
    Universal / mappings ++= {
      val mappedResources = (Compile / resources).value
      mappedResources.filterNot(_.getName() == "custom.scss").map { r =>
        r -> s"${r.getName()}"
      }
    }
    ```

2. **Mapped SCSS Resources**: This setting maps SCSS resources in the `resources` directory to the `scss` directory of the package.

    ```scala
    Universal / mappings ++= {
      val mappedResources = (Compile / resources).value
      mappedResources.filter(_.getName() == "custom.scss").map { r =>
        r -> s"scss/${r.getName()}"
      }
    }
    ```

3. **Mapped Scala.js Linked Files**: This setting maps all linked files from the Scala.js fastLinkJS task to the `lib` directory of the package.

    ```scala
    Universal / mappings ++= {
        val log = streams.value.log
        val report = (Compile / fastLinkJS).value
        val outputDirectory =
          (Compile / fastLinkJS / scalaJSLinkerOutputDirectory).value
        report.data.publicModules.map { m =>
          log.info(s"moduleId: ${m.moduleID}")
          (outputDirectory / m.jsFileName) -> s"lib/${m.jsFileName}"
        }.toSeq
      }
    ```

These settings ensure that all the necessary files are included in the package when the project is packaged.
