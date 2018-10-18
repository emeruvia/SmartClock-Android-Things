package fgcu.smartclock.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class WeatherInfoModel(
//  @SerializedName("weather")
//  val weatherCondition: WeatherConditionModel,
//  @SerializedName("main")
//  val temperature: TemperatureModel
  @SerializedName("dt")
  val date: Long,
  @SerializedName("name")
  val city: String
)