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

1. **CrossProject**: A `crossProject` is a project that is built for multiple platforms. In our case, the JVM and JavaScript platforms. In this `build.sbt`, `common` is a `crossProject`. [Documentation](https://www.scala-js.org/doc/project/cross-build.html)

2. **ScalaJSPlugin**: This plugin enables the compilation of Scala code to JavaScript. It's used in the `tttClient` project. [Documentation](https://www.scala-js.org/doc/project/)

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

#### Sbt and Docker

##### Why all the Docker complexity in the build?

As will be discussed in a later section on `docker-compose` and the runtime architecture of the workshop project, we are running Kafka as our datastore, and the Confluent schema-registry as our serialization format store. This means that we need to wait for Kafka and the schema-registry to be available before we can start our server project. **docker-compose** allows us to express and enforce this condition with a single user command.

We are also using library dependencies to communicate with Kafka that depend on a native–binary JNI dependency that is not available for Mactintosh silicon CPUs like the M1. The JNI dependency is a **transient dependency** of one of our  library dependencies. This library has not updated to the latest version of the JNI library, so we need a different solution.  Docker may be able to provide an isolated process, but a docker container is not a full Virtual machine. Anything running in a docker container will use the host system's native runtime platform by default.

Therefore, we need to run the container in a virtual machine capable of emulating a linux/amd64 **platform** so that the native dependency will execute correctly.

Finally we must build the image using the emulated linux/amd64 environment such that it will run under linux/amd64 emulation to enable our JNI Kafka dependency to execute on incompatible machines.


##### SERVER / dockerCommands:

The `dockerCommands` setting in the `server` project is used to specify the Docker commands that should be run when the Docker image for the `server` project is built. It's a sequence of `Cmd` and `ExecCmd` objects, each representing a Docker command.

Here's a breakdown of the `dockerCommands` setting:

1. **Base Image Command for Stage0 and Mainstage**: These commands set the base image for the Docker image. `updatedBy` finds the command that contains "From", "openjdk:8" and "stage0" in the original default dockerCommands and swaps it for one that uses the build's `Docker / dockerBaseImage`. This is necessary because though the Docker plugin allows you to specify a `dockerBaseImage`, it doesn't actually use that image in the docker built for the project. For the definition of `updatedBy` and `baseImageCommand`, see `project/sbtExtensions.scala`.

    ```scala
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
        baseImageCommand((Docker / dockerBaseImage).value).forStage("mainstage"),
        c =>
          c match {
            case Cmd("FROM", args @ _*) =>
              args.contains("openjdk:8") && args.contains("mainstage")
            case _ => false
          }
      )
    ```

2. **Add Command**: This command adds the `wait-for-it.sh` script from a URL to the Docker image. The script is used to wait for a service to be available. `insertAt` inserts this command at index 6 of the updated default commands `Seq`. For the definition of `insertAt` see `project/sbtExtensions.scala`.

    ```scala
    dockerCommands.value.insertAt(
      6,
      Cmd(
        "ADD",
        "--chmod=u=rX,g=rX",
        "https://raw.githubusercontent.com/vishnubob/wait-for-it/master/wait-for-it.sh",
        "/4/opt/docker/bin/wait-for-it.sh"
      )
    )
    ```

3. **Run Command**: This command runs the `stat` command to display information about the Docker image's `/4/opt/docker` directory.

    ```scala
    dockerCommands.value.insertAt(10, Cmd("RUN", "stat", "/4/opt/docker"))
    ```

4. **Run Command to Change Permissions**: This command changes the permissions of the `wait-for-it.sh` script to make it executable after copying from the original build stage.

    ```scala
    dockerCommands.value.insertAt(20, ExecCmd("RUN", "chmod", "+x", "/opt/docker/bin/wait-for-it.sh"))
    ```

5. **Entrypoint Command**: This command sets the entrypoint for the Docker image. The entrypoint is a script that waits for the `schema-registry` service to be available before running the `scaladays-workshop-2023-server` application.

    ```scala
    dockerCommands.value.updatedBy(
      ExecCmd(
        "ENTRYPOINT",
        "/opt/docker/bin/wait-for-it.sh",
        "schema-registry:8081",
        "--timeout=30",
        "--strict",
        "--",
        "/opt/docker/bin/scaladays-workshop-2023-server",
        "-verbose"
      ),
      c =>
        c match {
          case ExecCmd("ENTRYPOINT", _) => true
          case _                        => false
        }
    )
    ```

These commands ensure that the Docker image is built correctly and that the application starts correctly when the Docker image is run.

In the context of defining Docker commands in the `dockerCommands` setting, there are two types of commands used: `ExecCmd` and `Cmd`.

**ExecCmd**:
- The `ExecCmd` type represents an executable command that is run within the Docker container.
- It is used when you want to execute a command that performs an action inside the container.
- `ExecCmd` commands are executed directly by the shell without any wrapping.
- You can use `ExecCmd` to run shell commands, scripts, or executables.
- More information about `ExecCmd` can be found in the [sbt-native-packager documentation](https://sbt-native-packager.readthedocs.io/en/latest/formats/docker.html).

**Cmd**:
- The `Cmd` type represents a command that is executed when the Docker container starts.
- It is used when you want to specify the primary command to be run when the container is launched.
- `Cmd` commands are specified as an array of arguments to the command.
- The command specified by `Cmd` is the main process running inside the container.
- More information about `Cmd` can be found in the [sbt-native-packager documentation](https://sbt-native-packager.readthedocs.io/en/latest/formats/docker.html).

When defining Docker commands in the `dockerCommands` setting, you can use both `ExecCmd` and `Cmd` to perform different actions at different stages of the Docker image lifecycle.

### System Architecture

[![](https://mermaid.ink/img/pako:eNqVk0tPhDAQgP9KU6-QCKirmHhZPSGX5YZ4qHSAhmdKUdfN_neH5y7rxd5mvvmYKU3nQOOaA3VpKlmTkdddVBHSdh9jGr5FNKzrHKABGdH3vhhaCH9maFoztlfYnrGzws6EoeKrQR5aHktyNtW9fkjeg2WAZy9obu45C7poHOyw0sYZlMyUkIpWyf1kBH0F5OfyQz6CkqUinvIt5nEhoFLzGGKaTyQc-_6JvSE-w5c0GO7UPxn-yfDPjO0Y92EhqjxQ-wLINcGj4-25VzbEfGMZY2p-Ca4y126-H9e-penbmr6j6d9o-rea_p2mv9H07zX9h3_41KAlyJIJjnt36L-PqMKnChF1MeSQsK7AxxdVR1RZp-pgX8XUVbIDg3YNZwqeBcPNKdfwhQtVS-omrGgRwpD6434Pa378Bd5oSPk?type=png)](https://mermaid.live/edit#pako:eNqVk0tPhDAQgP9KU6-QCKirmHhZPSGX5YZ4qHSAhmdKUdfN_neH5y7rxd5mvvmYKU3nQOOaA3VpKlmTkdddVBHSdh9jGr5FNKzrHKABGdH3vhhaCH9maFoztlfYnrGzws6EoeKrQR5aHktyNtW9fkjeg2WAZy9obu45C7poHOyw0sYZlMyUkIpWyf1kBH0F5OfyQz6CkqUinvIt5nEhoFLzGGKaTyQc-_6JvSE-w5c0GO7UPxn-yfDPjO0Y92EhqjxQ-wLINcGj4-25VzbEfGMZY2p-Ca4y126-H9e-penbmr6j6d9o-rea_p2mv9H07zX9h3_41KAlyJIJjnt36L-PqMKnChF1MeSQsK7AxxdVR1RZp-pgX8XUVbIDg3YNZwqeBcPNKdfwhQtVS-omrGgRwpD6434Pa378Bd5oSPk)

The system architecture is described in `src/docker-compose.yml`. This is the first interactive step in the workshop. You will first setup a zookeeper cluster with three nodes. Please make a fresh clone of this repository and run

```bash
git checkout setup-zookeeper
```

In the freshly cloned workspace.

Follow the instructions below:

#### Setting up a ZooKeeper Cluster

To set up a ZooKeeper cluster using Docker Compose, follow these steps:

1. Open `src/docker-compose.yml`
2. You should see the following code:

```yaml
version: "3.5"
services:
  zookeeper-1:
    image: confluentinc/cp-zookeeper:latest
    environment:
      ZOOKEEPER_SERVER_ID: 1
      ZOOKEEPER_CLIENT_PORT: 2181
      ZOOKEEPER_TICK_TIME: 2000
      ZOOKEEPER_INIT_LIMIT: 5
      ZOOKEEPER_SYNC_LIMIT: 2
      ZOOKEEPER_SERVERS: zookeeper-1:22888:23888;zookeeper-2:32888:33888;zookeeper-3:42888:43888
  zookeeper-2:
    image: confluentinc/cp-zookeeper:latest
    environment:
      ZOOKEEPER_SERVER_ID: 2
      ZOOKEEPER_CLIENT_PORT: 2181
      ZOOKEEPER_TICK_TIME: 2000
      ZOOKEEPER_INIT_LIMIT: 5
      ZOOKEEPER_SYNC_LIMIT: 2
      ZOOKEEPER_SERVERS: zookeeper-1:22888:23888;zookeeper-2:32888:33888;zookeeper-3:42888:43888
  zookeeper-3:
    image: confluentinc/cp-zookeeper:latest
    environment:
      ZOOKEEPER_SERVER_ID: ???
      ZOOKEEPER_CLIENT_PORT: ???
      ZOOKEEPER_TICK_TIME: 2000
      ZOOKEEPER_INIT_LIMIT: 5
      ZOOKEEPER_SYNC_LIMIT: 2
      ZOOKEEPER_SERVERS: ???
```

##### Exercise 1

This Docker Compose configuration sets up a ZooKeeper cluster with three nodes: `zookeeper-1`, `zookeeper-2`, and `zookeeper-3`. Let's explain the key elements:

`version: "3.5"`: Specifies the Docker Compose file version.

`services`: Defines the list of services to be created.

`zookeeper-1, zookeeper-2, zookeeper-3`: Each service represents an individual ZooKeeper node. The numbers at the end of the service names (`-1`, `-2`, `-3`) distinguish between the different nodes.

`image: confluentinc/cp-zookeeper:latest`: Specifies the Docker image to be used for the ZooKeeper service. In this case, it uses the latest version of the `confluentinc/cp-zookeeper` image provided by Confluent.

`environment`: Sets environment variables for the ZooKeeper service.

`ZOOKEEPER_SERVER_ID`: Specifies the unique ID for the ZooKeeper node. Each node in the cluster must have a unique ID.

`ZOOKEEPER_CLIENT_PORT`: Defines the port number on which ZooKeeper listens for client connections.

`ZOOKEEPER_TICK_TIME`: Sets the length of a single tick, which is the basic time unit used by ZooKeeper.

`ZOOKEEPER_INIT_LIMIT`: Defines the time (in ticks) that ZooKeeper servers can take to connect and synchronize with each other.

`ZOOKEEPER_SYNC_LIMIT`: Specifies the maximum time (in ticks) that ZooKeeper servers can be out of sync with each other.

`ZOOKEEPER_SERVERS`: Sets the list of ZooKeeper servers in the format `server:id1:host1:port1;server:id2:host2:port2;....` This configuration helps ZooKeeper nodes discover and connect to each other in the cluster.

You need to fill in the ZOOKEEPER_SERVER_ID, ZOOKEEPER_CLIENT_PORT, and ZOOKEEPER_SERVERS environment variables for the zookeeper-3 service to complete the configuration. After filling it in, you can check that the zookeeper cluster is setup appropriately by saving the file, and running:

```bash
docker-compose up -d
docker-compose logs -f zookeeper-3
```

in a terminal window in your clone's `src/` directory that contains the docker-compose.yml.

These commands launch the ZooKeeper containers in the background (-d flag) according to the configuration specified in the `docker-compose.yml` file, and display the logs of the zookeeper node you filled out in the exercise. In the logs you should see no errors and eventually see an info log like following (it may take a few moments):

```shell
INFO [QuorumPeer[...]]: INFO ... - Accepted socket connection from /<IP_Address>:<Port>
```

Congratulations, you've setup your zookeeper cluster. Stop following the logs with (<Cmd|Ctrl> C). You can now tear it down with:

```bash
docker-compose down
git reset --hard setup-kafka
```

in the terminal and move on to setting up kafka.

#### Setting up a Kafka Cluster

To set up a Kafka cluster using Docker Compose, continue from the previous step and follow these additional steps:

1. Copy the following code below the ZooKeeper services in the 'src/docker-compose.yml' file in the workspace:

```yaml
  kafka-1:
    image: confluentinc/cp-kafka:latest
    depends_on:
      - zookeeper-1
      - zookeeper-2
      - zookeeper-3
    environment:
      KAFKA_BROKER_ID: 1
      KAFKA_ZOOKEEPER_CONNECT: zookeeper-1:2181,zookeeper-2:2181,zookeeper-2:2181
      KAFKA_ADVERTISED_LISTENERS: PLAINTEXT://kafka-1:9092
      KAFKA_JMX_PORT: 9998
  kafka-2:
    image: confluentinc/cp-kafka:latest
    depends_on:
      - zookeeper-1
      - zookeeper-2
      - zookeeper-3
    environment:
      KAFKA_BROKER_ID: 2
      KAFKA_ZOOKEEPER_CONNECT: zookeeper-1:2181,zookeeper-2:2181,zookeeper-2:2181
      KAFKA_ADVERTISED_LISTENERS: PLAINTEXT://kafka-2:9092
      KAFKA_JMX_PORT: 9998
  kafka-3:
    image: ???
    depends_on:
      ???
    environment:
      KAFKA_BROKER_ID: ???
      KAFKA_ZOOKEEPER_CONNECT: ???
      KAFKA_ADVERTISED_LISTENERS: ???
      KAFKA_JMX_PORT: 9998
```

##### Exercise 2

This snippet defines three Kafka services, `kafka-1`, `kafka-2`, and `kafka-3`, within the Docker Compose configuration. Let's break down the key elements:

`kafka-1` and `kafka-2`:

`image: confluentinc/cp-kafka:latest`: Specifies the Docker image to be used for the Kafka service. In this case, it uses the latest version of the confluentinc/cp-kafka image provided by Confluent.

`depends_on`: Specifies the services that this Kafka service depends on. In this case, it depends on `zookeeper-1`, `zookeeper-2`, and `zookeeper-3`, ensuring that the ZooKeeper cluster is started before the Kafka service.

`environment`: Sets environment variables for the Kafka service.

`KAFKA_BROKER_ID`: Specifies the unique ID for the Kafka broker. Each broker in the cluster must have a unique ID.

`KAFKA_ZOOKEEPER_CONNECT`: Defines the ZooKeeper connection string for the Kafka broker. It specifies the addresses of the ZooKeeper nodes that Kafka will connect to for coordination.

`KAFKA_ADVERTISED_LISTENERS`: Specifies the listener configuration for the Kafka broker. In this case, it sets the listener to `PLAINTEXT` protocol and defines the advertised listener address as `kafka-1:9092 or kafka-2:9092`. Clients will use these addresses to connect to the respective Kafka brokers.

`KAFKA_JMX_PORT`: Defines the JMX (Java Management Extensions) port for monitoring and managing the Kafka broker.

`kafka-3`:

Similar to `kafka-1` and `kafka-2`, this section defines the configuration for the `kafka-3` service.
However, the `image`, `depends_on`, and `environment` variable values are left blank (???) and need to be filled in according to your specific setup.
To configure the `kafka-3` service correctly, you need to provide the appropriate values for `image`, `depends_on`, `KAFKA_BROKER_ID`, `KAFKA_ZOOKEEPER_CONNECT`, and `KAFKA_ADVERTISED_LISTENERS` based on the values for `kafka-1` and `kafka-2`.

After filling it in, you can check that the kafka cluster is setup appropriately by saving the file, and running:

```bash
docker-compose up -d
docker-compose logs -f kafka-3
```

in a terminal window in your clone's `src/` directory that contains the docker-compose.yml.

These commands launch the ZooKeeper and Kafka containers in the background (-d flag) according to the configuration specified in the `docker-compose.yml` file, and display the logs of the Kafka node you filled out in the exercise. In the logs you should see no errors and eventually see an info log like following (it may take a few moments):

```shell
INFO [KafkaServer id=<broker_id>] started (kafka.server.KafkaServer)
```

Congratulations, you've setup your kafka cluster. Stop following the logs with (<Cmd|Ctrl> C). You can now tear it down with:

```bash
docker-compose down
git reset --hard add-other-services
```

in the terminal and move on to adding the rest of the docker-compose configuration.

#### Adding the schema-registry, kafka monitor, the game client and server

To set up a the kafka-magic monitor, and the game client and server in the docker-compose:

1. Copy the following code below the kafka services in the 'src/docker-compose.yml' file in the workspace:

```yaml
schema-registry:
    image: "confluentinc/cp-schema-registry:6.2.0"
    hostname: schema-registry
    depends_on:
      - zookeeper-1
      - zookeeper-2
      - zookeeper-3
      - kafka-1
      - kafka-2
      - kafka-3
    ports:
      - "8081:8081"
    environment:
      SCHEMA_REGISTRY_HOST_NAME: schema-registry
      SCHEMA_REGISTRY_KAFKASTORE_BOOTSTRAP_SERVERS: 'PLAINTEXT://kafka-1:9092,PLAINTEXT://kafka-2:9092,???'
  server:
    image: "127.0.0.1:${SCALADAYS_WORKSHOP_DOCKER_REGISTRY_HOST_PORT?Cannot find host port}/scaladays-workshop-2023-server:latest"
    platform: linux/amd64
    hostname: scaladays-workshop-2023-server
    restart: always
    environment:
      ROOT_LOG_LEVEL : ERROR
    ports:
      - "28082:8082"
      - "28083:8085"
    depends_on:
      - zookeeper-1
      - zookeeper-2
      - zookeeper-3
      - kafka-1
      - kafka-2
      - kafka-3
      - schema-registry
  magic:
    image: "digitsy/kafka-magic"
    ports:
      - "29080:80"
    depends_on:
      - zookeeper-1
      - zookeeper-2
      - zookeeper-3
      - kafka-1
      - kafka-2
      - kafka-3
      - schema-registry
    volumes:
      - myConfig:/config
    environment:
      KMAGIC_ALLOW_TOPIC_DELETE: "true"
      KMAGIC_ALLOW_SCHEMA_DELETE: "true"
      KMAGIC_CONFIG_STORE_TYPE: "file"
      KMAGIC_CONFIG_STORE_CONNECTION: "Data Source=/config/KafkaMagicConfig.db;"
      KMAGIC_CONFIG_ENCRYPTION_KEY: "ENTER_YOUR_KEY_HERE"
  client:
    image: "lipanski/docker-static-website:latest"
    ports:
      - 23000:3000
    depends_on:
      - server
    volumes:
      - ${SCALADAYS_CLIENT_DIST?Cannot find scaladays client distribution}:/home/static
      - ${SCALADAYS_CLIENT_DIST?Cannot find scaladays client distribution}/httpd.conf:/home/static/dist/httpd.conf
volumes:
  myConfig:
```

##### Exercise 3

Let's examine the above snippet.

`schema-registry`:

`image`: "confluentinc/cp-schema-registry:6.2.0": Specifies the Docker image to be used for the Schema Registry service. In this case, it uses version 6.2.0 of the `confluentinc/cp-schema-registry` image provided by Confluent.

`hostname: schema-registry`: Sets the hostname for the Schema Registry container.
`depends_on`: Specifies the services that the Schema Registry service depends on. It requires the ZooKeeper cluster (`zookeeper-1`, `zookeeper-2`, `zookeeper-3`) and the Kafka brokers (`kafka-1`, `kafka-2`, `kafka-3`) to be running before starting the Schema Registry service.

`ports`: Maps the container's port 8081 to the host's port 8081, allowing access to the Schema Registry service from the host machine.

`environment`: Sets environment variables for the Schema Registry service.

`SCHEMA_REGISTRY_HOST_NAME`: Specifies the hostname for the Schema Registry service.

`SCHEMA_REGISTRY_KAFKASTORE_BOOTSTRAP_SERVERS`: Defines the bootstrap servers for the Schema Registry to connect to Kafka. In this case, it provides the addresses of `kafka-1:9092` and `kafka-2:9092`. Replace ??? with the appropriate address for the third Kafka broker.

`server`:

`image: "127.0.0.1:${SCALADAYS_WORKSHOP_DOCKER_REGISTRY_HOST_PORT?Cannot find host port}/scaladays-workshop-2023-server:latest"`: Specifies the Docker image to be used for the server service. The image location is determined using an environment variable `${SCALADAYS_WORKSHOP_DOCKER_REGISTRY_HOST_PORT}` to fetch the host port.

`platform: linux/amd64`: Specifies the platform (architecture) for the server container.

`hostname: scaladays-workshop-2023-server`: Sets the hostname for the server container.

`restart`: always: Configures the container to automatically restart if it stops for any reason.

`environment`: Sets environment variables for the server service.

`ROOT_LOG_LEVEL`: Specifies the log level for the server application. In this case, it is set to `ERROR`.

`ports`: Maps the container's ports 8082 and 8085 to the host's ports 28082 and 28083, respectively, allowing access to the server service from the host machine.

`depends_on`: Specifies the services that the server service depends on. It requires the ZooKeeper cluster, Kafka brokers, and Schema Registry to be running before starting the server service.

`magic`:

`image: "digitsy/kafka-magic"`: Specifies the Docker image to be used for the Magic service. In this case, it uses the `digitsy/kafka-magic` image.

`ports`: Maps the container's port 80 to the host's port 29080, allowing access to the Magic service from the host machine.

`depends_on`: Specifies the services that the Magic service depends on. It requires the ZooKeeper cluster, Kafka brokers, and Schema Registry to be running before starting the Magic service.

`volumes`: Mounts the volume myConfig to the /config directory within the container.

`environment`: Sets environment variables for the Magic service, including configuration options related to topics and schemas.

`client`:

`image: "lipanski/docker-static-website:latest"`: Specifies the Docker image to be used for the client service. In this case, it uses the `lipanski/docker-static-website` image.

`ports`: Maps the container's port 3000 to the host's port 23000, allowing access to the client service from the host machine.

`depends_on`: Specifies that the client service depends on the server service to be running before starting.

`volumes`:

`myConfig`: Defines a named volume called myConfig that can be used for persistent data storage.


After filling in the blank for the kafka-3 server, you can check that everything is setup appropriately by saving the file, runnig:

```bash
docker-compose up -d
```

in a terminal window in your clone's `src/` directory that contains the docker-compose.yml, opening `http://localhost:29080` in your browser and follow the following steps:

1. Open [Kafka Magic](http://localhost:29080/cluster).
1. Click Register New.
1. Enter `Scaladays Workshop` in the `Cluster Name` input.
1. Enter `kakfa-1:9092,kafka-2:9092,kafka-1:9092` in the `Bootstrap Servers` input.
1. Click `Schema Registry`.
1. Enter `http://schema-registry:8081` in the `Schema Registry URL` input.
1. Toggle `Auto-register schemas` to true.
1. Click `Verify`. An alert will show success. Close it.
1. Click Register Connection. Your cluster is registered.

Congratulations, you've setup your infrastructure. You can now tear it down with:

```bash
docker-compose down
git reset --hard domain-modeling
```

And move on to the next step.


