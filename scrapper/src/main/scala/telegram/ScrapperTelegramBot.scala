package com.melalex.detector
package telegram

import cats.data.OptionT
import cats.effect.Async
import cats.syntax.functor._
import com.bot4s.telegram.cats.{ Polling, TelegramBot }
import com.bot4s.telegram.models._
import org.apache.kafka.clients.producer.{ KafkaProducer, ProducerRecord }
import sttp.client3.SttpBackend

class ScrapperTelegramBot[F[_]](token: String,
                                topic: String,
                                producer: KafkaProducer[Int, String],
                                backend: SttpBackend[F, Any])(
    implicit async: Async[F]
) extends TelegramBot[F](token, backend)
    with Polling[F] {

  override def receiveMessage(msg: Message): F[Unit] = {

    def receiveInternal(text: String) = async.blocking {
      producer.send(new ProducerRecord(topic, msg.messageId, text)).get()
    }

    val action = for {
      text <- OptionT.fromOption[F](msg.text)
      _ <- OptionT.liftF(receiveInternal(text))
    } yield ()

    action.void.getOrElseF(unit)
  }
}
