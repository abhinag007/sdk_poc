package com.poc.stocks_data.repository

import com.poc.stocks_data.data_models.HistoricalDataResponse
import com.poc.stocks_data.data_models.StockQuoteResponse
import com.poc.stocks_data.service.YahooFinanceService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class YahooFinanceRepository(private val service: YahooFinanceService) {

    suspend fun getStockQuote(symbols: String): StockQuoteResponse {
        return withContext(Dispatchers.IO) {
            service.getStockQuote(symbols)
        }
    }

    suspend fun getHistoricalData(symbol: String, range: String, interval: String): HistoricalDataResponse {
        return withContext(Dispatchers.IO) {
            service.getHistoricalData(symbol, range, interval)
        }
    }
}

