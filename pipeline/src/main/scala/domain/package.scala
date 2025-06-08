package com.melalex.detector

package object domain {

  case class ManipulationDetectorRequest(text: String)

  case class ManipulationDetectorResponseEntry(
      entity: String,
      score: Double,
      index: Int,
      word: String,
      start: Int,
      end: Int
  )
}
