package com.example.taller1

import WeatherRepository
import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast

class DetalleDestinoActivity : AppCompatActivity() {
    private lateinit var nombreDestinoTextView: TextView
    private lateinit var paisTextView: TextView
    private lateinit var climaTextView: TextView
    private lateinit var categoriaTextView: TextView
    private lateinit var planTextView: TextView
    private lateinit var precioTextView: TextView
    private lateinit var weatherRepository: WeatherRepository
    private var bundle: Bundle? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalle_destino)
        weatherRepository = WeatherRepository()

        initViews()
        extractAndDisplayDestinationDetails()
        setupFavoritesButton()

        val btnFavoritos = findViewById<Button>(R.id.btnFavs)
        val nombreDestino = bundle?.getString("nombre")
        if (nombreDestino != null && isDestinationInFavorites(nombreDestino)) {
            btnFavoritos.isEnabled = false
        }
    }


    private fun initViews() {
        nombreDestinoTextView = findViewById(R.id.nombreDestino)
        paisTextView = findViewById(R.id.pais)
        climaTextView = findViewById(R.id.clima)
        categoriaTextView = findViewById(R.id.categoria)
        planTextView = findViewById(R.id.plan)
        precioTextView = findViewById(R.id.precio)
    }

    @SuppressLint("SetTextI18n")
    private fun extractAndDisplayDestinationDetails() {
        var latitud = 0.0
        var longitud = 0.0
        bundle = intent.extras
        bundle?.let {
            nombreDestinoTextView.text = it.getString("nombre")
            paisTextView.text = it.getString("pais")
            latitud = it.getDouble("latitud")
            longitud = it.getDouble("longitud")
            categoriaTextView.text = it.getString("categoria")
            planTextView.text = it.getString("plan")
            precioTextView.text = "$ " + it.getInt("precio").toString()
        }
        val apiKey = "181ad6cc7bb10dce77de7ff69bd5d0ac"

        latitud.let { lat ->
            longitud.let { lon ->
                weatherRepository.getCurrentWeatherByCoordinates(lat, lon, apiKey) { weatherResponse ->
                    runOnUiThread {
                        if (weatherResponse != null) {
                            // Actualizar la UI con la respuesta del clima
                            climaTextView.text = "${weatherResponse.main.temp}°C"
                        } else {
                            // Manejar el error
                            climaTextView.text = "Error al obtener el clima"
                        }
                    }
                }
            }
        }
    }

    private fun setupFavoritesButton() {
        val btnFavoritos = findViewById<Button>(R.id.btnFavs)
        btnFavoritos.setOnClickListener { onFavoritosButtonClick() }
    }

    private fun onFavoritosButtonClick() {
        val btnFavoritos = findViewById<Button>(R.id.btnFavs)
        btnFavoritos.isEnabled = false  // Deshabilitar el botón

        val nombreDestino = bundle?.getString("nombre")
        if (nombreDestino != null && !isDestinationInFavorites(nombreDestino)) {
            bundle?.let { MainActivity.favoritos.add(it) }
            showToast("Añadido a Favoritos")
        }
    }


    private fun isDestinationInFavorites(nombreDestino: String): Boolean {
        return MainActivity.favoritos.any { destino ->
            destino.getString("nombre") == nombreDestino
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
    }
}
