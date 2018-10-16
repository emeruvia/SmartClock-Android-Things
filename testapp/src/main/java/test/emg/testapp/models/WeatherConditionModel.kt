package test.emg.testapp.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class WeatherConditionModel(
  @SerializedName("main")
  val weatherName: String,
  @SerializedName("description")
  val weatherDescription: String,
  @SerializedName("icon")
  val weatherIcon: String
) : Serializable