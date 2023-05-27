import Dependencies.SbtPlugins._
// We have scalajs cross projects that may share code with scalajvm
// projects, so we need the scalajs-crossproject plugin[1].
addSbtPlugin(sbtScalajsCrossProject)
// We have scalajs, so we need the scalajs sbt plugin[1].
addSbtPlugin(sbtScalaJs)
// We may want to use npm dependencies in our scalajs modules, so we
// need the latest sbt-scalajs-bundler for scalajs 1.x[2].
addSbtPlugin(sbtScalajsBundler)
addSbtPlugin(sbtDependencyUpdates)
addSbtPlugin(scalaFmt)
addSbtPlugin(scalaFix)
addSbtPlugin(nativePackager)


// 1: [Example plugins.sbt configuration from portable-scala/sbt-crossproject/Readme.md](https://github.com/portable-scala/sbt-crossproject/blob/31b03a9e01abb11043d672303fa71439f868f868/README.md?plain=1#L24-L29)
// 2: [sbt-scalajs-bundler plugins.sbt setup](https://scalacenter.github.io/scalajs-bundler/)
