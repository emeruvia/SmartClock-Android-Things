package test.emg.testapp.utils

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import test.emg.testapp.interfaces.RetrofitService

class WeatherClient {
  private var retrofit: Retrofit? = null

  companion object {
    val BASE_URL = "http://api.openweathermap.org"
  }

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