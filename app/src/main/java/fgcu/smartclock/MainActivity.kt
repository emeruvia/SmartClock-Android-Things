package fgcu.smartclock

import android.app.Activity
import android.os.Bundle
import fgcu.smartclock.interfaces.IPApiService
import fgcu.smartclock.utils.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import android.widget.Toast
import retrofit2.Response

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

  private val ipApiUrl: String = " https://ipapi.co"

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)


    fetchIpData()
  }

  fun fetchIpData() {
//    val retrofit: Retrofit = new Retro
    val retrofit = RetrofitClient().buildClient(ipApiUrl)
    val ipApiService: IPApiService = retrofit.create(IPApiService::class.java)
    val ipApiCall = ipApiService.ipApiClient("69.88.190.12","json")

    ipApiCall.enqueue(object: Callback<IPApi> {
      override fun onFailure(
        call: Call<IPApi>,
        t: Throwable
      ) {
        println("This shit does not work")
      }

      override fun onResponse(
        call: Call<IPApi>,
        response: Response<IPApi>
      ) {
        println("This works")
        var ip: IPApi = response.body()!!
        println(ip)
        println(ip.city)
        println(ip.country)
        println(ip.ip)
        println(ip.latitude)
        println(ip.longitude)
        println(ip.postal)
        println(ip.region)
      }

    })
  }
}
