import com.typesafe.sbt.packager.docker._
import sbt._
import sbt.util.CacheImplicits._
import sbt.Keys._
import java.nio.file.Files
import sbt.util.CacheStore


object sbtExtensions{
  implicit class SeqExtensions[A](val underlying: Seq[A]){
    def updatedBy(a: A, pred: A => Boolean): Seq[A] = underlying.map{
      case item if pred(item) => a
      case item => item
    }
    def insertAt(index: Int, item: A): Seq[A] = {
      val (left, right) = underlying.splitAt(index)
      left ++ Seq(item) ++ right
    }
  }

  def baseImageCommand(baseImage: String) = {
    new ForStage{
      def forStage(stage: String): CmdLike = Cmd("FROM", "--platform=$BUILDPLATFORM", baseImage, "as", stage)
    }
  }

  abstract class ForStage {
    def forStage(stage: String): CmdLike
  }

  lazy val setupBuildX = taskKey[String]("Sets up the buildx appropriately.")

  lazy val dockerRegistryHostPort = settingKey[Int]("The docker registry host port. By default it is 5000, but you can change it either by setting this setting with sbt set or wwith the environment variable `SCALADAYS_WORKSHOP_DOCKER_REGISTRY_HOST_PORT`")

  lazy val dockerComposeFile = settingKey[File]("The docker compose yml file.")
  lazy val dockerComposeUp = taskKey[Unit]("Runs docker compose.")
  lazy val dockerComposeDown = taskKey[Unit]("Shuts down docker compose.")
  lazy val dockerComposeRestart = inputKey[Unit]("""|Usage: `scaladays-workshop-2023>dockerComposeRestart
                                                 |<serviceName>[
                                                 |<serviceName2>
                                                 |... <serviceNameN>]`. Restarts
                                                 |the service(s) named
                                                 |in the docker
                                                 |compose for the
                                                 |project.""".stripMargin)
  lazy val dockerComposeCommand = taskKey[File]("The docker compose command.")

  lazy val generateConfigToml = taskKey[Seq[File]]("Generates the config.toml so that the cross platform builder necessary for supporting mac m1s doesn't fail.")

  lazy val configTomlCacheStore = taskKey[CacheStore]("The cache store for config file generation.")

  def generateConfigTomlInManagedResourcesFrom(registryPortAndManagedResourcesDir:(Int, File)): Seq[File] = {
    val (registryPort, managedResourcesDir) = registryPortAndManagedResourcesDir
    val file = managedResourcesDir / "docker" / "registry" /"config.toml"
    IO.delete(file)
    IO.append(file, s"""|[registry."127.0.0.1:$registryPort"]
                      |http = true""".stripMargin)
    Seq(file)
  }

}
