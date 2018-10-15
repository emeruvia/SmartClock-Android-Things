package fgcu.smartclock.interfaces

import fgcu.smartclock.IPApiModel
import retrofit2.Call
import retrofit2.http.GET

interface IPApiService {
  @GET("json")
  fun ipApiService(): Call<IPApiModel>
}