package com.melalex.detector.telegram

import cats.data.OptionT
import cats.effect.Async
import cats.syntax.functor._
import com.bot4s.telegram.cats.{ Polling, TelegramBot }
import com.bot4s.telegram.methods._
import com.bot4s.telegram.models._
import com.typesafe.scalalogging.LazyLogging
import sttp.client3.SttpBackend

class TelegramBotAdapter[F[_]: Async](token: String, algebra: TelegramBotAlgebra[F], backend: SttpBackend[F, Any])
    extends TelegramBot[F](token, backend)
    with Polling[F]
    with LazyLogging {

  override def receiveMessage(msg: Message): F[Unit] = {
    def receiveInternal(text: String) =
      algebra.processMessage(text).map {
        case Left(value) =>
          logger.error(s"Error occurred while processing message: $text", value)
          None
        case Right(value) => value
      }

    val action = for {
      text <- OptionT.fromOption[F](msg.text)
      rsp <- OptionT(receiveInternal(text))
      _ <- OptionT.liftF(request(SendMessage(msg.source, rsp, Some(ParseMode.Markdown))))
    } yield ()

    action.void.getOrElseF(unit)
  }
}
