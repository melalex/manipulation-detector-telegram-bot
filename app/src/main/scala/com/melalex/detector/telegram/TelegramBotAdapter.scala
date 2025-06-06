package com.melalex.detector.telegram

import cats.data.OptionT
import cats.effect.Async
import cats.syntax.functor._
import com.bot4s.telegram.cats.{ Polling, TelegramBot }
import com.bot4s.telegram.methods._
import com.bot4s.telegram.models._
import sttp.client3.SttpBackend

class TelegramBotAdapter[F[_]: Async](token: String, algebra: TelegramBotAlgebra[F], backend: SttpBackend[F, Any])
    extends TelegramBot[F](token, backend)
    with Polling[F] {

  override def receiveMessage(msg: Message): F[Unit] = {

    def receiveInternal(text: String) =
      algebra.processMessage(text).map {
        case Left(value) =>
          logger.error(s"Error occurred while processing message: $text", value)
          None
        case Right(value) => value
      }

    def createMessage(rsp: String) =
      SendMessage(
        chatId = msg.source,
        text = rsp,
        parseMode = Some(ParseMode.HTML),
        replyToMessageId = Some(msg.messageId)
      )

    val action = for {
      text <- OptionT.fromOption[F](msg.text)
      rsp <- OptionT(receiveInternal(text))
      _ <- OptionT.liftF(request(createMessage(rsp)))
    } yield ()

    action.void.getOrElseF(unit)
  }
}
