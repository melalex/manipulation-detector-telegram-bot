package com.melalex.detector

import cats.effect.{ ExitCode, IO, IOApp }
import com.melalex.detector.config.ApplicationConfig
import pureconfig.ConfigSource
import pureconfig.generic.auto._

object Main extends IOApp {

  def run(args: List[String]): IO[ExitCode] = {
    val config = ConfigSource.default.at("app").loadOrThrow[ApplicationConfig]
    val module = new Module[IO](config)

    module.telegramBot.startPolling().map(_ => ExitCode.Success)
  }
}
