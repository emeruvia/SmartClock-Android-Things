package test.emg.testapp

data class IPApi(
  val ip: String,
  val city: String,
  val region: String,
  val country: String,
  val postal: String,
  val latitude: Float,
  val longitude: Float
)