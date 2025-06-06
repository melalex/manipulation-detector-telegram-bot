package com.melalex.detector.config

case class ApplicationConfig(
    telegram: TelegramConfig,
    manipulationDetectorProvider: ManipulationDetectorProviderConfig,
    mongo: MongoConfig
)

case class TelegramConfig(
    apiKey: String
)

case class ManipulationDetectorProviderConfig(
    baseUrl: String,
    apiKey: Option[String]
)

case class MongoConfig(
    connectionString: String,
    database: String
)
