package fgcu.smartclock

import android.content.Context
import android.os.Bundle
import android.util.Log
import com.google.android.gms.awareness.Awareness
import com.google.android.gms.awareness.state.Weather
import com.google.android.gms.common.api.GoogleApiClient


class WeatherData(
  val context: Context
) : GoogleApiClient.ConnectionCallbacks {
  var address: CharSequence? = null
  var weather: Weather? = null

  init {
    var client = GoogleApiClient.Builder(context)
        .addApi(Awareness.API)
        .build()

    client.connect()

    Awareness.SnapshotApi.getPlaces(client)
        .setResultCallback { placesResult ->
          if (placesResult == null) address = null else address =
              placesResult.placeLikelihoods[0].place.address.toString()
        }

    Awareness.SnapshotApi.getWeather(client)
        .setResultCallback { WeatherData ->
          if (WeatherData == null) weather = null else weather = WeatherData.weather
        }
  }


  override fun onConnectionSuspended(p0: Int) {


  }

  override fun onConnected(p0: Bundle?) {
    var string1 =Awareness.getSnapshotClient(context).places.result.toString()
    var string2 = Awareness.getSnapshotClient(context).weather.result.toString()
    Log.d("WeatherData","Plces : " + string1 + " Weather : "+ string2)
  }



}