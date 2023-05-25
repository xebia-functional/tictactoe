import sbt._
import Keys._
import org.scalajs.sbtplugin.ScalaJSPlugin
import org.scalajs.sbtplugin.ScalaJSPlugin.autoImport._
import org.portablescala.sbtplatformdeps.PlatformDepsPlugin.autoImport._
object Dependencies {

  // define versions, The variable name must be camel case by module name
  object Versions {
    val catsEffect = "3.4.8"
    val http4s = "1.0.0-M36"
    val munit = "0.7.29"
    val munitCats3 = "1.0.7"
    val munitScalacheck = "0.7.29"
    val organizeImports = "0.6.0"
    val scalajsDom = "2.4.0"
    val tyrian = "0.6.2"
  }

  object Compile {
    // Scala
    val catsEffect = "org.typelevel"             %% "cats-effect"         % Versions.catsEffect
    val http4sEmberServer = "org.http4s"         %% "http4s-ember-server" % Versions.http4s
    val organizeImports = "com.github.liancheng" %% "organize-imports"    % Versions.organizeImports
  }

  object Test {
    val munit = "org.scalameta"           %% "munit"               % Versions.munit           % "test"
    val munitCats3 = "org.typelevel"      %% "munit-cats-effect-3" % Versions.munitCats3      % "test"
    val munitScalacheck = "org.scalameta" %% "munit-scalacheck"    % Versions.munitScalacheck % "test"
  }

  lazy val commonDepsSettings: Seq[Def.Setting[_]] = Def.settings(
    libraryDependencies ++= Seq(Test.munit, Test.munitCats3, Test.munitScalacheck)
  )

  lazy val serverDepsSettings: Seq[Def.Setting[_]] = commonDepsSettings ++ Def.settings(
    libraryDependencies ++= Seq(Compile.catsEffect, Compile.http4sEmberServer)
  )

  lazy val clientDepsSettings: Seq[Def.Setting[_]] = commonDepsSettings ++ Def.settings(
    libraryDependencies ++= Seq("org.scala-js" %%% "scalajs-dom" % Versions.scalajsDom, "io.indigoengine" %%% "tyrian-io" % Versions.tyrian)
  )

}
