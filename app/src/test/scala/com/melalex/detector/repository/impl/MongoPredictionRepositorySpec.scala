package com.melalex.detector.repository.impl

import cats.effect.IO
import cats.effect.unsafe.implicits.global
import com.melalex.detector.config.MongoConfig
import com.melalex.detector.domain.PredictionEntity
import mongo4cats.client.MongoClient
import mongo4cats.embedded.EmbeddedMongo
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class MongoPredictionRepositorySpec extends AnyFlatSpec with Matchers with EmbeddedMongo {

  override val mongoPort: Int = 27017

  "PredictionRepositoryMongo" should "insert and retrieve a prediction entity" in withRunningEmbeddedMongo {
    val connectionStr = s"mongodb://localhost:$mongoPort"

    MongoClient
      .fromConnectionString[IO](connectionStr)
      .use { client =>
        val entity = PredictionEntity("Test input", Some("Prediction"))
        val config = MongoConfig(connectionString = connectionStr, database = "test")
        val repo   = new MongoPredictionRepository[IO](client, config)

        for {
          _ <- repo.insert(entity)
          all <- repo.findAll()
        } yield all should contain(entity)
      }
  }.unsafeRunSync()
}
