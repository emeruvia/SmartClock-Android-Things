package fgcu.smartclock.interfaces

import fgcu.smartclock.IPApi
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface IPApiService {
  @GET("/{ip}/{format}/")
  fun ipApiClient(
    @Path("ip") ip: String,
    @Path("format") format: String
  ): Call<IPApi>
}