package com.melalex.detector.repository.impl

import cats.Monad
import cats.implicits._
import com.melalex.detector.config.MongoConfig
import com.melalex.detector.domain.PredictionEntity
import com.melalex.detector.repository.PredictionRepository
import io.circe.generic.auto._
import mongo4cats.circe._
import mongo4cats.client.MongoClient
import mongo4cats.collection.MongoCollection

class MongoPredictionRepository[F[_]: Monad](client: MongoClient[F], config: MongoConfig)
    extends PredictionRepository[F] {

  private val collectionName = "predictions"

  private def getCollection: F[MongoCollection[F, PredictionEntity]] =
    for {
      db <- client.getDatabase(config.database)
      col <- db.getCollectionWithCodec[PredictionEntity](collectionName)
    } yield col

  override def insert(entity: PredictionEntity): F[Unit] =
    getCollection.flatMap(_.insertOne(entity).void)

  override def findAll(): F[Seq[PredictionEntity]] =
    getCollection.flatMap(_.find.all.map(_.toSeq))
}
