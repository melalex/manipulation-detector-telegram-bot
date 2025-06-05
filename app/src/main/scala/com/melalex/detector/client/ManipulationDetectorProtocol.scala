package com.melalex.detector.client

import com.melalex.detector.client.ManipulationDetectorProtocol.{
  ManipulationDetectorRequest,
  ManipulationDetectorResponse,
  ManipulationDetectorResponseEntry
}
import io.circe._
import io.circe.generic.extras.Configuration
import io.circe.generic.extras.semiauto.{ deriveConfiguredDecoder, deriveConfiguredEncoder }

trait ManipulationDetectorProtocol {

  implicit val configuration: Configuration = Configuration.default

  implicit val manipulationDetectorRequestEncoder: Encoder[ManipulationDetectorRequest] =
    deriveConfiguredEncoder[ManipulationDetectorRequest]

  implicit val manipulationDetectorRequestDecoder: Decoder[ManipulationDetectorRequest] =
    deriveConfiguredDecoder[ManipulationDetectorRequest]

  implicit val manipulationDetectorResponseDecoder: Decoder[ManipulationDetectorResponse] =
    deriveConfiguredDecoder[ManipulationDetectorResponse]

  implicit val manipulationDetectorResponseEntryDecoder: Decoder[ManipulationDetectorResponseEntry] =
    deriveConfiguredDecoder[ManipulationDetectorResponseEntry]

  implicit val manipulationDetectorResponseEntryEncoder: Encoder[ManipulationDetectorResponseEntry] =
    deriveConfiguredEncoder[ManipulationDetectorResponseEntry]
}

object ManipulationDetectorProtocol extends ManipulationDetectorProtocol {

  case class ManipulationDetectorRequest(text: String)

  case class ManipulationDetectorResponse(entries: Seq[ManipulationDetectorResponseEntry])

  case class ManipulationDetectorResponseEntry(
      entity: String,
      score: Double,
      index: Int,
      word: String,
      start: Int,
      end: Int
  )
}
