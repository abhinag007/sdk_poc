package com.poc.stocks_data

import com.poc.stocks_data.data_models.HistoricalDataResponse
import com.poc.stocks_data.data_models.StockQuoteResponse
import com.poc.stocks_data.repository.YahooFinanceRepository
import com.poc.stocks_data.service.YahooFinanceService

object YahooFinanceSDK {

    private val service by lazy {
        YahooFinanceService.create()
    }

    private val repository by lazy {
        YahooFinanceRepository(service)
    }

    suspend fun getStockQuote(symbols: String): StockQuoteResponse {
        return repository.getStockQuote(symbols)
    }

    suspend fun getHistoricalData(symbol: String, range: String, interval: String): HistoricalDataResponse {
        return repository.getHistoricalData(symbol, range, interval)
    }
}
