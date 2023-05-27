import sbt._
object Dependencies {

  // define versions, The variable name must be camel case by module name
  object Versions {
    val scalacssCore = "1.0.0"
    val scalajsStubs = "1.1.0"
    val react = "18.2.0"
    val reactDom = "18.2.0"
    val scalajsDom = "2.4.0"
    val tyrian = "0.6.2"
    val scalajsReactCore = "2.1.1"
    val extReact = "1.0.0"
    val sbtScalajsCrossProject = "0.5.0"
    val sbtScalaJs = "1.13.1"
    val sbtScalajsBundler = "0.21.1"
    val munit = "0.7.29"
    val munitCats3 = "1.0.7"
    val munitScalacheck = "0.7.29"
    val sbtDependencyUpdates = "1.2.7"
    val scalaFmt = "2.4.6"
    val scalaFix = "0.10.4"
    val organizeImports = "0.6.0"
    val catsEffect = "3.4.8"
    val fs2 = "3.6.1"
    val log4Cats = "2.5.0"
    val pureConfig = "0.17.2"
    val http4s = "1.0.0-M36"
    val fuuid = "0.8.0-M2"
    val fs2Kafka = "2.2.0"
    val kafkaStreams = "2.8.0"
    val circe = "0.14.5"
    val logbackClassic = "1.4.1"
    val vulcan = "1.9.0"
    val nativePackager = "1.9.16"
  }

  object Compile {
    val scalajsStubs =
      "org.scala-js" %% "scalajs-stubs" % Versions.scalajsStubs % "provided"
    val catsEffect = "org.typelevel" %% "cats-effect" % Versions.catsEffect
    val fs2Core = "co.fs2" %% "fs2-core" % Versions.fs2
    val fs2IO = "co.fs2" %% "fs2-io" % Versions.fs2
    val log4Cats = "org.typelevel" %% "log4cats-slf4j" % Versions.log4Cats
    val pureConfig =
      "com.github.pureconfig" %% "pureconfig-core" % Versions.pureConfig
    val http4sDSL = "org.http4s" %% "http4s-dsl" % Versions.http4s
    val http4sEmberServer =
      "org.http4s" %% "http4s-ember-server" % Versions.http4s
    val kafkaClients =
      "org.apache.kafka" % "kafka-clients" % Versions.kafkaStreams
    val kafkaStreams =
      "org.apache.kafka" % "kafka-streams" % Versions.kafkaStreams
    val kafkaStreamsScala =
      "org.apache.kafka" % "kafka-streams-scala_2.13" % Versions.kafkaStreams
    val logBack = "ch.qos.logback" % "logback-classic" % Versions.logbackClassic

    val fs2Kafka = "com.github.fd4s" %% "fs2-kafka" % Versions.fs2Kafka
    val fs2KafkaVulcan =
      "com.github.fd4s" %% "fs2-kafka-vulcan" % Versions.fs2Kafka

    val http4sDeps = Seq(http4sEmberServer, http4sDSL)
    val kafkaDeps = Seq(
      kafkaStreams,
      kafkaStreamsScala,
      fs2Kafka,
      fs2KafkaVulcan,
      kafkaClients
    )
    val serverDeps = Seq(
      scalajsStubs,
      catsEffect,
      fs2Core,
      fs2IO,
      log4Cats,
      logBack,
      "com.github.fd4s" %% "vulcan" % Versions.vulcan,
      "com.github.fd4s" %% "vulcan-generic" % Versions.vulcan
    ) ++ http4sDeps ++ kafkaDeps
  }

  object SbtPlugins {
    val sbtScalajsCrossProject =
      "org.portable-scala" % "sbt-scalajs-crossproject" % Versions.sbtScalajsCrossProject
    val sbtScalaJs = "org.scala-js" % "sbt-scalajs" % Versions.sbtScalaJs
    val sbtScalajsBundler =
      "ch.epfl.scala" % "sbt-scalajs-bundler" % Versions.sbtScalajsBundler
    val sbtDependencyUpdates =
      "org.jmotor.sbt" % "sbt-dependency-updates" % Versions.sbtDependencyUpdates
    val scalaFmt = "org.scalameta" % "sbt-scalafmt" % Versions.scalaFmt
    val scalaFix = "ch.epfl.scala" % "sbt-scalafix" % Versions.scalaFix
    val organizeImports = "com.github.liancheng" %% "organize-imports" % Versions.organizeImports
    val nativePackager = "com.github.sbt" % "sbt-native-packager" % Versions.nativePackager
  }

  object Npm {
    val react: (String, String) = "react" -> Versions.react
    val reactDom: (String, String) = "react-dom" -> Versions.reactDom
  }

  object Test {
    val munit: ModuleID = "org.scalameta" %% "munit" % Versions.munit
    val munitCats3: ModuleID =
      "org.typelevel" %% "munit-cats-effect-3" % Versions.munitCats3
    val munitScalacheck = "org.scalameta" %% "munit-scalacheck" % Versions.munitScalacheck
  }

}
