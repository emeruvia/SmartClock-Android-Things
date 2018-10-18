package test.emg.testapp

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.hanks.htextview.fall.FallTextView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import test.emg.testapp.interfaces.RetrofitService
import test.emg.testapp.models.IPApiModel
import test.emg.testapp.models.WeatherInfoModel
import test.emg.testapp.utils.RetrofitClient

class MainActivity : AppCompatActivity() {

  private lateinit var fallTextView: FallTextView

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    fallTextView = findViewById(R.id.display_tv)

  }

  fun updateButton(view: View) {
    fetchWeatherData("miami")
  }

  fun fetchIpData() {
    val retrofit = RetrofitClient().buildClient(resources.getString(R.string.ip_api_base_url))
    val service = retrofit.create(RetrofitService::class.java)
    val call = service!!.ipApiService()
    call.enqueue(object : Callback<IPApiModel> {
      override fun onResponse(
        call: Call<IPApiModel>,
        response: Response<IPApiModel>
      ) {
        val success: IPApiModel = response.body()!!
        fallTextView.animateText("Based on your ip, your citi is: " + success.city);
        println("Success")
        println(response.body()!!.country)
        println(response.body()!!.city)
        println(response.body()!!.lon)
        println(response.body()!!.lat)
        println(response.body()!!.zip)
        fetchWeatherData(success.city)
      }

      override fun onFailure(
        call: Call<IPApiModel>,
        t: Throwable
      ) {
        fallTextView.animateText("failure to get data from api")
        println("failure")
      }

    });
  }

  fun fetchWeatherData(city: String) {
    println("City value being passed" + city)
    val retrofit = RetrofitClient().buildClient(resources.getString(R.string.weather_base_url))
    val service = retrofit.create(RetrofitService::class.java)
    val call: Call<List<WeatherInfoModel>> = service.weatherService(city, resources.getString(R.string.open_weather_api))
    call.enqueue(object: Callback<List<WeatherInfoModel>> {
      override fun onResponse(
        call: Call<List<WeatherInfoModel>>,
        response: Response<List<WeatherInfoModel>>
      ) {
        println("Success")
        println(response.raw())
      }

      override fun onFailure(
        call: Call<List<WeatherInfoModel>>,
        t: Throwable
      ) {
        println(t.toString())
      }

    })
  }
}
