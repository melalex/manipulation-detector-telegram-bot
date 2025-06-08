package com.melalex.detector
package config

case class ApplicationConfig(
    kafka: KafkaConfig,
    manipulationDetectorProvider: ManipulationDetectorProviderConfig,
    mongo: MongoConfig,
    spark: SparkConfig,
)

case class KafkaConfig(
    topic: String,
    bootstrapServerConfig: String
)

case class ManipulationDetectorProviderConfig(
    baseUrl: String,
    apiKey: Option[String]
)

case class MongoConfig(
    database: String
)

case class SparkConfig(
    checkpointLocation: String
)
