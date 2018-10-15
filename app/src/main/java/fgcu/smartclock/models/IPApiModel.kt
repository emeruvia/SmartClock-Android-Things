package fgcu.smartclock

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class IPApiModel(
  @SerializedName("as")
  val asNumber: String,
  val city: String,
  val country: String,
  val countryCode: String,
  val isp: String,
  val lat: Double,
  val lon: Double,
  val org: String,
  @SerializedName("query")
  val ipAdress: String,
  val region: String,
  val regionName: String,
  val timezone: String,
  val zip: String
) : Serializable