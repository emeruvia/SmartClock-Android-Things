package test.emg.testapp.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class WeatherInfoModel(
  @SerializedName("weather")
  val weatherCondition: List<WeatherConditionModel>,
  @SerializedName("main")
  val temperature: TemperatureModel
) : Serializable