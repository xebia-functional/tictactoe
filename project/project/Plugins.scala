import sbt._
object SbtPlugins {

  // define versions, The variable name must be camel case by module name
  object Versions {
    val sbtScalajsCrossProject = "0.5.0"
    val sbtScalaJs = "1.13.1"
    val sbtScalajsBundler = "0.21.1"
    val sbtDependencyUpdates = "1.2.7"
    val sbtScalaFmt = "2.4.6"
    val sbtScalaFix = "0.10.4"
    val sbtNativePackager = "1.9.16"
  }

  val sbtScalajsCrossProject = "org.portable-scala" % "sbt-scalajs-crossproject" % Versions.sbtScalajsCrossProject
  val sbtScalaJs = "org.scala-js"                   % "sbt-scalajs"              % Versions.sbtScalaJs
  val sbtScalajsBundler = "ch.epfl.scala"           % "sbt-scalajs-bundler"      % Versions.sbtScalajsBundler
  val sbtDependencyUpdates = "org.jmotor.sbt"       % "sbt-dependency-updates"   % Versions.sbtDependencyUpdates
  val scalaFmt = "org.scalameta"                    % "sbt-scalafmt"             % Versions.sbtScalaFmt
  val scalaFix = "ch.epfl.scala"                    % "sbt-scalafix"             % Versions.sbtScalaFix
  val nativePackager = "com.github.sbt"             % "sbt-native-packager"      % Versions.sbtNativePackager

}
