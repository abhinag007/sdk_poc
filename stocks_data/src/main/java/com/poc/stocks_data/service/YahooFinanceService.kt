package com.poc.stocks_data.service

import com.poc.stocks_data.data_models.HistoricalDataResponse
import com.poc.stocks_data.data_models.StockQuoteResponse
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface YahooFinanceService {

    @GET("v7/finance/quote")
    suspend fun getStockQuote(
        @Query("symbols") symbols: String
    ): StockQuoteResponse

    @GET("v8/finance/chart/{symbol}")
    suspend fun getHistoricalData(
        @Query("symbol") symbol: String,
        @Query("range") range: String, // e.g., "1mo", "6mo", "1y"
        @Query("interval") interval: String // e.g., "1d", "1wk", "1mo"
    ): HistoricalDataResponse

    companion object {
        private const val BASE_URL = "https://query1.finance.yahoo.com/"

        fun create(): YahooFinanceService {
            val moshi = Moshi.Builder()
                .add(KotlinJsonAdapterFactory()) // If you're using Kotlin data classes
                .build()

            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .build()
            return retrofit.create(YahooFinanceService::class.java)
        }
    }
}

