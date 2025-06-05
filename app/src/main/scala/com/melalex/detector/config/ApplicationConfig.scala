package com.melalex.detector.config

case class ApplicationConfig(
    telegram: TelegramConfig,
    manipulationDetectorProvider: ManipulationDetectorProviderConfig
)

case class TelegramConfig(
    apiKey: String
)

case class ManipulationDetectorProviderConfig(
    baseUrl: String,
    apiKey: Option[String]
)
