import sbt._

object Dependencies {

  object Versions {
    val cats        = "2.13.0"
    val catsEffect  = "3.6.1"
    val pureConfig  = "0.17.9"
    val telegram    = "5.8.4"
    val sttp        = "3.11.0"
    val circe       = "0.14.13"
    val logback     = "1.5.18"
    val logging     = "3.9.5"
    val slf4j       = "2.0.17"
    val mongoCats   = "0.7.13"
    val mongoDriver = "5.5.0"

    val scalaCheck     = "1.18.1"
    val scalaTest      = "3.2.19"
    val catsScalaCheck = "0.3.2"
    val wiremock       = "3.13.0"
    val mockito        = "3.2.10.0"
  }

  object Libraries {

    def circe(artifact: String): ModuleID = "io.circe" %% artifact % Versions.circe

    def sttp(artifact: String): ModuleID = "com.softwaremill.sttp.client3" %% artifact % Versions.sttp

    lazy val cats           = "org.typelevel"              %% "cats-core"          % Versions.cats
    lazy val catsEffect     = "org.typelevel"              %% "cats-effect"        % Versions.catsEffect
    lazy val pureConfig     = "com.github.pureconfig"      %% "pureconfig"         % Versions.pureConfig
    lazy val telegram       = "com.bot4s"                  %% "telegram-core"      % Versions.telegram
    lazy val logging        = "com.typesafe.scala-logging" %% "scala-logging"      % Versions.logging
    lazy val slf4j          = "org.slf4j"                  % "slf4j-api"           % Versions.slf4j
    lazy val logback        = "ch.qos.logback"             % "logback-classic"     % Versions.logback
    lazy val mongoCatsCore  = "io.github.kirill5k"         %% "mongo4cats-core"    % Versions.mongoCats
    lazy val mongoCatsCirce = "io.github.kirill5k"         %% "mongo4cats-circe"   % Versions.mongoCats
    lazy val mongoDriver    = "org.mongodb.scala"          %% "mongo-scala-driver" % Versions.mongoDriver

    lazy val asyncHttpClient      = sttp("async-http-client-backend-cats")
    lazy val asyncHttpClientCirce = sttp("circe")

    lazy val circeCore    = circe("circe-core")
    lazy val circeGeneric = circe("circe-generic")
    lazy val circeParser  = circe("circe-parser")

    // Test
    lazy val mongoCatsEmbedded = "io.github.kirill5k" %% "mongo4cats-embedded" % Versions.mongoCats
    lazy val wiremock          = "org.wiremock"       % "wiremock"             % Versions.wiremock
    lazy val scalaTest         = "org.scalatest"      %% "scalatest"           % Versions.scalaTest
    lazy val scalaCheck        = "org.scalacheck"     %% "scalacheck"          % Versions.scalaCheck
    lazy val catsScalaCheck    = "io.chrisdavenport"  %% "cats-scalacheck"     % Versions.catsScalaCheck
    lazy val mockito           = "org.scalatestplus"  %% "mockito-3-4"         % Versions.mockito
  }
}
