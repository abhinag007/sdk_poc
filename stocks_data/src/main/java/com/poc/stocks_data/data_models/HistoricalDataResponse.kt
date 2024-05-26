package com.poc.stocks_data.data_models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class HistoricalDataResponse(
    @Json(name = "chart") val chart: Chart
)

@JsonClass(generateAdapter = true)
data class Chart(
    @Json(name = "result") val result: List<Result>
)

@JsonClass(generateAdapter = true)
data class Result(
    @Json(name = "timestamp") val timestamp: List<Long>,
    @Json(name = "indicators") val indicators: Indicators
)

@JsonClass(generateAdapter = true)
data class Indicators(
    @Json(name = "quote") val quote: List<Quote>
)

@JsonClass(generateAdapter = true)
data class Quote(
    @Json(name = "close") val close: List<Double>
)
