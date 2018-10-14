package fgcu.smartclock

import android.content.Context
import android.util.Log
import com.google.android.gms.awareness.Awareness
import com.google.android.gms.awareness.state.Weather
import com.google.android.gms.common.api.GoogleApiClient


class WeatherData(
  val context: Context
) {
  var address: CharSequence? = null
  var weather: Weather? = null

  init {
    var client = GoogleApiClient.Builder(context)
        .addApi(Awareness.API)
        .build()

    client.connect()

    Awareness.getSnapshotClient(context).places.addOnCompleteListener { task -> address = task.result.toString()  }
    Log.d("WeatherData", "Places successfully loaded: "+ Awareness.getSnapshotClient(context).places.isComplete)
//    Awareness.SnapshotApi.getPlaces(client)
//        .setResultCallback { placesResult ->
//          if (placesResult == null) address = null else address =
//              placesResult.placeLikelihoods[0].place.address.toString()
//        }

    Awareness.SnapshotApi.getWeather(client)
        .setResultCallback { WeatherData ->
          if (WeatherData == null) weather = null else weather = WeatherData.weather
        }
  }

}