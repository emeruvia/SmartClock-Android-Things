package fgcu.smartclock.interfaces

import fgcu.smartclock.models.IPApiModel
import fgcu.smartclock.models.WeatherModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface RetrofitService {
  @GET("/json")
  fun ipApiService(): Call<IPApiModel>

  @GET("/data/2.5/weather?")
  fun weatherService(
    @Query("q") city: String,

    @Query("appid") api: String
  ): Call<WeatherModel>
}
