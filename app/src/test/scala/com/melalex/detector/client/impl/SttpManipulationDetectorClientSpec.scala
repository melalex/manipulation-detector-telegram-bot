package com.melalex.detector.client.impl

import cats.Id
import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock.{ aResponse, equalTo, post, urlEqualTo }
import com.github.tomakehurst.wiremock.core.WireMockConfiguration
import com.melalex.detector.client.ManipulationDetectorProtocol
import com.melalex.detector.client.ManipulationDetectorProtocol.{
  ManipulationDetectorRequest,
  ManipulationDetectorResponse,
  ManipulationDetectorResponseEntry
}
import com.melalex.detector.config.ManipulationDetectorProviderConfig
import com.melalex.detector.test.ResourceUtil.parseJsonResource
import io.circe.syntax.EncoderOps
import org.scalatest.BeforeAndAfterAll
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import sttp.client3.HttpClientSyncBackend

class SttpManipulationDetectorClientSpec
    extends AnyFlatSpec
    with Matchers
    with BeforeAndAfterAll
    with ManipulationDetectorProtocol {

  private val wireMockServer = new WireMockServer(WireMockConfiguration.wireMockConfig().dynamicPort())

  private val backend = HttpClientSyncBackend()

  override def beforeAll(): Unit = wireMockServer.start()

  override def afterAll(): Unit = wireMockServer.stop()

  "SttpManipulationDetectorClient" should "return a successful response in case of manipulative sentence" in {
    // Arrange
    val client = createClient()

    val request         = parseJsonResource[ManipulationDetectorRequest]("test/manipulative-request.json")
    val responseEntries = parseJsonResource[Seq[ManipulationDetectorResponseEntry]]("test/manipulative-response.json")
    val response        = ManipulationDetectorResponse(responseEntries)

    wireMockServer.stubFor(
      post(urlEqualTo("/ner"))
        .withRequestBody(equalTo(request.asJson.noSpaces))
        .withHeader("Authorization", equalTo("Bearer test-token"))
        .willReturn(
          aResponse()
            .withStatus(200)
            .withHeader("Content-Type", "application/json")
            .withBody(responseEntries.asJson.noSpaces)
        )
    )

    // Act
    val result = client.detectManipulation(request)

    // Assert
    result shouldBe Right(response)
  }

  it should "return a successful response in case of non manipulative sentence" in {
    // Arrange
    val client = createClient()

    val request = parseJsonResource[ManipulationDetectorRequest]("test/non-manipulative-request.json")
    val responseEntries =
      parseJsonResource[Seq[ManipulationDetectorResponseEntry]]("test/non-manipulative-response.json")
    val response = ManipulationDetectorResponse(responseEntries)

    wireMockServer.stubFor(
      post(urlEqualTo("/ner"))
        .withRequestBody(equalTo(request.asJson.noSpaces))
        .withHeader("Authorization", equalTo("Bearer test-token"))
        .willReturn(
          aResponse()
            .withStatus(200)
            .withHeader("Content-Type", "application/json")
            .withBody(responseEntries.asJson.noSpaces)
        )
    )

    // Act
    val result = client.detectManipulation(request)

    // Assert
    result shouldBe Right(response)
  }

  private def createClient() = {
    val config = ManipulationDetectorProviderConfig(
      baseUrl = s"http://localhost:${wireMockServer.port()}",
      apiKey = Some("test-token")
    )

    new SttpManipulationDetectorClient[Id](config, backend)
  }
}
