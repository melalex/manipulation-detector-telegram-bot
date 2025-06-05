package com.melalex.detector.client.impl

import cats.Functor
import cats.data.EitherT
import cats.implicits._
import com.melalex.detector.client.ManipulationDetectorProtocol.{
  ManipulationDetectorRequest,
  ManipulationDetectorResponse,
  ManipulationDetectorResponseEntry
}
import com.melalex.detector.client.{ ManipulationDetectorClient, ManipulationDetectorProtocol }
import com.melalex.detector.config.ManipulationDetectorProviderConfig
import com.melalex.detector.error.{ AppEither, AppError, AppException }
import sttp.client3.{ SttpBackend, _ }
import sttp.client3.circe._
import sttp.model._

class SttpManipulationDetectorClient[F[_]: Functor](config: ManipulationDetectorProviderConfig,
                                                    backend: SttpBackend[F, Any])
    extends ManipulationDetectorClient[F]
    with ManipulationDetectorProtocol {

  override def detectManipulation(request: ManipulationDetectorRequest): F[AppEither[ManipulationDetectorResponse]] = {
    val response = basicRequest
      .post(uri"${config.baseUrl}/ner")
      .body(request)
      .contentType(MediaType.ApplicationJson)
      .response(asJson[Seq[ManipulationDetectorResponseEntry]])
      .header("Authorization", config.apiKey.map(it => s"Bearer $it"))
      .send(backend)

    EitherT(response.map(_.body))
      .map(ManipulationDetectorResponse(_))
      .leftMap(
        it =>
          AppException(
            AppError(
              "manipulation-detection-provider-error",
              "Exception occurred during call to manipulation detection provider"
            ),
            it
        )
      )
      .value
  }

}
