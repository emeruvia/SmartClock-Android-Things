package fgcu.smartclock.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class WeatherModel(
  @SerializedName("weather")
  val weatherCondition: List<WeatherConditionModel>,
  @SerializedName("main")
  val temperature: TemperatureModel,
  @SerializedName("dt")
  val date: Double,
  @SerializedName("name")
  val city: String
) : Serializable