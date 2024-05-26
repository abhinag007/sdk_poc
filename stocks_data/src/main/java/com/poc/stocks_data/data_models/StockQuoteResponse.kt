package com.poc.stocks_data.data_models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class StockQuoteResponse(
    @Json(name = "quoteResponse") val quoteResponse: QuoteResponse
)

@JsonClass(generateAdapter = true)
data class QuoteResponse(
    @Json(name = "result") val result: List<StockQuote>
)

@JsonClass(generateAdapter = true)
data class StockQuote(
    @Json(name = "symbol") val symbol: String,
    @Json(name = "regularMarketPrice") val marketPrice: Double,
    @Json(name = "regularMarketTime") val marketTime: Long
)
