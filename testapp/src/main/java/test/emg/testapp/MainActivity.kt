package test.emg.testapp

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import test.emg.testapp.interfaces.IPApiService
import test.emg.testapp.utils.RetrofitClient

class MainActivity : AppCompatActivity() {

  private val BASE_URL: String = "http://ip-api.com/"

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)


    fetchIpData()
  }

  fun fetchIpData() {
//    val retrofit: Retrofit = new Retro
    val retrofit = RetrofitClient().buildClient(BASE_URL)
    val service: IPApiService = retrofit.create(IPApiService::class.java)
    val call = service.ipApiService()
    call.enqueue(object : Callback<IpApiModel> {
      override fun onResponse(
        call: Call<IpApiModel>,
        response: Response<IpApiModel>
      ) {
        println("Success")
        println(response.body()!!.country)
        println(response.body()!!.city)
        println(response.body()!!.lon)
        println(response.body()!!.lat)
        println(response.body()!!.zip)
      }

      override fun onFailure(
        call: Call<IpApiModel>,
        t: Throwable
      ) {
        println("failure")
      }

    });
  }
}
