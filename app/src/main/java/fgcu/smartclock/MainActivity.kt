package fgcu.smartclock

import android.app.Activity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.hanks.htextview.fall.FallTextView
import fgcu.smartclock.interfaces.IPApiService
import fgcu.smartclock.models.IPApiModel
import fgcu.smartclock.utils.RetrofitClient
import kotlinx.android.synthetic.main.activity_main.city_textview
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import java.sql.Time
import java.text.SimpleDateFormat
import java.util.Calendar

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
class MainActivity : Activity(){


  private val BASE_URL: String = " http://ip-api.com"
  private lateinit var hourTextView: FallTextView
  private lateinit var minuteTextview : FallTextView
  private lateinit var meridiemTextview : TextView
  private lateinit var weatherImageView : ImageView
  private lateinit var weatherStatusTextView : TextView
  private lateinit var currentTempTextView : TextView
  private lateinit var weatherRangeTextView : TextView
  private lateinit var dateTextView : TextView
  private lateinit var cityTextView : TextView
  private var timeHandler = Handler()
  private var locationHandler = Handler()
  private var weahterHandler = Handler()
  private var firebaseHandler = Handler()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    actionBar.hide()
    hourTextView = findViewById(R.id.hour_fallTextView)
    minuteTextview = findViewById(R.id.minute_fallTextView)
    meridiemTextview = findViewById(R.id.meridiem_textview)
    weatherImageView = findViewById(R.id.weather_icon_imageview)
    weatherStatusTextView = findViewById(R.id.weather_status_textview)
    currentTempTextView = findViewById(R.id.current_temp_textview)
    weatherRangeTextView = findViewById(R.id.temp_range_textview)
    dateTextView = findViewById(R.id.date_textview)
    cityTextView = findViewById(R.id.city_textview)

    timeHandler.postDelayed(object : Runnable {
      override fun run() {
        var minute = ""
        var seconds = SimpleDateFormat("ss").format(Calendar.getInstance().time)
        if(seconds == "00"){
          minute = SimpleDateFormat("mm").format(Calendar.getInstance().time)
          minuteTextview.animateText(minute)}
        if(minute == ("00"))
          hourTextView.animateText(SimpleDateFormat("HH").format(Calendar.getInstance().time))
      timeHandler.postDelayed(this,1000)
    }
    },1000)


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
      }

    })
  }
}
