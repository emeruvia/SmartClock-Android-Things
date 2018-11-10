package test.emg.testapp.interfaces

import test.emg.testapp.models.WeatherModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import test.emg.testapp.models.IPApiModel

interface RetrofitService {
  @GET("/json")
  fun ipApiService(): Call<IPApiModel>

  @GET("/data/2.5/weather?")
  fun weatherService(
    @Query("q") city: String,

    @Query("appid") api: String
  ): Call<WeatherModel>
}
