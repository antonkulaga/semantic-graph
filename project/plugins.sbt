resolvers += "Typesafe repository" at "http://repo.typesafe.com/typesafe/releases/"

//bintray plugin
addSbtPlugin("me.lessis" % "bintray-sbt" % "0.1.2")

// The Play plugin
addSbtPlugin("com.typesafe.play" % "sbt-plugin" % "2.3.6")

addSbtPlugin("com.typesafe.sbt" % "sbt-gzip" % "1.0.0")

//scalajs plugin
addSbtPlugin("org.scala-lang.modules.scalajs" % "scalajs-sbt-plugin" % "0.5.6")

addSbtPlugin("com.typesafe.sbteclipse" % "sbteclipse-plugin" % "2.4.0")
