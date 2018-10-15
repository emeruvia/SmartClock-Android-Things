package fgcu.smartclock.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Temperature(
  @SerializedName("temp")
  val currentTemperature: Double,
  @SerializedName("temp_max")
  val highTemperature: Double,
  @SerializedName("temp_min")
  val lowTemperature: Double
) : Serializable