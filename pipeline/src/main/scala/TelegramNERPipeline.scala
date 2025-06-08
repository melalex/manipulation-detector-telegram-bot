package com.melalex.detector

import config.ApplicationConfig
import domain.{ ManipulationDetectorRequest, ManipulationDetectorResponseEntry }

import com.typesafe.scalalogging.LazyLogging
import io.circe.generic.auto._
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions._
import pureconfig.ConfigSource
import pureconfig.generic.auto._
import sttp.client3._
import sttp.client3.circe._

object TelegramNERPipeline extends App with LazyLogging {

  private val config = ConfigSource.default.at("app").loadOrThrow[ApplicationConfig]

  private val spark = SparkSession
    .builder()
    .appName("Telegram NER Pipeline")
    .getOrCreate()

  private val nerUdf = udf { (text: String) =>
    val config = ConfigSource.default.at("app").loadOrThrow[ApplicationConfig]
    val backend = HttpURLConnectionBackend()
    val request = basicRequest
      .post(uri"${config.manipulationDetectorProvider.baseUrl}/ner")
      .body(ManipulationDetectorRequest(text))
      .response(asJson[Seq[ManipulationDetectorResponseEntry]])

    request.send(backend) match {
      case Response(Right(res), _, _, _, _, _) => res
      case Response(Left(err), _, _, _, _, _) =>
        logger.error("Failed to request manipulation detection provider", err)
        Seq.empty[ManipulationDetectorResponseEntry]
    }
  }

  private val readDF = spark.readStream
    .format("kafka")
    .option("kafka.bootstrap.servers", config.kafka.bootstrapServerConfig)
    .option("subscribe", config.kafka.topic)
    .option("startingOffsets", "latest")
    .load()
    .selectExpr("CAST(value AS STRING)")
    .toDF("value")
    .withColumn("entities", nerUdf(col("value")))

  readDF.writeStream
    .format("mongodb")
    .option("database", config.mongo.database)
    .option("collection", "predictions")
    .option("checkpointLocation", config.spark.checkpointLocation)
    .start()
    .awaitTermination()

  spark.stop()
}
