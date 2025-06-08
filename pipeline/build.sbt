ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.16"

lazy val root = (project in file("."))
  .settings(
    name := "manipulation-detector-telegram-pipeline",
    idePackagePrefix := Some("com.melalex.detector")
  )

libraryDependencies ++= Seq(
  "org.apache.spark"              %% "spark-core"            % "4.0.0",
  "org.apache.spark"              %% "spark-sql"             % "4.0.0",
  "org.apache.spark"              %% "spark-streaming"       % "4.0.0",
  "org.apache.spark"              %% "spark-sql-kafka-0-10"  % "4.0.0",
  "org.mongodb.spark"             %% "mongo-spark-connector" % "10.5.0",
  "com.softwaremill.sttp.client3" %% "core"                  % "3.11.0",
  "com.softwaremill.sttp.client3" %% "circe"                 % "3.11.0",
  "io.circe"                      %% "circe-generic"         % "0.14.13",
  "io.circe"                      %% "circe-parser"          % "0.14.13",
  "com.github.pureconfig"         %% "pureconfig"            % "0.17.9",
  "com.typesafe.scala-logging"    %% "scala-logging"         % "3.9.5",
  "org.slf4j"                     % "slf4j-api"              % "2.0.17",
  "ch.qos.logback"                % "logback-classic"        % "1.5.18",
)
