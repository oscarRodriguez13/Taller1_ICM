package com.example.taller1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import org.json.JSONArray
import org.json.JSONObject
import java.nio.charset.Charset

class DestinosTuristicosActivity : AppCompatActivity() {
    var arreglo: MutableList<String> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_destinos_turisticos)
        val categoriaSeleccionada = intent.getStringExtra("categoriaSeleccionada")
        val lista = findViewById<ListView>(R.id.listaDestinos)
        val json = JSONObject(loadJsonFromAsset())
        val destinosJson = json.getJSONArray("destinos")
        for (i in 0 until destinosJson.length()) {
            val jsonObject = destinosJson.getJSONObject(i)
            if (categoriaSeleccionada == "Todos" || jsonObject.getString("categoria") == categoriaSeleccionada) {
                arreglo.add(jsonObject.getString("nombre"))
            }
        }
        val adaptador = ArrayAdapter(
            this, android.R.layout.simple_list_item_1, arreglo
        )

        lista.adapter = adaptador
        selectItem(lista,destinosJson)
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

    private fun selectItem(lista: ListView, destinosJson: JSONArray) {
        lista.setOnItemClickListener { parent, view, position, id ->
            val nombreDestino = arreglo[position]

            val bundle = Bundle().apply {
                for (i in 0 until destinosJson.length()) {
                    val jsonObject = destinosJson.getJSONObject(i)
                    if (jsonObject.getString("nombre") == nombreDestino) {
                        putString("nombre", jsonObject.getString("nombre"))
                        putString("pais", jsonObject.getString("pais"))
                        putDouble("latitud", jsonObject.getDouble("latitud"))
                        putDouble("longitud", jsonObject.getDouble("longitud"))
                        putString("categoria", jsonObject.getString("categoria"))
                        putString("plan", jsonObject.getString("plan"))
                        putInt("precio", jsonObject.getInt("precio"))
                    }
                }
            }

            val intent = Intent(this@DestinosTuristicosActivity, DetalleDestinoActivity::class.java).apply {
                putExtras(bundle)
            }

            // Iniciar DetalleDestinoActivity
            startActivity(intent)
        }
    }

}