package fgcu.smartclock.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class WeatherInfo(
  @SerializedName("weather")
  val weatherCondition: WeatherCondition,
  @SerializedName("main")
  val temperature: Temperature
) : Serializable