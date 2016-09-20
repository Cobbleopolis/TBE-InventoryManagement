val appName: String = "InventoryManagement"

val appVersion: String = "1.0"

val appScalaVersion: String = "2.11.7"

val baseDependencies = Seq(
    jdbc,
    cache,
    ws,
    specs2 % Test
)

val webJars = Seq (
    "org.webjars" %% "webjars-play" % "2.5.0",
    "org.webjars" % "bootstrap-sass" % "3.3.1-1",
    "org.webjars" % "jquery" % "2.2.2",
    "org.webjars.bower" % "font-awesome-sass" % "4.6.2",
    "org.webjars.npm" % "react" % "15.3.2",
    "org.webjars.npm" % "react-dom" % "15.3.2",
    "org.webjars.npm" % "requirejs" % "2.3.2"
)

val otherDependencies = Seq(
    "com.adrianhurt" %% "play-bootstrap" % "1.0-P25-B3" exclude("org.webjars", "jquery"),
    "org.mindrot" % "jbcrypt" % "0.3m"
)

lazy val `inventorymanagement` = (project in file(".")).enablePlugins(PlayScala, DebianPlugin, BuildInfoPlugin).settings(
    name := appName,
    version := appVersion,
    scalaVersion := appScalaVersion,
    libraryDependencies ++= baseDependencies ++ webJars ++ otherDependencies,
    unmanagedResourceDirectories in Test <+=  baseDirectory ( _ /"target/web/public/test" ),
    resolvers += "scalaz-bintray" at "https://dl.bintray.com/scalaz/releases",
    resolveFromWebjarsNodeModulesDir := true,
    typingsFile := Some(baseDirectory.value / "app" / "assets" / "javascripts" / "typings" / "index.d.ts"),
    maintainer in Linux := "Logan Thompson <cobbleopolis@gmail.com>",
    packageSummary in Linux := "Inventory Management server",
    packageDescription := "A play server to run a Inventory Management instance",
    (testOptions in Test) += Tests.Argument(TestFrameworks.ScalaTest, "-h", "target/report"),
    JsEngineKeys.engineType := JsEngineKeys.EngineType.Node
    //    bashScriptExtraDefines += """addJava "-Dconfig.file=${app_home}/../conf/production.conf""""
)