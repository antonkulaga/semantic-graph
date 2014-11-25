import bintray.Opts
import sbt.Keys._
import sbt._

import scala.scalajs.sbtplugin.ScalaJSPlugin._

object Dependencies
{
  val shared = Def.setting(Seq())

  val preview = Def.setting(shared.value ++ Seq(
    "org.webjars" %% "webjars-play" % "2.3.0-2",
    "org.webjars" % "jquery" % "1.11.1",
    "org.webjars" % "three.js" % "r66",
    "org.denigma" %% "binding-play" % "0.6.2",
    "com.assembla.scala-incubator" %% "graph-core" % "1.9.0",
    "org.scalajs" %% "scalajs-pickling-play-json" % "0.3.1"
  ))

  val graphs = Def.setting(shared.value++Seq(
    "org.scala-lang.modules.scalajs" %%% "scalajs-dom" % "0.6",
    "com.scalatags" %%% "scalatags" % "0.4.2",
    "com.scalarx" %%% "scalarx" % "0.2.6",
    "org.scalajs" %%% "threejs" % "0.0.68-0.1.2",
    "org.scalax" %%% "semweb" % "0.6.13"
  ))

  val ui = Def.setting(shared.value :+ "org.denigma" %%% "binding" % "0.6.4")



}