package test.emg.testapp.interfaces

import retrofit2.Call
import retrofit2.http.GET
import test.emg.testapp.IpApiModel

interface IPApiService {
  @GET("json")
  fun ipApiService(): Call<IpApiModel>
}