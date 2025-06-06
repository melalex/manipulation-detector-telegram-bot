package com.melalex.detector.service.impl

import cats.Monad
import cats.data.EitherT
import cats.implicits._
import com.melalex.detector.client.ManipulationDetectorClient
import com.melalex.detector.client.ManipulationDetectorProtocol.{
  ManipulationDetectorRequest,
  ManipulationDetectorResponse,
  ManipulationDetectorResponseEntry
}
import com.melalex.detector.domain.PredictionEntity
import com.melalex.detector.error.AppEither
import com.melalex.detector.repository.PredictionRepository
import com.melalex.detector.service.ManipulationDetectionService

import scala.annotation.tailrec

class ManipulationDetectionServiceImpl[F[_]: Monad](client: ManipulationDetectorClient[F],
                                                    predictionRepository: PredictionRepository[F])
    extends ManipulationDetectionService[F] {

  override def detectManipulation(text: String): F[AppEither[Option[String]]] =
    EitherT(client.detectManipulation(ManipulationDetectorRequest(text)))
      .map(compileResult(text))
      .semiflatMap(savePrediction(text))
      .value

  private def savePrediction(text: String)(prediction: Option[String]): F[Option[String]] =
    for {
      _ <- predictionRepository.insert(PredictionEntity(text, prediction))
    } yield prediction

  private def compileResult(text: String)(ner: ManipulationDetectorResponse): Option[String] =
    if (ner.entries.isEmpty) None
    else Some(highlightManipulationEntities(text, ner.entries))

  private def highlightManipulationEntities(text: String, entities: Seq[ManipulationDetectorResponseEntry]): String = {

    @tailrec
    def iter(i: Int, entities: List[(Int, Int)], acc: List[String]): List[String] =
      entities match {
        case Nil => text.substring(i) :: acc
        case head :: tail =>
          val (start, end) = head
          iter(
            end,
            tail,
            "</u>" :: text.substring(start, end) :: "<u>" :: text.substring(i, start) :: acc
          )
      }

    val sortedEntities = entities
      .sortBy(_.start)

    val mergedSpans = sortedEntities
      .foldLeft(List.empty[(Int, Int)]) {
        case (Nil, e) => List((e.start, e.end))
        case ((lastStart, lastEnd) :: rest, e) if e.start <= lastEnd =>
          (lastStart, math.max(lastEnd, e.end)) :: rest
        case (acc, e) =>
          (e.start, e.end) :: acc
      }
      .reverse

    iter(0, mergedSpans, Nil).reverse.mkString
  }
}
