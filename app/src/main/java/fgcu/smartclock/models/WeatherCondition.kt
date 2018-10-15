package fgcu.smartclock.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class WeatherCondition( @SerializedName("main") val weatherName : String, @SerializedName("description") val weatherDescription : String, @SerializedName("icon") val weatherIcon : String) : Serializable