package com.poc.stocks_data

import com.poc.stocks_data.repository.YahooFinanceRepository
import com.poc.stocks_data.service.YahooFinanceService
import com.squareup.moshi.Moshi
import kotlinx.coroutines.runBlocking
import mockwebserver3.MockResponse
import mockwebserver3.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class YahooFinanceSDKTest {

    private lateinit var mockWebServer: MockWebServer
    private lateinit var service: YahooFinanceService
    private lateinit var repository: YahooFinanceRepository

    @Before
    fun setUp() {
        mockWebServer = MockWebServer()
        mockWebServer.start()  // This will bind to an available ephemeral port above 1024
        val moshi = Moshi.Builder().build()
        val retrofit = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
        service = retrofit.create(YahooFinanceService::class.java)
        repository = YahooFinanceRepository(service)
    }


    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun testGetStockQuote() = runBlocking {
        val mockResponse = MockResponse()
            .setResponseCode(200)
            .setBody("""
                {
                    "quoteResponse": {
                        "result": [
                            {
                                "symbol": "AAPL",
                                "regularMarketPrice": 150.0,
                                "regularMarketTime": 1629943200
                            }
                        ]
                    }
                }
            """.trimIndent())
        mockWebServer.enqueue(mockResponse)

        val stockQuoteResponse = repository.getStockQuote("AAPL")
        val stockQuote = stockQuoteResponse.quoteResponse.result[0]

        assertEquals("AAPL", stockQuote.symbol)
        assertEquals(150.0, stockQuote.marketPrice, 0.0)
        assertEquals(1629943200, stockQuote.marketTime)
    }

    @Test
    fun testGetHistoricalData() = runBlocking {
        val mockResponse = MockResponse()
            .setResponseCode(200)
            .setBody("""
                {
                    "chart": {
                        "result": [
                            {
                                "timestamp": [1628812800, 1628899200],
                                "indicators": {
                                    "quote": [
                                        {
                                            "close": [146.36, 145.86]
                                        }
                                    ]
                                }
                            }
                        ]
                    }
                }
            """.trimIndent())
        mockWebServer.enqueue(mockResponse)

        val historicalDataResponse = repository.getHistoricalData("AAPL", "1mo", "1d")
        val result = historicalDataResponse.chart.result[0]
        val timestamps = result.timestamp
        val closePrices = result.indicators.quote[0].close

        assertEquals(listOf(1628812800, 1628899200), timestamps)
        assertEquals(listOf(146.36, 145.86), closePrices)
    }
}
