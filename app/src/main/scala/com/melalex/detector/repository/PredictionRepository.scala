package com.melalex.detector.repository

import com.melalex.detector.domain.PredictionEntity

trait PredictionRepository[F[_]] {

  def insert(entity: PredictionEntity): F[Unit]

  def findAll(): F[Seq[PredictionEntity]]
}
