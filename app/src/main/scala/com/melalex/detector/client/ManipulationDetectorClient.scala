package com.melalex.detector.client

import com.melalex.detector.client.ManipulationDetectorProtocol.{ManipulationDetectorRequest, ManipulationDetectorResponse}
import com.melalex.detector.error.AppEither

trait ManipulationDetectorClient[F[_]] {

  def detectManipulation(request: ManipulationDetectorRequest): F[AppEither[ManipulationDetectorResponse]]
}
