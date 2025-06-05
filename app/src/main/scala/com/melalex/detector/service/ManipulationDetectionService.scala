package com.melalex.detector.service

import com.melalex.detector.error.AppEither

trait ManipulationDetectionService[F[_]] {

  def detectManipulation(text: String): F[AppEither[Option[String]]]
}
