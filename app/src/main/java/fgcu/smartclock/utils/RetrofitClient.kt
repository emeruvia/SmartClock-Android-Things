package fgcu.smartclock.utils

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Default retrofit client. When initialized, URL needs to be passed as a parameter
 */
class RetrofitClient {
  private var retrofit: Retrofit? = null

  fun buildClient(baseUrl: String): Retrofit {
    if (retrofit == null) {
      retrofit = Retrofit.Builder()
          .baseUrl(baseUrl)
          .addConverterFactory(GsonConverterFactory.create())
          .build()
    }
    return retrofit!!
  }
}