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
import com.squareup.picasso.Picasso
import fgcu.smartclock.interfaces.RetrofitService
import fgcu.smartclock.models.IPApiModel
import fgcu.smartclock.models.WeatherModel
import fgcu.smartclock.utils.RetrofitClient
import fgcu.smartclock.utils.WeatherClient
import kotlinx.android.synthetic.main.activity_main.city_textview
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import java.sql.Time
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.GregorianCalendar
import java.util.Locale
import java.util.TimeZone

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
  private lateinit var hourTextView: FallTextView
  private lateinit var minuteTextview: FallTextView
  private lateinit var meridiemTextview: TextView
  private lateinit var weatherImageView: ImageView
  private lateinit var weatherStatusTextView: TextView
  private lateinit var currentTempTextView: TextView
  private lateinit var weatherRangeTextView: TextView
  private lateinit var dateTextView: TextView
  private lateinit var cityTextView: TextView
  private var timeHandler = Handler()
  private var weatherHandler = Handler()
  private var firebaseHandler = Handler()
  private lateinit var time: Date
  private var hour = 0
  private var hourGMT = 0

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

    manageTime()
    fetchIpData()

  }

  fun manageTime() {
    time = Calendar.getInstance()
        .time
    hourGMT = SimpleDateFormat("HH").format(time).toInt()
    hour =  if(hourGMT < 5) hourGMT + 19 else hourGMT - 5
    minuteTextview.animateText(SimpleDateFormat("mm").format(time))
    hourTextView.animateText((if(hour % 12 == 0 ) 12 else hour%12).toString())
    dateTextView.text = SimpleDateFormat("MM-dd-yyyy").format(time)
    meridiemTextview.text = if (hour < 12) "a.m." else "p.m."
    timeHandler.postDelayed(object : Runnable {
      override fun run() {
        updateTime()
        timeHandler.postDelayed(this, 1000)
      }
    }, 1000)

  }

  fun updateTime() {
    time = Calendar.getInstance()
        .time
    var minute = ""
    val seconds = SimpleDateFormat("ss").format(time)
    if (seconds == "00") {
      minute = SimpleDateFormat("mm").format(time)
      minuteTextview.animateText(minute)
    }
    if (minute == ("00")) {
      hour =  if(hourGMT < 5) hourGMT + 19 else hourGMT - 5
      hourTextView.animateText((if(hour % 12 == 0 ) 12 else hour%12).toString())
    }
    meridiemTextview.text = if (hour < 12) "a.m." else "p.m."
    if (hour == 0)
      dateTextView.text = SimpleDateFormat("MM-dd-yyyy").format(time)
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
        Log.d("FetchIP", "Got location from IP city: "+ response.body()!!.city)
        val success = response.body()!!
        cityTextView.text = success.city +", "+ success.regionName
        fetchWeatherData(success.city)
      }

      override fun onFailure(
        call: Call<IPApiModel>,
        t: Throwable
      ) {
        Log.d("FetchIP", "Didn't get location from IP")
        call.enqueue(this)
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
        updateWeather(response)
        weatherHandler.postDelayed(object : Runnable {
          override fun run() {
            updateWeather(response)
            weatherHandler.postDelayed(this, 30000)
          }
        }, 30000)
      }

    })
  }

  fun updateWeather(response: Response<WeatherModel>) {
    Picasso.get()
        .load(response.body()!!.weatherCondition.get(0).weatherIcon)
        .into(weatherImageView)
    weatherStatusTextView.text = response.body()!!.weatherCondition.get(0)
        .weatherName

    val currentTemp = (response.body()!!.temperature.currentTemperature - 273.15) * 1.8 + 32
    val highTemp = (response.body()!!.temperature.highTemperature - 273.15) * 1.8 + 32
    val lowTemp = (response.body()!!.temperature.lowTemperature - 273.15) * 1.8 + 32

    currentTempTextView.text = String.format("Temp: %.1f F",currentTemp)
    weatherRangeTextView.text = String.format("Hi: %.1f F\t\tLow: %.1f F",highTemp,lowTemp)
  }
}
