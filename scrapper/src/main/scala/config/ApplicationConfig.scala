package com.melalex.detector
package config

case class ApplicationConfig(
    telegram: TelegramConfig,
    kafka: KafkaConfig
)

case class TelegramConfig(
    apiKey: String
)

case class KafkaConfig(
    topic: String,
    bootstrapServerConfig: String
)
