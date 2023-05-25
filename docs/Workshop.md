# ScalaDays 2023 - TicTacToe

This workshop will guide you in building a 2-player real-time strategy game entirely using Scala 3. By the end of this workshop, you will gain knowledge about:

- Scala 3
- Scala.js
- Functional Domain Modeling
- Functional Reactive Programming with a style similar to [Elm](https://elm-lang.org/) using [Tyrian](https://github.com/purplekingdomgames/tyrian)
- Kafka consumers and producers
- Docker
- docker-compose
- sbt-native-packager
- sbt tasks
- Tagless Final
- http4s with websockets

## Prerequisites

To participate in this workshop, you will need:

1. A computer with a keyboard, monitor, and a wireless internet connection.
2. A desktop version of Docker, such as [Docker Desktop](https://www.docker.com/products/docker-desktop/) (which is free for open source projects) or [Rancher Desktop](https://rancherdesktop.io/) running the Docker daemon.
3. [Git](https://git-scm.com/).
4. For Windows users only: [Windows Subsystem for Linux](https://learn.microsoft.com/en-us/windows/wsl/install).
5. [Node.js](https://nodejs.org/en).
6. [Scala base installation](https://docs.scala-lang.org/getting-started/index.html).
7. Java 11+ (likely installed with [SDKMAN!](https://sdkman.io/)).
8. [Intellij IDEA with the Scala plugin](https://docs.scala-lang.org/getting-started/intellij-track/getting-started-with-scala-in-intellij.html), [VSCode with metals](https://scalameta.org/metals/docs/editors/vscode/), or any other [metals-compatible editor](https://scalameta.org/metals/docs/).

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
git reset --hard domain-modeling-1
```

And move on to the next step.

#### Functional Domain Modeling - The X's and O's of TicTacToe

Now that we have learned about sbt multiprojects, docker packaging, and the deployment of a full-stack system, we can start thinking about our domain.

There is almost nothing more important to the success of a programming project than a well-defined domain model. Scala 3 is well-suited for domain modeling. The familiar Scala 2 features that make Scala a great language for domain modeling:

* case classes
* pattern matching
* static typing
* type inference
* traits

all still exist in Scala 3.

However, new and improved features, including:

* opaque types
* improved enum types
* union types
* improved macro systems
* hlist-like tuples
* union and intersection types
* CanThrow
* Tasty

allow for more compile-time constraint restriction, discrete typing, and batteries-included domain modeling without the use of external libraries, like [Refined](https://github.com/fthomas/refined), [Scala Newtype](https://github.com/estatico/scala-newtype), [Shapeless 2](https://github.com/milessabin/shapeless/tree/series/2.3) and [scalameta](https://github.com/scalameta/scalameta).

A domain is the closed model of some universe of discourse. It's all
the objects, operations, and relationships within the universe in the
"real world". Obviously we can't simulate all the things in a "real"
pet store on a comupter system -- that would include all the physical
interactions of all the subatomic particles in the pet store and their
probabilistic behavior. So when we describe a domain we wish to
encode, we define only the macro-level objects, operations, and
relationships of the real domain relevant within the goals of the
execution of a computer simulation, or program.

Functional domain modeling is the practice of defining domain models
as immutable data values and predominately pure functions that operate
upon those models. It takes the practice of defining a model with
names and operations from a real universe of discourse and applies
functional principals to ensure that the data types are immutable and
side effecting operations are limited.

For today's workshop, we're modeling a few very small universes of
discourse. The first one is the game Tic Tac Toe. In Tic Tac Toe we
have a 3x3 board forming grid of positions players may play. There are
two players. There are two piece types: X and O. One player is
assigned to the X pieces, and another to the Y pieces. Players may not
swap pieces. The player assigned to X goes first and places an X in
any space on the grid. Only one piece can occupy a grid space at one
time. Once placed a piece cannot be moved. After X has placed their
piece, the O player places an O on one of the remaining open spaces on
the gamee board. Players continue to alternate until one player has 3
of the same pieces in a horizontal, vertical, or diagonal row of game
board spaces, in which case that player wins the game, or there are no
more spaces left upon which to play, which means the game is
tied. Both conditions result in the end of the game and require the
start of a new one.

Before we start the exercise, think about how you would design model
domain datatypes in Scala 3. What data types will you need? Will they
have fields? If so, what should they be named? Are there any
relationships between them?

##### Exercise 5

Since your task today is to produce a full stack Tic Tac Toe game using Scala and Scala.js, you need to use your only subproject that is built for *both* Scala and Scala.js to build your shared domain model of Tic Tac Toe. This is the `common` project, and its code is in `./modules/common/shared/src/main/scala/scaladays/models/`.

When first modeling a game's domain, think of the possible states the game can be in at any given time. In the description above, there are several states: turns, wins, and ties. In addition, as this is a simulation, there's a state where the game is "processing" the information sent to it from the players.

There are several options for how to model these states. They could be strings. They could be several independent case classes. They could be one case class with several separate boolean properties, all of which but 1 are true at any given time.

When we are modeling a domain, we should always try to preserve the commonalities between the objects in a domain, try to use the fewest objects possible to describe it, and keep the types as unique as possible with as few fields as possible. This is because, when testing or debugging or thinking about the program using the domain, you want to think about the fewest possible values an object of that type could be.

To make the fewest possible items, we have to know how to calculate the size of the different types of types Scala 3 can express.

Scala 3 has two fundamental types of types:

1. Product types combine other types together concurrently in a manner similar to a Cartesian product. They're called product types because the total possible values of the type is the product of the total possible values of the individual types. In Scala, product types are typically represented by case classes.

1. Sum types have a number of possible forms or alternatives. They're called sum types because the total possible values of the type is the sum of the total possible values of the individual types. In Scala 3, sum types are typically represented by sealed trait algebraic data types or, in more recent versions of Scala, enums or union types.

The number of possible values of a sum type is calculated as the sum of the possible values of each of its alternatives.

For example, the `Stoplight` enum below:

```scala
enum Stoplight:
  case Red, Yellow, Green
```

has:

```
possible values of Stoplight = possible values of Ref + possible values of Yellow + possible values of Green = 1 + 1 + 1 = 3
```

The number of possible values for a product type is calculated as the product of the possible values of each of its fields.

For example, a case class of an Int and a Short:

```scala
case class Example(s: Short, i: Int)
```

is

```
possible values of Example = possible values of Short * possible values of Int = (32,767 (for the positive values) + 1 (for zero) + + 32,768 (for the negative values)) * (2,147,483,647 (for the positive values) + 1 (for zero) + 2,147,483,648 (for the negative values)) = 65536 * 4294967296 = 2.81474976710660 * 10^14

```

because `s` is of type `Short` and `i` is of type `Int`.

There are also generic product types. These are classes or case classes that take generic parameters. When we program against a generic interface, the Generic parameters count as a type with 1 possible value. Thus, since product types possible values are the product of the possible values of their fields, we should use the generic interface whenever possible when working with product types because a type like:

```scala
case class Foo[A, B, C, D](a: A, b: B, c: C, d:D)
```

even as a product type has 1 possible value, because `A`,`B`,`C`,and `D` all have one possible value, and the number of possible values for the product type, `Foo[]` is

```
possible values of Foo = possible values of A * possible values of B * possible values of C * possible values of D = 1 * 1 * 1 * 1 = 1
```

The two types of types can be mixed, for example a sum type made up of product types.

```scala
enum Things:
  case This(a: Short) extends Things
  case That(a: Short) extends Things
  
```

which has:

```
number of possible values of This + number of possible values of That = 65536 + 65536 = 131,072
```

Or a product type of sum types:

```
enum Stoplight:
  enum Stoplight:
  case Red, Yellow, Green
  
case class Foo(s: Short, light: Stoplight)

```
.

In this case, `Foo` has:

```
number possible values of Short * number possible values of Stoplight = 65536 * 3 = 196,608
```

possible values.

For completeness, there is a final type, intersection types, which is the combination of all the types joined by the type intersection operator:

```scala
trait Foo
trait Bar

type FooBar = Foo & Bar
```

Which is calculated the same way as product types -- that is the size of Foo * size of Bar.

From the above, it should be obvious that to keep things simple to think about we should prefer to use sum types over generic product types whenever possible.

Open
`modules/common/shared/src/main/scala/scaladays/models/Game.scala`

It should look like this:

```scala
package scaladays.models


enum GameState:
  ???

```

Your task in this exercise, given the description of the game above and the goal of keeping the `GameState` type as simple as possible while retaining the relationships of the individual states, is to fill out the `GameState` enum.

The number of possible types for `GameState` should be 6. Once you
have it, save the file and move on to the next step by running:

```bash
git reset --hard domain-modeling-2
```

To move to the next step.

`Game.scala` should now be updated. The names may not be exactly the same as what you entered, but there should be exactly the same number of them and their meanings should be similar to yours.

We've now expanded the modeling to include the `Piece` type, the
`Position` type, and the `Movement` type. Game.scala should now look like this:


```scala
enum Piece:
  case Cross, Circle

final case class Position(x: Int, y: Int)

object Position:
  given Ordering[Position] = (x: Position, y: Position) => if (x.x == y.x) x.y - y.y else x.x - y.x

enum GameState:
  case CrossTurn, CircleTurn, Processing, CrossWin, CircleWin, Tie

final case class Movement(position: Position, piece: Piece, confirmed: Boolean = true)

final case class Game(gameId: GameId, crossPlayer: PlayerId, circlePlayer: PlayerId, state: GameState, movements: List[Movement])

```

In addition in `modules/common/shared/src/main/scala/scaladays/models` there is a new file: `ids.scala`.

Scala 3 added a great feature for modeling called Opaque Types. It allows you to create an alias for a type that, outside of the file where it was created, behaves as if it is an entirely separate type. This is similar to type aliases in Scala 2, but in Scala 2, when two type aliases are assigned to the same type, the two types are equal to each other and interchangeable:

```scala
// scala 2

object aliases{
type Foo = Int
type Bar = Int

case class UseFoo(f: Foo)
case class UseBar(b: Bar)

def makeUseFoo(b: Bar): UseFoo = UseFoo(b)
def makeUseBar(f: Foo): UseBar = UseBar(f)
```

With opaque types, you can't accidentally swap Bar and Foo like in the above:

```scala
//scala 3

opaque type Foo = Int

object Foo:
  def apply(i: Int): Foo = i
  
opaque type Bar = Int

object Bar:
  def apply(i: Int): Bar = i
  
case class UseFoo(f: Foo)
case class UseBar(b: Bar)

def makeUseFoo(b: Bar): UseFoo = UseFoo(b) // won't compile!
def makeUseBar(f: Foo): UseBar = UseBar(f) // won't compile!
```
.

##### Exercise 6

In the game model above, we have two new types: `GameId` and `PlayerId`.

To quote the FUUID site:

> Java UUID’s aren’t “exceptionally” safe. Operations throw and are not referentially transparent. We can fix that.
-- https://davenverse.github.io/fuuid/

To avoid possible pitfalls, you'll use `FUUID`s instead of `UUID`s in
our domain model.

However, you don't want to be able to swap `GameId` and `PlayerId`
anywhere we use the ids. So your job is to use opaque types to define
`GameId` and `PlayerId` so that that cannot happen. Include an apply
for each type like the opaque type example above.

`ids.scala` should look like this:

```scala

package scaladays.models

import java.util.UUID

import io.chrisdavenport.fuuid.FUUID

object ids:

  opaque type PlayerId = ???
  opaque type GameId   = ???
  
```

When you are done, you should be able to run:

```scala
ticTacToeRoot> common / console
import scaladays.models.ids._
import io.chrisdavenport.fuuid.FUUID
import cats.effect.unsafe.implicits._
import cats.effect.IO

FUUID.randomFUUID[IO].map(PlayerId.apply(_)).unsafeRunSync
FUUID.randomFUUID[IO].map(GameId.apply(_)).unsafeRunSync

```

In the sbt console. Enter `:quit` to exit.

Once it works without exceptions, you can enter:

```bash
git reset --hard server-1
```

To move to the next step.

#### Creating the game server

Now that you've established the basic model for your game, we need to
start talking about how to execute the model and allow two players to
play it. You could do all of the work on the client side. However,
that presents limitations. Only two players could play the game at a
time. Both players would have to be present at the same computer to
play. All the responsibilities of state management and processing as
well as the rendering and input handling must be performed on the
client, mixing the two concerns.

A more flexible solution is to create a web server to handle the
processing of the domain model. This allows the client to focus on the
user experience for the players and to offload game processing and
state management to the server.

The premier scala http library is [http4s](https://http4s.org/).

It provides a typed functional model of http servers, clients, and
websocket streaming communcations. It is built on libraries like
[fs2](https://fs2.io/#/), [cats](https://typelevel.org/cats/), and
[cats-effect](https://typelevel.org/cats-effect/). These libraries
work together very well, are well-documented, are extremely popular
(of the 33.8 M indexed artifacts in maven central, cats is #181,
cats-effect is the 288th most popular library, fs2 is #688,
http4s-core is #2271), used at industrial scale by companies like
Disney Streaming, and are supported by a dedicated, large, welcoming
community of scala, http, and functional programming experts.

##### Exercise 7

In this exercise, you are going to setup a basic webserver using
http4s. When you first setup an http server, the most basic thing you
can do is create an http endpoint to check that you can communicate
with it over http. This is typically called a "health check" endpoint.

The server code is in `./modules/server/src/main/scala/scaladays`.

<details>
  <summary>Deep-dive: Why we use Tagless Final in Scala without formal theory</summary>

  ###### Tagless Final Encoding

Before we go into the specifics of setting up an http4s server, let's
take a little time to discuss a functional programming architecture
called `tagless final` encoding. We're not going to go too deeply into
the theorhetical background of tagless final, but we are going to talk
about its practical uses.

As discussed in the domain modeling section, modeling a domain is not
just about the data objects in the domain, but about the operations on
those datatypes. Just as we want to constrain the size possible values
as tightly as possible, we want to constrain the size of the possible
implementations of the interfaces of those operations.

We calculate the size of a method or function in the same way as we
calculate the size of a data type. Because methods and functions have
arguments, they are inherently product types. They also have
**return** values, and so to calculate the possible values, we have to
take the product of the return type to the power of the possible
values of the return type. For a method foo:


```scala

def foo(x: Int, s: Short): Boolean
```

the calculation looks like this:

```
number possible implementations of foo = number of values of Boolean ^ (number of possible values of Int * number of possible values of Short) = 2 ^ (4294967296 * 65536) = Effectively Infinity
```
.

Obviously, this is quite a large interface. We would have to generate
a _lot_ of inputs to test to exhaustively verify the behavior of foo
any chosen foo implementation is the correct one. When the methods or
classes are grouped into a module or class, we can add the sizes of
the methods in the class together and calculate the size of the whole
interface. These are likewise, quite large.

To reduce this complexity, we want to limit the size of the interfaces
we create and use. We use the same methods to reduce this complexity
as we do when creating data types -- use Sum types whenever possible,
use generics whenever possible, and use small, constrained product
types as arguments and returns whenever necessary. In scala, we also
have the option of constraining the generic types passed to methods,
providing small, independent interfaces applied to generic types called
`Context Bounds`:

```scala
// here Labelable is a context bound on A, and Label is a Sum type
enum Label:
  case Sold, PendingSale, Undefined

trait Labelable[A]:
  def toLabel(a: A): Label

def foo[A: Labelable](a: A): Label
```

It's important to note that we don't have to express a context bound
in the generic parameters of a method in Scala 3. We can also express
it as a `using` parameter, a parameter to a `ContextFunction`, or a
normal parameter. All of the following mean the same thing.

```scala
def fooUsing[A](a: A)(using Labelable[A]): Label
def fooContextFunction[A](a: A): Labelable[A] ?=> Label
def fooNormal[A](a: A, labelable: Labelable[A]): Label
val fooVal: [A] => Labelable[A] ?=> Label = [A] => Labelable[A] ?=> (a: A) => summon[Labelable[A]].toLabel(a)
```

In FP, we like to delay side effects until the latest possible
moment. We do this, in general, by encoding programs as data types and
defining an interpreter that converts a datatype into an executable
program at runtime. This can be a little tedious without helper
interfaces. Imagine programming `fooVal` above like this:

```scala
Apply(
  Select(
    Apply(
	  TypeApply(
	    ClassOf(
		  Summon.class
	   ), 
	   List(
	     TypeTree(
		   TypeApply(
		     ClassOf(
			   Labelable.class
		     ),
			 List(
			   TypeParamRef("A")
		     )
		   )
	     )
	   )
	  ), 
	  List()
	), 
    "toLabel"
  ), 
  List(
    ParamRef("a")
  )
)
```

That is clearly not very ergonomic. `tagless final` allows us to use
all the complexity limiting tricks we talked about for values to
encode programs with small, parameterized methods which produce trees
of data values (for side effects) encoding a given, possibly
side-effecting domain, like say handling http requests and responses
behind the implementations of the interfaces. The data types that are
built then expose some sort of `run` method, which interprets the
value of the tree produced.

In this way, programs written with a tagless final encoding limit the
cumbersome data type encodings required to work with side-effecting
programs while still allowing users to reason about the code as a tree
of immutable values. This allows the programmer to refactor tagless
final programs safely.

It allows the designers of libraries to control and optimize the
interpretation of the trees produced by effectful programs without
adding additional complexity to the interfaces used by users of these
libraries. Method calls to the interfaces are free to modify the input
trees of previous calls, for example.

Meanwhile, users are prevented from violating the calling conventions
of the library by coding against the interface rather than the
implementation, enforced by the typechecker. The underlying complexity
is fully abstracted away from the user.

New methods can be added to a library without violating the source
compatibility of code built against earlier versions. And many other
advantages.

In general, tagless final programs do this in the same way as we
defined foo above, by inverting control to some interface over an
unknown effect type, generally encoded as `F[_]` in interfaces and
methods. The effect type is chosen later, when the program is executed
in main or in tests. As long as the effect data type chosen matches
the constraints of the interfaces passed to the methods within a
program, any effect data type will work.

In http4s and cats-based libraries, we generally choose to use
cats-effect's `IO` data type as the effect type.

Don't worry if you don't fully understand the above. Just know that
`tagless final` is a way of managing complexity through inversion of
control that produces programs as values, which are safer to refactor
during maintenance and limit the number of things a programmer has to
think about when reading the program.

  
</details>


Open `modules/server/src/main/scala/scaladays/Main.scala`. You should see this:

```scala
package scaladays

import scaladays.models.ServiceError

import cats.effect.{ExitCode, IO, IOApp}

import org.typelevel.log4cats.Logger
import org.typelevel.log4cats.slf4j.Slf4jLogger

object Main extends IOApp:

  given logger: Logger[IO] = Slf4jLogger.getLogger[IO]

  def run(args: List[String]): IO[ExitCode] =
    Server
      .serve[IO]
      .compile
      .drain
      .handleErrorWith { error =>
        logger.error(error)("Unrecoverable error") >>
          IO.raiseError[ExitCode](ServiceError(error.getMessage))
      }
      .as(ExitCode.Success)
```

Let's explain what this does:

- `Main` object extends `IOApp`. `IOApp` is a Cats-Effect trait that
  serves as a simple and safe entry point for an application which
  runs on the JVM. The main function of your application will run
  inside the `IO` context, providing safety and referential
  transparency.

- The `given` keyword is used to define an implicit value of type
  `Logger[IO]`, which represents a logger that works within the `IO`
  effect type. The `Slf4jLogger.getLogger[IO]` call creates a new
  logger backed by SLF4J, a logging facade for Java.

- The `run` method is the main method that Cats-Effect's `IOApp`
  requires you to implement. It takes a list of command-line arguments
  and returns an `IO` of `ExitCode`. `ExitCode` is a datatype
  representing the exit code of a process, with `Success` and `Error`
  being the primary examples.

- The body of `run` first calls `Server.serve[IO]`, which is a method
  that starts an HTTP server and returns a stream of `IO` actions
  representing the server's operation.

- `compile` is a method from fs2, a functional streams library for
  Scala. It transforms a stream into an effectful action. The action
  in this case is the server operation.

- `drain` is another method from fs2 which runs the stream purely for
  its effects, discarding any output.

- The `handleErrorWith` function is used to handle any errors that may
  occur when the server is running. If an error occurs, it logs an
  error message with the logger, and then raises an error of type
  `ExitCode` wrapped in the `IO` monad, using a `ServiceError` to wrap
  the original error message. The `>>` operator is used to sequence
  two `IO` actions, ensuring that the first action (logging the error)
  is completed before the second action (raising the error) is
  started.

- Finally, `.as(ExitCode.Success)` is used to map the result of the
  entire computation to `ExitCode.Success`, meaning that if the server
  runs successfully and then terminates, the application will exit
  with a success status.

Open `Server.scala`. You should see the following:

```scala
package scaladays

import scaladays.config.{ConfigurationService}
import scaladays.server.*
import cats.effect.{Async, ExitCode, Resource}
import cats.implicits.*
import org.http4s.server
import org.http4s.server.middleware.CORS
import org.http4s.server.websocket.WebSocketBuilder
import org.typelevel.log4cats.Logger

object Server:

  def serve[F[_]: Async: Logger]: fs2.Stream[F, ExitCode] =
    for
      configService <- fs2.Stream.eval(ConfigurationService.impl)
      routes         = (ws: WebSocketBuilder[F]) => HealthCheck.healthService
      stream        <- fs2.Stream.eval(
                         configService.httpServer
                           .withHttpWebSocketApp(ws =>
                             CORS.policy.withAllowOriginAll.withAllowCredentials(false).apply(routes(ws).orNotFound)
                           )
                           .build
                           .use(_ => Async[F].never[ExitCode])
                       )
    yield stream
```

This Scala 3 file defines a `Server` object that sets up an HTTP
server using http4s and Cats-Effect. Here is a breakdown of its
components:

- The `serve` method defines a server that operates inside an effect
  type `F[_]`. This type must have an `Async` instance (to provide
  asynchronous and concurrent operations) and a `Logger` instance (for
  logging). It returns a stream of `ExitCode` values, which represent
  the potential exit codes of the server process.

- Inside the for-comprehension, `configService <-
  fs2.Stream.eval(ConfigurationService.impl)` retrieves an instance of
  a configuration service by evaluating an effect. `fs2.Stream.eval`
  creates a stream that emits the result of a single effectful
  computation.

- The `routes` variable is defined as a function that takes a
  `WebSocketBuilder` and returns a `HealthCheck.healthService`. It
  provides a route for checking the health status of the service.

- The `stream` variable is a stream that emits the result of an
  effectful computation. This computation first retrieves the HTTP
  server configuration from the `configService`, configures it with a
  WebSocket application, builds the server, and then starts it. The
  server is wrapped in a resource using
  `configService.httpServer.build.use`, ensuring that the server will
  be properly shut down even if an error occurs.

- The WebSocket application is configured to use the CORS
  (Cross-Origin Resource Sharing) policy, which allows all origins and
  disallows credentials. This means that requests from any origin are
  allowed, but they cannot include credentials like cookies or HTTP
  authentication.

- The routes for the WebSocket application are given by the `routes`
  function defined earlier. If a request does not match any route, a
  404 Not Found response is returned (`routes(ws).orNotFound`).

- `Async[F].never[ExitCode]` creates an effect that never completes,
  which is used to keep the server running indefinitely. The server
  will only be shut down when the JVM process is terminated, or if an
  error occurs in the server.

- The for-comprehension yields the `stream` at the end, which is the
  stream of `ExitCode` values produced by the server. In this case, it
  will only ever emit a value if an error occurs in the server,
  because the server runs indefinitely.

Open `modules/server/src/main/scala/scaladays/config/ConfigurationService.scala`.

You should see the following:

```scala
package scaladays.config

import scaladays.models.{Configuration, HttpConfiguration}

import cats.effect.std.Dispatcher
import cats.effect.{Async, Resource}
import cats.implicits.*

import org.http4s.ember.server.EmberServerBuilder
import org.http4s.{Response, Status}

import com.comcast.ip4s.{Host, Port}
import org.typelevel.log4cats.Logger

trait ConfigurationService[F[_]]:

  def config: Configuration

  def httpServer: EmberServerBuilder[F]


object ConfigurationService:

  def impl[F[_]: Async: Logger]: F[ConfigurationService[F]] =
    def bootServer(httpConfiguration: HttpConfiguration): EmberServerBuilder[F] =
      EmberServerBuilder
        .default[F]
        .withHostOption(Host.fromString(httpConfiguration.host))
        .withPort(Port.fromInt(httpConfiguration.port).get)
        .withMaxHeaderSize(8 * 1024)
        .withIdleTimeout(scala.concurrent.duration.Duration.Inf)
        .withErrorHandler { case e =>
          Logger[F]
            .error(e)("Error in http server")
            .as(
              Response[F](Status.InternalServerError).putHeaders(org.http4s.headers.`Content-Length`.zero)
            )
        }
        .withOnWriteFailure { (optReq, response, failure) =>
          Logger[F].error(failure)(
            s"Error writing http response: \n\t- ${optReq.toString} \n\t- ${response.toString}"
          )
        }

    for
      conf     <- SetupConfiguration.loadConfiguration[F, Configuration]
    yield new ConfigurationService[F]:
      override lazy val config: Configuration                   = conf
      override lazy val httpServer: EmberServerBuilder[F]       = bootServer(
        conf.http.server
      )
```

This Scala 3 file defines a `ConfigurationService` trait and its
implementation, which encapsulates the configuration data and HTTP
server setup for the application.

Here's a breakdown of its components:

- `ConfigurationService` trait: This trait has two methods:
  - `config`: Returns a `Configuration` object. The actual
    configuration data is not shown in this snippet, but it likely
    includes settings such as server host, port, etc.
  - `httpServer`: Returns a `EmberServerBuilder[F]` object, which is
    used to build an HTTP server.

- `ConfigurationService.impl`: This method provides an implementation
  of the `ConfigurationService` trait. It requires an effect type
  `F[_]` that has an `Async` and a `Logger` instance. It returns a
  `ConfigurationService[F]` wrapped in an `F` effect.

- `bootServer` function: This function takes an `HttpConfiguration`
  and returns a server builder. It sets up the server host, port, max
  header size, idle timeout, and error handlers using the provided
  configuration. The error handlers log any errors that occur in the
  server and return a 500 Internal Server Error response. There is
  also a handler for write failures, which logs the failed request and
  response.

- The for-comprehension loads the configuration using
  `SetupConfiguration.loadConfiguration[F, Configuration]` and then
  creates a new `ConfigurationService` with this configuration. The
  `config` and `httpServer` methods of the service are implemented as
  lazy vals, meaning that they are only computed once, when they are
  first accessed.
  
  Open `SetupConfiguration.scala`. You should see the following:
  
  ```scala
  package scaladays.config

import scala.reflect.ClassTag

import cats.effect.Async
import cats.implicits.*

import com.typesafe.config.ConfigFactory
import org.typelevel.log4cats.Logger
import pureconfig.{ConfigReader, ConfigSource}

object SetupConfiguration:

  def loadConfiguration[F[_]: Async: Logger, C: ClassTag](using cr: ConfigReader[C]): F[C] =
    for
      classLoader <- Async[F].delay(
                       ConfigFactory.load(getClass().getClassLoader())
                     )
      config      <- Async[F].delay(
                       ConfigSource.fromConfig(classLoader).at("scaladays.workshop").loadOrThrow[C]
                     )
    yield config
```

This Scala 3 file defines a `SetupConfiguration` object that contains
a method for loading application configuration data. The configuration
data is loaded using the PureConfig and TypeSafe Config libraries.

Here's a breakdown of the file:

- `SetupConfiguration` object: This object has one method,
  `loadConfiguration`.

- `loadConfiguration` method: This generic method takes two type
  parameters `F[_]` and `C`. `F[_]` is the effect type, and `C` is the
  configuration type. The method requires the effect type `F[_]` to
  have an `Async` and `Logger` typeclass instance, and `C` to have a
  `ClassTag` and a `ConfigReader`. The method returns the
  configuration of type `C` wrapped in the effect type `F`.

    - `ClassTag` is a type class used by the Scala runtime system to
      retain type information for instances of generic types. It is
      used here to retain the type information about `C`, the
      configuration type.

    - `ConfigReader` is a type class from the PureConfig library. It
      provides a way to convert raw configuration data into a type
      `C`.

- Inside the method, a for-comprehension is used to load the
  configuration:
    - The `com.typesafe.config.Config` object is loaded using the
      class loader of the current class. The `Async[F].delay` function
      is used to suspend this operation in the `F` effect.
    - A `ConfigSource` is obtained by calling
      `ConfigSource.fromConfig(classLoader).at("scaladays.workshop")`. This
      specifies the path in the configuration file where the
      configuration data is located. `loadOrThrow[C]` is used to load
      and decode the configuration data into type `C`. If any error
      occurs during loading or decoding, an exception will be
      thrown. Again, `Async[F].delay` is used to suspend this
      operation in the `F` effect.
    - Finally, the loaded and decoded configuration is yielded.

The configuration file read by the `SetupConfiguration` object is
`modules/server/src/main/resources/application.conf`. Open it.

It should look like this:

```hocon
scaladays.workshop {
  http {
    server {
      host = "0.0.0.0"
      port = 8082
    }

    health {
      host = "0.0.0.0"
      port = 8083
    }
  }
}
```

The `application.conf` file contains configuration data for the
`scaladays.workshop` application. The configuration is mainly for the
HTTP server and health check services.

Here's a breakdown of the configuration:

- `scaladays.workshop`: This is the root path in the configuration and
  holds all configuration data for the `scaladays.workshop`
  application.

- `http`: Inside the `scaladays.workshop` block, there is an `http`
  block. This block contains configuration data related to HTTP
  services.

    - `server`: This block holds configuration data for the main HTTP
      server of the application.
        - `host`: This setting specifies the network interface the
          server should listen on. The value `"0.0.0.0"` means that
          the server should listen on all network interfaces.
        - `port`: This setting specifies the port number the server
          should listen on. The value `8082` means that the server
          should listen on port 8082.

    - `health`: This block contains configuration data for the health
      check service.
        - `host`: Similar to the `host` in the `server` block, this
          setting specifies the network interface the health check
          service should listen on. The value `"0.0.0.0"` means that
          the service should listen on all network interfaces.
        - `port`: Similar to the `port` in the `server` block, this
          setting specifies the port number the health check service
          should listen on. The value `8083` means that the service
          should listen on port 8083.

This configuration data can be loaded into your application using the
PureConfig library, as shown in the `SetupConfiguration.scala`
file. The loaded configuration data will be well-typed and can be used
to set up your application's HTTP server and health check service.

Open
`modules/server/src/main/scala/scaladays/server/HealthCheck.scala`. It
should look lke this:

```scala
package scaladays.server

import cats.effect.Async

import org.http4s.*
import org.http4s.dsl.*

object HealthCheck:

  def healthService[F[_]: Async]: HttpRoutes[F] =
    val dsl = new Http4sDsl[F] {}
    import dsl.*
      HttpRoutes.of[F] {
        case GET -> Root / "hello" => Ok("World!")
    }
```

The `HealthCheck.scala` file defines a `HealthCheck` object that
includes a method for setting up HTTP routes for health checking
services.

Here's a breakdown:

- `HealthCheck` object: This object contains one method,
  `healthService`.

- `healthService` method: This method takes a single type parameter
  `F[_]` which requires an `Async` typeclass instance. The method
  returns `HttpRoutes[F]`, a data type from the http4s library
  representing a set of HTTP routes.

Inside the `healthService` method:

- An instance of `Http4sDsl[F]` is created. `Http4sDsl` is a
  domain-specific language (DSL) for creating HTTP routes in a
  declarative way. The instance is assigned to `dsl`, and all members
  of `dsl` are then imported for use.

- `HttpRoutes.of[F]` is used to define a set of HTTP routes. One
  route is defined:
    - The route responds to a GET request to the path "/hello"
      with a 200 OK response containing the text "World!".

The `Async` typeclass in Cats Effect signifies a computation that may
execute asynchronously. It is used here because http4s is a
non-blocking, purely functional library for creating HTTP services,
which aligns well with non-blocking, asynchronous effect types like
the ones provided by Cats Effect.

Your task in this exercise is to define a healthcheck endpoint at GET
`/ping` that outputs "pong", using the example of the `/hello` route
in the file. When you are done defining the route, delete the example
`/hello` route.

You can test if your service is correct by runnig

```scala
dockerComposeUp
```

in the sbt console for your project, and opening:
`http://localhost:28082` in your browser when the server is fully
started. You should see `pong` on the page.

Congratulations! You've just set up your first real-world http4s
server. Run

```bash
docker-compose down
git reset --hard client-1
```

in your terminal to move on to the next step.
