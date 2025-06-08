package com.melalex.detector

import config.ApplicationConfig
import telegram.ScrapperTelegramBot

import cats.effect.{ ExitCode, IO, IOApp, Resource }
import org.apache.kafka.clients.producer.{ KafkaProducer, ProducerConfig }
import org.asynchttpclient.Dsl.asyncHttpClient
import pureconfig.ConfigSource
import pureconfig.generic.auto._
import sttp.client3.asynchttpclient.cats.AsyncHttpClientCatsBackend

import java.util.Properties

object Main extends IOApp {

  def run(args: List[String]): IO[ExitCode] = {
    val config  = ConfigSource.default.at("app").loadOrThrow[ApplicationConfig]
    val backend = AsyncHttpClientCatsBackend.usingClient[IO](asyncHttpClient())

    val props = new Properties()
    props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, config.kafka.bootstrapServerConfig)
    props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.IntegerSerializer")
    props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer")

    Resource.fromAutoCloseable(IO(new KafkaProducer[Int, String](props))).use { producer =>
      val bot = new ScrapperTelegramBot[IO](config.telegram.apiKey, config.kafka.topic, producer, backend)

      bot.startPolling().map(_ => ExitCode.Success)
    }
  }
}
