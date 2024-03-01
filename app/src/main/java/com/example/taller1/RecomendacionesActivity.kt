package com.example.taller1

import WeatherRepository
import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import org.json.JSONObject
import java.nio.charset.Charset

class RecomendacionesActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recomendaciones)
        setupTextView()
    }

    @SuppressLint("SetTextI18n")
    private fun setupTextView() {
        val nombreDestinoTextView = findViewById<TextView>(R.id.nombreDestino)
        val paisTextView = findViewById<TextView>(R.id.pais)
        val categoriaTextView = findViewById<TextView>(R.id.categoria)
        val planTextView = findViewById<TextView>(R.id.plan)
        val precioTextView = findViewById<TextView>(R.id.precio)
        val categoria = categoriaMasFrecuente()
        if ( categoria == "N/A"){
            nombreDestinoTextView.text = "N/A"
            paisTextView.text = ""
            categoriaTextView.text = ""
            planTextView.text = ""
            precioTextView.text = ""
        } else{
            val destino = destinoRecomendado(categoria)
            nombreDestinoTextView.text = destino.getString("nombre")
            paisTextView.text = destino.getString("pais")
            val lat = destino.getDouble("latitud")
            val lon = destino.getDouble("longitud")
            categoriaTextView.text = destino.getString("categoria")
            planTextView.text = destino.getString("plan")
            precioTextView.text = "USD " + destino.getInt("precio").toString()
            consultarClima(lat, lon)
        }

    }

    private fun consultarClima(latitud : Double, longitud : Double){
        val apiKey = "181ad6cc7bb10dce77de7ff69bd5d0ac"
        val climaTextView = findViewById<TextView>(R.id.clima)
        var weatherRepository: WeatherRepository = WeatherRepository()
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



    private fun categoriaMasFrecuente(): String {
        val categorias = listOf("Playas", "Montañas", "Ciudades Históricas", "Maravillas del Mundo", "Selvas")
        val listaDeEnteros = MutableList(categorias.size) { 0 }

        if(MainActivity.favoritos == null){
            return "N/A"
        }

        for (destino in MainActivity.favoritos) {
            val categoria = destino.getString("categoria")

            for (x in categorias.indices) {
                if (categorias[x] == categoria) {
                    listaDeEnteros[x] += 1
                    break
                }
            }
        }

        var max = listaDeEnteros.maxOrNull() ?: 0
        if (max == 0) {
            return "N/A"
        }

        val categoriaMasFrecuenteIndex = listaDeEnteros.indexOf(max)
        return categorias[categoriaMasFrecuenteIndex]
    }

    fun loadJsonFromAsset(): String? {
        var json = ""
        try {
            val stream = assets.open("destinos.json")
            val size = stream.available()
            val buffer = ByteArray(size)
            stream.read(buffer)
            stream.close()
            json = String(buffer, Charset.forName("UTF-8"))
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return json
    }

    private fun destinoRecomendado(categoriaRecomendada: String): Bundle {
        val json = JSONObject(loadJsonFromAsset())
        val destinosJson = json.getJSONArray("destinos")

        // Filtrar los destinos que tienen la categoría recomendada
        val destinosDisponibles = (0 until destinosJson.length())
            .map { destinosJson.getJSONObject(it) }
            .filter { it.getString("categoria") == categoriaRecomendada }

        val destinosFavoritos = MainActivity.favoritos.filter { it.getString("categoria") == categoriaRecomendada }

        val destinoAleatorio = destinosFavoritos.randomOrNull()
        val destinoRecomendado = Bundle()
        destinoAleatorio?.let { destino ->
            destinoRecomendado.putString("nombre", destino.getString("nombre"))
            destinoRecomendado.putString("pais", destino.getString("pais"))
            destinoRecomendado.putDouble("latitud", destino.getDouble("latitud"))
            destinoRecomendado.putDouble("longitud", destino.getDouble("longitud"))
            destinoRecomendado.putString("categoria", destino.getString("categoria"))
            destinoRecomendado.putString("plan", destino.getString("plan"))
            destinoRecomendado.putInt("precio", destino.getInt("precio"))
        }
        return destinoRecomendado
    }


}