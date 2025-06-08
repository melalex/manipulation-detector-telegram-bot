ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.16"

lazy val root = (project in file("."))
  .settings(
    name := "manipulation-detector-telegram-scrapper",
    idePackagePrefix := Some("com.melalex.detector")
  )

libraryDependencies ++= Seq(
  "com.softwaremill.sttp.client3" %% "core"                           % "3.11.0",
  "com.softwaremill.sttp.client3" %% "circe"                          % "3.11.0",
  "com.softwaremill.sttp.client3" %% "async-http-client-backend-cats" % "3.11.0",
  "io.circe"                      %% "circe-generic"                  % "0.14.13",
  "io.circe"                      %% "circe-parser"                   % "0.14.13",
  "com.github.pureconfig"         %% "pureconfig"                     % "0.17.9",
  "com.typesafe.scala-logging"    %% "scala-logging"                  % "3.9.5",
  "org.slf4j"                     % "slf4j-api"                       % "2.0.17",
  "ch.qos.logback"                % "logback-classic"                 % "1.5.18",
  "com.bot4s"                     %% "telegram-core"                  % "5.8.4",
  "org.typelevel"                 %% "cats-core"                      % "2.13.0",
  "org.typelevel"                 %% "cats-effect"                    % "3.6.1",
  "org.apache.kafka"              % "kafka-clients"                   % "4.0.0"
)

assembly / mainClass := Some("com.melalex.detector.Main")

assembly / assemblyMergeStrategy := {
  case PathList("module-info.class") => MergeStrategy.discard

  case PathList("META-INF", xs @ _*) =>
    xs match {
      case "io.netty.versions.properties" :: Nil => MergeStrategy.first
      case _                                     => MergeStrategy.discard
    }

  // Default strategies
  case x =>
    val oldStrategy = (assembly / assemblyMergeStrategy).value
    oldStrategy(x)
}
