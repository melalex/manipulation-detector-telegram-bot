package com.melalex.detector

import cats.effect.{ ExitCode, IO, IOApp }
import com.melalex.detector.config.ApplicationConfig
import mongo4cats.client.MongoClient
import pureconfig.ConfigSource
import pureconfig.generic.auto._

object Main extends IOApp {

  def run(args: List[String]): IO[ExitCode] = {
    val config = ConfigSource.default.at("app").loadOrThrow[ApplicationConfig]

    MongoClient.fromConnectionString[IO](config.mongo.connectionString).use { client =>
      val module = new Module[IO](config, client)

      module.telegramBot.startPolling().map(_ => ExitCode.Success)
    }
  }
}
