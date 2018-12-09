package fgcu.smartclock

import android.app.Activity
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso
import fgcu.smartclock.R.drawable
import fgcu.smartclock.interfaces.RetrofitService
import fgcu.smartclock.models.IPApiModel
import fgcu.smartclock.models.WeatherModel
import fgcu.smartclock.utils.RetrofitClient
import fgcu.smartclock.utils.WeatherClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

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

  // instance variables for all the textviews and imageviews
  private lateinit var hourTextView: TextView
  private lateinit var weatherStatusTextView: TextView
  private lateinit var currentTempTextView: TextView
  private lateinit var weatherRangeTextView: TextView
  private lateinit var dateTextView: TextView
  private lateinit var cityTextView: TextView
  private lateinit var weatherImageView: ImageView
  private lateinit var backgroundImageView: ImageView

  // instance variable for FireBase storage
  private lateinit var storage: StorageReference

  // Handlers allow to send and process runnable objects with a thread. They are used in order
  // to call the methods every nth time assgined. There are three implementations, time, weater,
  // and firebase handlers
  private var timeHandler = Handler()
  private var weatherHandler = Handler()
  private var firebaseHandler = Handler()

  // Date instance variable
  private lateinit var time: Date
  //instance variables for calculating the time

  /**
   * onCreate method is the one in charge of running the first the app, similar to main. In this
   * method, we set the content view to the activity_main layout.
   * - It hides the actionbar for the application to be full screen.
   * - Initializes all the the views instance variables with their respective ID's from the
   *  activity_main.xml layout
   * - Initializes the storage variable with the FireBase storage instance
   * - Calls methods manageTime()
   * - fetchIpData()
   */
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    actionBar.hide()

    hourTextView = findViewById(R.id.hour_textview)
    weatherImageView = findViewById(R.id.weather_icon_imageview)
    weatherStatusTextView = findViewById(R.id.weather_status_textview)
    currentTempTextView = findViewById(R.id.current_temp_textview)
    weatherRangeTextView = findViewById(R.id.temp_range_textview)
    dateTextView = findViewById(R.id.date_textview)
    cityTextView = findViewById(R.id.city_textview)
    backgroundImageView = findViewById(R.id.clock_background_imageview)

    storage = FirebaseStorage.getInstance()
        .reference

    manageTime()
    fetchIpData()
  }

  /**
   * Method gets the image from FireBase and sets up handler for the background image to be called
   * every 24 hours
   */
  fun getBackground() {
    var baseImageFileName = "image_"
    var imageFileCount = 1
    var fileTypeName = ".jpg"
    storage.child("image_0.jpg")
        .downloadUrl.addOnCompleteListener {
      setImage(it.result)
    }
    firebaseHandler.postDelayed(object : Runnable {
      override fun run() {
        storage.child(baseImageFileName + imageFileCount % 7 + fileTypeName)
            .downloadUrl.addOnCompleteListener {
          setImage(it.result)
          imageFileCount++
        }
        weatherHandler.postDelayed(this, 86400000)
      }
    }, 86400000)
  }

  /**
   * Method is in charge of managing the time. It initializes the time handler and updates it every
   * second.
   */
  fun manageTime() {
    setTime()
    timeHandler.postDelayed(object : Runnable {
      override fun run() {
//        updateTime()
        timeHandler.postDelayed(this, 1000)
      }
    }, 1000)

  }

  /**
   * Method gets the image from the Uri, and sets it to the background image. It resizes the width
   * and height of the image depending on the size of the screen
   * @param Uri string
   */
  fun setImage(result: Uri?) {
    Picasso.get()
        .load(result)
        .centerCrop()
        .resize(backgroundImageView.measuredWidth, backgroundImageView.measuredHeight)
        .into(backgroundImageView)
  }

  /**
   * Gets an instance calendar time and sets the time to every their respective textviews
   */
  fun setTime() {
    time = Calendar.getInstance()
        .time
    val hourGMT = SimpleDateFormat("K:mm aa", Locale.getDefault()).format(time)
    hourTextView.text = (hourGMT)
    dateTextView.text = SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault()).format(time)
  }

  /**
   * Method is in charge of getting the location of the device using the IP-Api.
   */
  fun fetchIpData() {
    // Objects in charge of making the API call, they use the retrofit library in order to get all
    // the data and then make an API call
    val retrofit = RetrofitClient().buildClient(resources.getString(R.string.ip_api_base_url))
    val service = retrofit.create(RetrofitService::class.java)
    val call = service!!.ipApiService()
    call.enqueue(object : Callback<IPApiModel> {

      // Method gets the city the device is located whenever the api call is successful
      override fun onResponse(
        call: Call<IPApiModel>,
        response: Response<IPApiModel>
      ) {
        Log.d("FetchIP", "Got location from IP city: " + response.body()!!.city)
        val success = response.body()!!
//        cityTextView.text = success.city + ", " + success.regionName
        // call fetchWeatherData method with the location of the device
        fetchWeatherData(success.city)
        // call method to get the background image
        getBackground()
      }

      // Method displays a Log debug message if the call fails
      override fun onFailure(
        call: Call<IPApiModel>,
        t: Throwable
      ) {
        Log.d("FetchIP", "Didn't get location from IP")
        println(t.message)
        // Show connection error not found
        Picasso.get()
            .load(R.drawable.unknown)
            .into(weatherImageView)
        weatherStatusTextView.text = "Error connecting to network"
        currentTempTextView.text = ""
        call.enqueue(this)
      }

    });
  }

  /**
   * Method is in charge of making the API calls and initialize the weather handler
   * @param string containing the city to make the API call for
   */
  fun fetchWeatherData(city: String) {
    // sets up the weather client in order to make the api call
    val service = WeatherClient().createWeatherClient()
    // makes the api call in order to fetch the weather data
    val call: Call<WeatherModel> =
      service!!.weatherService(city, resources.getString(R.string.open_weather_api))
    call.enqueue(object : Callback<WeatherModel> {
      override fun onFailure(
        call: Call<WeatherModel>,
        t: Throwable
      ) {
        println(t.message)
        Picasso.get()
            .load(R.drawable.unknown)
            .into(weatherImageView)
        weatherStatusTextView.text = "Error connecting to network"
        // queue api call again
        call.enqueue(this)
      }

      // method is ran whenever the API call was successful.
      override fun onResponse(
        call: Call<WeatherModel>,
        response: Response<WeatherModel>
      ) {
        // Log debug message containing the weather condition body
        Log.d("WeatherConnection", response.body()!!.weatherCondition.toString())
        Log.d("WeatherConnection", response.body()!!.temperature.toString())
        Log.d("WeatherConnection", response.body()!!.city.toString())
        // init update weather call
        updateWeather(response)
        // set up handler to fetch latest data every minute.
        weatherHandler.postDelayed(object : Runnable {
          override fun run() {
            updateWeather(response) // call method to update weather
            //sets delay time for the handler to be re-run
            weatherHandler.postDelayed(this, 60000)
          }
        }, 60000)
      }

    })
  }

  /**
   * Method to update the weather view, it parses through the response body received as a parameter
   * from the API call. It will set the image and text views with the respective data after every
   * call. It calculates the current weather temperature, high and low temperature for the day in
   * Fahrenheit degrees.
   * @param Response<WeatherModel> object containing the weather data fetched from the API call
   */
  fun updateWeather(response: Response<WeatherModel>) {
    val icon =
      response.body()!!.weatherCondition[0]
          .weatherIcon

    // Loads the weather icon into the weather image view using Picasso library
    Picasso.get()
        .load(iconDrawable(icon))
        .into(weatherImageView)
    weatherStatusTextView.text = response.body()!!.weatherCondition[0]
        .weatherDescription

    val currentTemp = (response.body()!!.temperature.currentTemperature - 273.15) * 1.8 + 32
    val highTemp = (response.body()!!.temperature.highTemperature - 273.15) * 1.8 + 32
    val lowTemp = (response.body()!!.temperature.lowTemperature - 273.15) * 1.8 + 32

    currentTempTextView.text = String.format("Temp: %.1f F", currentTemp)
    weatherRangeTextView.text = String.format("Hi: %.1f F\t\tLow: %.1f F", highTemp, lowTemp)
  }

  /**
   * Method maps the values of the weather condition icon grabbed from the weather API
   * @param string of the weather condition icon
   * @return id of the drawable object
   */
  private fun iconDrawable(iconCondition: String): Int {
    when (iconCondition) {
      "01d" -> return drawable.clear_sky
      "01n" -> return drawable.nt_clear_sky
      "02d" -> return drawable.few_clouds
      "02n" -> return drawable.nt_few_clouds
      "03d" -> return drawable.cloudy
      "03n" -> return drawable.cloudy
      "04d" -> return drawable.broken_clouds
      "04n" -> return drawable.nt_broken_clouds
      "09d" -> return drawable.shower_rain
      "09n" -> return drawable.shower_rain
      "10d" -> return drawable.rain
      "10n" -> return drawable.rain
      "11d" -> return drawable.thunderstorm
      "11n" -> return drawable.nt_thunderstorm
    }
    return drawable.unknown
  }
}
