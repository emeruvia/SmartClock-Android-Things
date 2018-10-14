package fgcu.smartclock

import android.app.Activity
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import com.google.android.gms.awareness.Awareness
import com.google.android.gms.common.api.GoogleApiClient

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
class MainActivity : Activity(), GoogleApiClient.ConnectionCallbacks {

  var client : GoogleApiClient? = null
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    client = GoogleApiClient.Builder(this)
        .addApi(Awareness.API)
        .addConnectionCallbacks(this)
        .build()

    client?.connect()
    if (client?.isConnected as Boolean) {
      Log.d("MainActivity", "Connection Sucessful")

    }

  }

  override fun onConnectionSuspended(p0: Int) {
    Log.d("MainActivity", "Entered onConnectionSuspended")

  }

  override fun onConnected(p0: Bundle?) {
    Log.d("MainActivity", "Entered onConnected")

    Log.d("MainActivity", "Tried getWeather")
    Awareness.SnapshotApi.getWeather(client)
        .setResultCallback { WeatherData ->
          if (!WeatherData.status.isSuccess) Log.d("MainActivity", "Weather Failed") else
            Log.d("MainActivity", "Weather : " +WeatherData.status.isSuccess)
        }

    Log.d("MainActivity", "Tried getLocation")
    Awareness.SnapshotApi.getPlaces(client)
        .setResultCallback { placesResult ->
          if (!placesResult.status.isSuccess) Log.d("MainActivity", "Places Failed")
          Log.d("MainActivity", "Places : " +placesResult.status.isSuccess)
        }



//    Log.d("MainActivity", "Places : " + string1 + " Weather : " + string2)
  }
}
