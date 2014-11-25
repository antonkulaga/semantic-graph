import sbt.Keys._
import sbt._
import bintray.Opts
import bintray.Plugin.bintraySettings
import bintray.Keys._
import com.typesafe.sbt.packager.universal.UniversalKeys

trait Publisher {
  self:sbt.Build with UniversalKeys=>

  protected val bintrayPublishIvyStyle = settingKey[Boolean]("=== !publishMavenStyle") //workaround for sbt-bintray bug

  lazy val publishSettings = Seq(
    repository in bintray := "denigma-releases",

    bintrayOrganization in bintray := Some("denigma"),

    licenses +=("MPL-2.0", url("http://opensource.org/licenses/MPL-2.0")),

    bintrayPublishIvyStyle := true
  )

  /**
   * For parts of the project that we will not publish
   */
  lazy val noPublishSettings = Seq(
    publish :=(),
    publishLocal :=(),
    publishArtifact := false
  )
}