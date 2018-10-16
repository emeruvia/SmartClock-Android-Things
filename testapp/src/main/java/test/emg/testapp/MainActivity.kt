package test.emg.testapp

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.hanks.htextview.fall.FallTextView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import test.emg.testapp.interfaces.RetrofitService
import test.emg.testapp.models.IPApiModel
import test.emg.testapp.utils.RetrofitClient

class MainActivity : AppCompatActivity() {

  private val BASE_URL: String = "http://ip-api.com/"
  private lateinit var fallTextView: FallTextView

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    fallTextView = findViewById(R.id.display_tv)

  }


  fun updateButton(view: View) {
    fetchIpData()
  }


  fun fetchIpData() {
    val retrofit = RetrofitClient().buildClient(BASE_URL)
    val service: RetrofitService = retrofit.create(RetrofitService::class.java)
    val call = service.ipApiService()
    call.enqueue(object : Callback<IPApiModel> {
      override fun onResponse(
        call: Call<IPApiModel>,
        response: Response<IPApiModel>
      ) {
        var success: IPApiModel = response.body()!!
        fallTextView.animateText("Based on your ip, your citi is: " + success.city);
        println("Success")
        println(response.body()!!.country)
        println(response.body()!!.city)
        println(response.body()!!.lon)
        println(response.body()!!.lat)
        println(response.body()!!.zip)
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
}
