package com.melalex.detector.service.impl

import cats.Id
import com.melalex.detector.client.ManipulationDetectorClient
import com.melalex.detector.client.ManipulationDetectorProtocol.{
  ManipulationDetectorRequest,
  ManipulationDetectorResponse,
  ManipulationDetectorResponseEntry
}
import com.melalex.detector.error.{ AppError, AppException }
import com.melalex.detector.test.ResourceUtil.{ parseJsonResource, readResourceAsString }
import org.mockito.Mockito.when
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import org.scalatestplus.mockito.MockitoSugar

class ManipulationDetectionServiceImplSpec extends AnyFlatSpec with Matchers with MockitoSugar {

  "ManipulationDetectionServiceImpl" should "highlight manipulation entities if any are found" in {
    val expected        = readResourceAsString("test/highlighted-manipulative-text.md")
    val request         = parseJsonResource[ManipulationDetectorRequest]("test/manipulative-request.json")
    val responseEntries = parseJsonResource[Seq[ManipulationDetectorResponseEntry]]("test/manipulative-response.json")
    val response        = ManipulationDetectorResponse(responseEntries)

    val mockClient = mock[ManipulationDetectorClient[Id]]
    val inputText  = request.text

    when(mockClient.detectManipulation(ManipulationDetectorRequest(inputText)))
      .thenReturn(Right(response))

    val service = new ManipulationDetectionServiceImpl[Id](mockClient)

    val result = service.detectManipulation(inputText)

    result shouldBe Right(Some(expected))
  }

  it should "return None when no manipulation entities are found" in {
    val mockClient = mock[ManipulationDetectorClient[Id]]
    val inputText  = "Just a normal sentence."
    val response   = ManipulationDetectorResponse(Seq.empty)

    when(mockClient.detectManipulation(ManipulationDetectorRequest(inputText)))
      .thenReturn(Right(response))

    val service = new ManipulationDetectionServiceImpl[Id](mockClient)

    val result = service.detectManipulation(inputText)

    result shouldBe Right(None)
  }

  it should "propagate error from the client" in {
    val mockClient = mock[ManipulationDetectorClient[Id]]
    val inputText  = "Faulty input."
    val error      = Left(AppException(AppError("0", "API failure")))

    when(mockClient.detectManipulation(ManipulationDetectorRequest(inputText)))
      .thenReturn(error)

    val service = new ManipulationDetectionServiceImpl[Id](mockClient)

    val result = service.detectManipulation(inputText)

    result.isLeft shouldBe true
  }
}
