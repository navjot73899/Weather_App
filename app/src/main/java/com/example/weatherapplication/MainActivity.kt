package com.example.weatherapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.SearchView
import android.widget.Toast
import com.example.weatherapplication.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity : AppCompatActivity() {
    lateinit var binding:ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        fetchWeatherData("delhi")
        searchCity()
    }

    private fun searchCity() {
       val searchview= binding.searchView
        searchview.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    fetchWeatherData(query)
                }
                return true
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                return true
            }
        })
    }

    private fun datety():String {
        val sdf= SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
        return sdf.format((Date()))

    }

    private fun fetchWeatherData(cityName:String) {
       val retrofit= Retrofit.Builder()
           .addConverterFactory(GsonConverterFactory.create())
           .baseUrl("https://api.openweathermap.org/data/2.5/")
           .build().create(ApiInterface::class.java)

        val response= retrofit.getWeatherData(cityName,"5a5fe2f4fc5ad851a1755b8893791969","metric")
         response.enqueue(object :Callback<WeatherApp>{
             override fun onResponse(call: Call<WeatherApp>, response: Response<WeatherApp>) {
                 val responseBody= response.body()
                 if(response.isSuccessful && responseBody != null){

                     val temperature= responseBody.main.temp.toString()
                     val humidity= responseBody.main.humidity
                     val windspeed= responseBody.wind.speed
                     val sunRise =responseBody.sys.sunrise.toLong()
                     val sunSet=responseBody.sys.sunset.toLong()
                     val seaLevel = responseBody.main.pressure
                     val condition = responseBody.weather.firstOrNull()?.main?:"unknown"
                     val maxTemp= responseBody.main.temp_max
                     val minTemp= responseBody.main.temp_min
                    // Log.d("bbb","Onrespnse $temperature")
                     binding.apply {
                         temperatureT.text= temperature
                         weather.text=condition
                         Maximum.text= "Max Temp:$maxTemp °C"
                         minimum.text= "Min Temp:$minTemp °C"
                         humidity1.text="$humidity %"
                         windspeed1.text="$windspeed m/s"
                         condition1.text=condition
                         sunrise.text="${time(sunRise)}"
                         sunset.text="${time(sunSet)}"
                         day.text=dayName(System.currentTimeMillis())
                         date.text =datety()
                         currentCity.text=cityName
                         sealevel.text= seaLevel.toString()

                         changeImageAccordingToWeatherCondition(condition)
                     }
                 }
             }

             override fun onFailure(call: Call<WeatherApp>, t: Throwable) {
              Toast.makeText(this@MainActivity,"errorr",Toast.LENGTH_SHORT).show()
             }
         })

    }

    private fun changeImageAccordingToWeatherCondition(condition: String) {

        when(condition){
            "Clear Sky","Sunny","Clear", "Haze"->{
                binding.root.setBackgroundResource(R.drawable.sunny_background)
                binding.lottieAnimationView.setAnimation(R.raw.sun)
            }

            "Partly Clouds","Clouds","Overcast","Mist","Foggy"->{
                binding.root.setBackgroundResource(R.drawable.colud_background)
                binding.lottieAnimationView.setAnimation(R.raw.cloud)
            }
            "Light Rain","Drizzle","Moderate Rain", "Showers","Heavy Rain"->{
                binding.root.setBackgroundResource(R.drawable.rain_background)
                binding.lottieAnimationView.setAnimation(R.raw.rain)
            }
            "Light Snow","Moderate Snow","Heavy Snow" , "Blizzard"->{
                binding.root.setBackgroundResource(R.drawable.snow_background)
                binding.lottieAnimationView.setAnimation(R.raw.snow)
            }
            else ->{

                    binding.root.setBackgroundResource(R.drawable.sunny_background)
                    binding.lottieAnimationView.setAnimation(R.raw.sun)
            }
        }
        binding.lottieAnimationView.playAnimation()
    }

    fun  dayName(timestamp:Long):String{
        val sdf= SimpleDateFormat("EEEE", Locale.getDefault())
        return sdf.format((Date()))
    }

    private fun time(timeStamp:Long):String {
        val sdf= SimpleDateFormat("HH:mm", Locale.getDefault())
        return sdf.format((timeStamp*1000))

    }


}