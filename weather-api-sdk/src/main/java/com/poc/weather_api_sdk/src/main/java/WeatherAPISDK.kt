package com.poc.weather_api_sdk.src.main.java

import android.content.Context
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject

class WeatherAPISDK(private val context: Context) {
    val requestQueue: RequestQueue by lazy {
        Volley.newRequestQueue(context.applicationContext)
    }

    fun getWeather(city: String, apiKey: String, callback: (Weather?) -> Unit) {
        val url = "https://api.openweathermap.org/data/2.5/weather?q=$city&appid=$apiKey"

        val request = JsonObjectRequest(Request.Method.GET, url, null,
            { response ->
                val weather = parseWeatherResponse(response)
                callback(weather)
            },
            {
                callback(null)
            })

        requestQueue.add(request)
    }

    private fun parseWeatherResponse(response: JSONObject): Weather? {
        try {
            val main = response.getJSONObject("main")
            val temperature = main.getDouble("temp")
            val weatherArray = response.getJSONArray("weather")
            val weatherObject = weatherArray.getJSONObject(0)
            val description = weatherObject.getString("description")
            return Weather(temperature, description)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }
}
