package fgcu.smartclock

import android.app.Activity
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import com.hanks.htextview.fall.FallText
import com.hanks.htextview.fall.FallTextView
import fgcu.smartclock.interfaces.IPApiService
import fgcu.smartclock.utils.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import java.util.Timer

/**
 * Skeleton of an Android Things activity.
 *
 * Android Things peripheral APIs are accessible through the class
 * PeripheralManagerService. For example, the snippet below will open a GPIO pin and
 * set it to HIGH:
 *
 * <pre>{@code
 * val service = PeripheralManagerService()
 * val mLedGpio = service.openGpio("BCM6")
 * mLedGpio.setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW)
 * mLedGpio.value = true
 * }</pre>
 * <p>
 * For more complex peripherals, look for an existing user-space driver, or implement one if none
 * is available.
 *
 * @see <a href="https://github.com/androidthings/contrib-drivers#readme">https://github.com/androidthings/contrib-drivers#readme</a>
 *
 */
class MainActivity : Activity() {

  private val BASE_URL: String = " http://ip-api.com"
  private lateinit var fallTextView: FallTextView

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    fallTextView = findViewById(R.id.fallTextView)
    //TODO get rid of method, used for testing purposes
    fetchIpData()
  }

  fun fetchIpData() {
    val retrofit: Retrofit = RetrofitClient().buildClient(BASE_URL)
    val service: IPApiService = retrofit.create(IPApiService::class.java)
    val call: Call<IPApiModel> = service.ipApiService()
    call.enqueue(object: Callback<IPApiModel> {
      override fun onFailure(
        call: Call<IPApiModel>,
        t: Throwable
      ) {
        println("Failure to fetch data for location")
      }

      override fun onResponse(
        call: Call<IPApiModel>,
        response: Response<IPApiModel>
      ) {
        println("Connection Sucessful")
        Toast.makeText(applicationContext, response.body()!!.zip, Toast.LENGTH_LONG).show()
        val model: IPApiModel = response.body()!!
        println(model.country)
        println(model.city)
        println(model.ipAdress)
        println(model.zip)
        fallTextView.animateText("Your city is: " + model.city)
      }

    })
  }
}
