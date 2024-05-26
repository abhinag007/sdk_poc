package com.poc.weather_api_sdk

import android.content.Context
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.poc.weather_api_sdk.src.main.java.WeatherAPISDK
import org.json.JSONArray
import org.json.JSONObject
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class WeatherAPISDKTest {

    @Mock
    private lateinit var context: Context

    @Mock
    private lateinit var requestQueue: RequestQueue

    private lateinit var weatherAPISDK: WeatherAPISDK

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        weatherAPISDK = WeatherAPISDK(context)
        weatherAPISDK.requestQueue = requestQueue
    }

    @Test
    fun `test getWeather with successful response`() {
        val city = "London"
        val apiKey = "test_api_key"
        val responseJson = JSONObject().apply {
            put("main", JSONObject().apply {
                put("temp", 20.5)
            })
            put("weather", JSONArray().apply {
                put(JSONObject().apply {
                    put("description", "Cloudy")
                })
            })
        }

        val successResponse = JsonObjectRequest("", null, null, null)
        `when`(requestQueue.add(successResponse)).thenAnswer {
            val listener = it.arguments[2] as Response.Listener<JSONObject>
            listener.onResponse(responseJson)
            null
        }

        weatherAPISDK.getWeather(city, apiKey) { weather ->
            assertEquals(20.5, weather?.temperature)
            assertEquals("Cloudy", weather?.description)
        }
    }

    @Test
    fun `test getWeather with error response`() {
        val city = "InvalidCity"
        val apiKey = "test_api_key"

        val errorResponse = JsonObjectRequest("", null, null, null)
        `when`(requestQueue.add(errorResponse)).thenAnswer {
            val errorListener = it.arguments[3] as Response.ErrorListener
            errorListener.onErrorResponse(VolleyError())
            null
        }

        weatherAPISDK.getWeather(city, apiKey) { weather ->
            assertNull(weather)
        }
    }
}
