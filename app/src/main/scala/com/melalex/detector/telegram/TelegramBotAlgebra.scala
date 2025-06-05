package com.melalex.detector.telegram

import com.melalex.detector.error.AppEither

trait TelegramBotAlgebra[F[_]] {

  def processMessage(text: String): F[AppEither[Option[String]]]
}
