package test.emg.testapp.interfaces

import test.emg.testapp.models.WeatherInfoModel
import retrofit2.Call
import retrofit2.http.GET
import test.emg.testapp.models.IPApiModel

interface RetrofitService {
  @GET("json")
  fun ipApiService(): Call<IPApiModel>

  @GET("temp")
  fun weatherService(): Call<WeatherInfoModel>
}