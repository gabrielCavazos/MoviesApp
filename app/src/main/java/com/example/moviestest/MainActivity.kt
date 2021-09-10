package com.example.moviestest

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.moviestest.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.DisposableHandle
import kotlinx.coroutines.launch
import okhttp3.Dispatcher
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: MovieAdapter
    val movieList = mutableListOf<Movie>()
    /*val movieList = listOf<Movie>(
            Movie("Don't Breathe 2",
                    214,
                    2.5,
                    "/pUc51UUQb1lMLVVkDCaZVsCo37U.jpg",
                    "The Blind Man has been hiding out for several years in an isolated cabin and has taken in and raised a young girl orphaned from a devastating house fire."),

            Movie("Shang-Chi and the Legend of the Ten Rings",
                    541,
                    9.8,
                    "/nDLylQOoIazGyYuWhk21Yww5FCb.jpg",
                    "Shang-Chi must confront the past he thought he left behind when he is drawn into the web of the mysterious Ten Rings organization."),
    )*/
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initRecycler()
        if(checkForInternet(this))
            findMovies()
        else
            fetchSavedMovies()

    }

    private fun fetchSavedMovies(){
        val dataBase = AppDataBase.getDatabase(this@MainActivity)

        dataBase.movies().getAll().observe(this@MainActivity, Observer{
            movieList.clear()
            movieList.addAll(it)
            adapter.notifyDataSetChanged()

        })
    }

    private fun getRetrofit(): Retrofit {
        return Retrofit
                .Builder()
                .baseUrl("https://api.themoviedb.org/3/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
    }

    private fun findMovies(){
        CoroutineScope(Dispatchers.IO).launch {
            val call: Response<MoviesResponse> = getRetrofit().create(APIService::class.java).getMovies("movie/popular?api_key=6f69d45e4cfbba1a8cd274b29616477f&language=en-US&page=1")
            val moviesResponse = call.body()
            val dataBase = AppDataBase.getDatabase(this@MainActivity)
            if(call.isSuccessful){

                dataBase.movies().deleteAll()
                val movies = moviesResponse?.movies?: emptyList<Movie>()
                movieList.clear()
                movieList.addAll(movies)
                for (movie in movies) {
                    dataBase.movies().insert(movie)
                }

            }else{



                showError()
            }
            runOnUiThread{

                adapter.notifyDataSetChanged()
            }

        }
    }

    private fun checkForInternet(context: Context): Boolean {

        // register activity with the connectivity manager service
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        // if the android version is equal to M
        // or greater we need to use the
        // NetworkCapabilities to check what type of
        // network has the internet connection
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            // Returns a Network object corresponding to
            // the currently active default data network.
            val network = connectivityManager.activeNetwork ?: return false

            // Representation of the capabilities of an active network.
            val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false

            return when {
                // Indicates this network uses a Wi-Fi transport,
                // or WiFi has network connectivity
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true

                // Indicates this network uses a Cellular transport. or
                // Cellular has network connectivity
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true

                // else return false
                else -> false
            }
        } else {
            // if the android version is below M
            @Suppress("DEPRECATION") val networkInfo =
                connectivityManager.activeNetworkInfo ?: return false
            @Suppress("DEPRECATION")
            return networkInfo.isConnected
        }
    }

    fun initRecycler(){
        binding.rvMovies.layoutManager = LinearLayoutManager(this)
        adapter = MovieAdapter(movieList)
        binding.rvMovies.adapter = adapter


    }

    fun showError(){
        Toast.makeText(this, "No se pudo llamar al servidor", Toast.LENGTH_SHORT).show()

    }
}