package com.melalex.detector

import cats.effect.Async
import com.melalex.detector.client.impl.SttpManipulationDetectorClient
import com.melalex.detector.config.ApplicationConfig
import com.melalex.detector.service.impl.ManipulationDetectionServiceImpl
import com.melalex.detector.telegram.TelegramBotAdapter
import org.asynchttpclient.Dsl.asyncHttpClient
import sttp.client3.asynchttpclient.cats.AsyncHttpClientCatsBackend

class Module[F[_]: Async](config: ApplicationConfig) {

  private val backend = AsyncHttpClientCatsBackend.usingClient[F](asyncHttpClient())

  private val manipulationDetectorClient =
    new SttpManipulationDetectorClient[F](config.manipulationDetectorProvider, backend)

  private val manipulationDetectionService = new ManipulationDetectionServiceImpl[F](manipulationDetectorClient)

  val telegramBot =
    new TelegramBotAdapter[F](config.telegram.apiKey, manipulationDetectionService.detectManipulation, backend)
}
