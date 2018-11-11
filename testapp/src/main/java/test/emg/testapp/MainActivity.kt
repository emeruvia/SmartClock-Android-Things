package test.emg.testapp

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import com.hanks.htextview.fall.FallTextView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import test.emg.testapp.interfaces.RetrofitService
import test.emg.testapp.models.IPApiModel
import test.emg.testapp.models.WeatherModel
import test.emg.testapp.utils.RetrofitClient
import test.emg.testapp.utils.WeatherClient

class MainActivity : AppCompatActivity() {

  private lateinit var fallTextView: FallTextView
  private lateinit var handler : Handler

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    fallTextView = findViewById(R.id.display_tv)

  }

  fun updateButton(view: View) {
    fetchIpData()
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
    val service = WeatherClient().createWeatherClient()
    val call: Call<WeatherModel> =
      service!!.weatherService(city, resources.getString(R.string.open_weather_api))
    call.enqueue(object : Callback<WeatherModel> {
      override fun onFailure(
        call: Call<WeatherModel>,
        t: Throwable
      ) {
        call.enqueue(this)
        println(t.message)
      }

      override fun onResponse(
        call: Call<WeatherModel>,
        response: Response<WeatherModel>
      ) {
        println("Works")
        println(response.message())
        println(response.raw())
        println(response.body()!!.city)
        println(response.body()!!.date)
        println(response.body()!!.temperature.currentTemperature)
        println(response.body()!!.weatherCondition.get(0).weatherName)
        println(response.body()!!.weatherCondition.get(0).weatherDescription)
      }

    })
  }
}
