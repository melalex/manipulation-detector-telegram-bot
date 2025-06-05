package com.melalex.detector.test

import io.circe.{ parser, Decoder }
import org.scalatest.Assertions.fail

import scala.io.Source

object ResourceUtil {

  def readResourceAsString(resource: String): String = {
    val source = Source.fromResource(resource)

    source.getLines().mkString("\n")
  }

  def parseJsonResource[T: Decoder](resource: String): T = {
    val json = readResourceAsString(resource)

    parser.parse(json) match {
      case Left(value) => fail(s"Cannot parse json: [ ${value.message} ]. Json was $value")
      case Right(value) =>
        value.as[T] match {
          case Left(value)  => fail(s"Cannot parse json: $value")
          case Right(value) => value
        }
    }
  }
}
