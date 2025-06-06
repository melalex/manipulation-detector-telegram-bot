package com.melalex.detector

import cats.effect.Async
import com.melalex.detector.client.impl.SttpManipulationDetectorClient
import com.melalex.detector.config.ApplicationConfig
import com.melalex.detector.repository.impl.MongoPredictionRepository
import com.melalex.detector.service.impl.ManipulationDetectionServiceImpl
import com.melalex.detector.telegram.TelegramBotAdapter
import mongo4cats.client.MongoClient
import org.asynchttpclient.Dsl.asyncHttpClient
import sttp.client3.asynchttpclient.cats.AsyncHttpClientCatsBackend

class Module[F[_]: Async](config: ApplicationConfig, mongo: MongoClient[F]) {

  private val backend = AsyncHttpClientCatsBackend.usingClient[F](asyncHttpClient())

  private val manipulationDetectorClient =
    new SttpManipulationDetectorClient[F](config.manipulationDetectorProvider, backend)

  private val predictionRepository = new MongoPredictionRepository[F](mongo, config.mongo)

  private val manipulationDetectionService =
    new ManipulationDetectionServiceImpl[F](manipulationDetectorClient, predictionRepository)

  val telegramBot =
    new TelegramBotAdapter[F](config.telegram.apiKey, manipulationDetectionService.detectManipulation, backend)
}
