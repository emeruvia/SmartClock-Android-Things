package fgcu.smartclock.utils

import fgcu.smartclock.interfaces.RetrofitService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Weather Retrofit Client, class has the URL already defined, so when initialized it doesn't
 * require a baseURL as parameter
 */
class WeatherClient {
  private var retrofit: Retrofit? = null

  companion object {
    //Base URL for openweather map
    const val BASE_URL = "http://api.openweathermap.org"
  }

  /**
   * Creates a weather client
   */
  fun createWeatherClient(): RetrofitService? {
    if (retrofit == null) {
      retrofit = Retrofit.Builder()
          .baseUrl(BASE_URL)
          .addConverterFactory(GsonConverterFactory.create())
          .build()
    }
    return retrofit!!.create(RetrofitService::class.java)
  }
}