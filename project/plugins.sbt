logLevel := Level.Warn

resolvers += "Typesafe repository" at "http://repo.typesafe.com/typesafe/releases/"

addSbtPlugin("com.typesafe.play" % "sbt-plugin" % "2.5.8")

addSbtPlugin("org.irundaia.sbt" % "sbt-sassify" % "1.4.6")

addSbtPlugin("name.de-vries" % "sbt-typescript" % "0.3.0-beta.4")

addSbtPlugin("com.eed3si9n" % "sbt-buildinfo" % "0.6.1")