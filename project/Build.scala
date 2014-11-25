import com.typesafe.sbt.packager.universal.UniversalKeys
import com.typesafe.sbteclipse.core.EclipsePlugin.EclipseKeys
import play.Play._
import sbt.Keys._
import sbt._

import bintray.Opts
import bintray.Plugin.bintraySettings
import bintray.Keys._

import scala.scalajs.sbtplugin.ScalaJSPlugin.ScalaJSKeys._
import scala.scalajs.sbtplugin.ScalaJSPlugin._
import scala.scalajs.sbtplugin.env.phantomjs.PhantomJSEnv

object Build extends sbt.Build with UniversalKeys with Publisher{

  val scalajsOutputDir = Def.settingKey[File]("directory for javascript files output by scalajs")


  lazy val preview = Project(
    id = "preview",
    base = file("."),
    settings = defaultSettings ++ previewSettings++ noPublishSettings
  ).enablePlugins(play.PlayScala)
    .aggregate(ui)

  lazy val ui = Project(
    id = "ui",
    base = file("ui"),
    settings = defaultSettings ++ scalajsSettings++ noPublishSettings
  ) settings(
    libraryDependencies ++= Dependencies.ui.value,
    persistLauncher := true,
    persistLauncher in Test := false
    ) dependsOn graphs


  lazy val graphs = Project(
    id = "semantic-graphs",
    base = file("graphs"),
    settings = defaultSettings ++ scalajsSettings ++publishSettings
  )  settings( libraryDependencies ++= Dependencies.graphs.value)

  lazy val defaultSettings: Seq[Setting[_]] = bintraySettings ++Seq(
    organization := "org.denigma",
    version := "0.1",
    scalaVersion := "2.11.4",
    scalacOptions ++= Seq("-deprecation", "-unchecked"),
    publishMavenStyle := false,
    resolvers += Opts.resolver.repo("scalax", "scalax-releases"),
    resolvers += Opts.resolver.repo("denigma", "denigma-releases"),
    resolvers += Opts.resolver.repo("alexander-myltsev", "maven"),
    resolvers += Resolver.url("scala-js-releases",
      url("http://dl.bintray.com/content/scala-js/scala-js-releases"))(
        Resolver.ivyStylePatterns)

  )

  lazy val scalajsSettings =
    scalaJSSettings ++ Seq(
      relativeSourceMaps := true,
      ScalaJSKeys.preLinkJSEnv := new PhantomJSEnv,
      ScalaJSKeys.postLinkJSEnv := new PhantomJSEnv
    )

  lazy val previewSettings = Seq(

    ScalaJSKeys.relativeSourceMaps := true, //just in case as sourcemaps do not seem to work=(

    parallelExecution in Test := false,

    scalajsOutputDir := (classDirectory in Compile).value / "public" / "javascripts",

    compile in Compile <<= (compile in Compile) dependsOn (fastOptJS in (ui, Compile)) dependsOn copySourceMapsTask,

    dist <<= dist dependsOn (fullOptJS in (ui, Compile)),

    stage <<= stage dependsOn (fullOptJS in (ui, Compile)),

    libraryDependencies ++= Dependencies.preview.value,

    EclipseKeys.skipParents in ThisBuild := false,

    commands += preStartCommand

  ) ++ (   Seq(packageLauncher, fastOptJS, fullOptJS) map { packageJSKey =>    crossTarget in (ui, Compile, packageJSKey) := scalajsOutputDir.value   }     )


  val copySourceMapsTask = Def.task {
    val scalaFiles = (Seq(graphs.base, ui.base) ** "*.scala").get
    for (scalaFile <- scalaFiles) {
      val target = new File((classDirectory in Compile).value, scalaFile.getPath)
      IO.copyFile(scalaFile, target)
    }
  }

  // Use reflection to rename the 'start' command to 'play-start'
  Option(play.Play.playStartCommand.getClass.getDeclaredField("name")) map { field =>
    field.setAccessible(true)
    field.set(playStartCommand, "play-start")
  }


  // The new 'start' command optimises the JS before calling the Play 'start' renamed 'play-start'
  lazy val preStartCommand = Command.args("start", "<port>") { (state: State, args: Seq[String]) =>
    Project.runTask(fullOptJS in (ui, Compile), state)
    state.copy(remainingCommands = ("play-start " + args.mkString(" ")) +: state.remainingCommands)
  }

}


trait Sharer {

  self:sbt.Build=>

  val sharedSrcDir = "models"


  def defaultSettings: Seq[Setting[_]]

  def scalajsSettings: Seq[Setting[_]]

  lazy val graphModelsJS = Project(
    id = "graph_models_js",
    base = file("models/js"),
    settings = defaultSettings++sharedScalaSettings
  ) settings(name := "graph-models")

  lazy val graphModelsJVM = Project(
    id = "graph_models_jvm",
    base = file(sharedSrcDir),
    settings = defaultSettings++sharedScalaSettings++scalajsSettings
  ) settings(name := "graph-models")

  lazy val sharedScalaSettings =
    Seq(
      name := "graph-models",
      libraryDependencies ++= Dependencies.shared.value
    )


  lazy val sharedDirectorySettings = Seq(
    unmanagedSourceDirectories in Compile += new File((file(".") / sharedSrcDir / "src" / "main" / "scala").getCanonicalPath),
    unmanagedSourceDirectories in Test += new File((file(".") / sharedSrcDir / "src" / "test" / "scala").getCanonicalPath),
    unmanagedResourceDirectories in Compile += file(".") / sharedSrcDir / "src" / "main" / "resources",
    unmanagedResourceDirectories in Test += file(".") / sharedSrcDir / "src" / "test" / "resources"
  )

}