package com.example.taller1

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity

class FavoritosActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favoritos)
        val listViewFavoritos = findViewById<ListView>(R.id.listaFavoritos)
        val nombresDestinos = obtainNombresDestinos(MainActivity.favoritos)
        if (nombresDestinos.isEmpty()) {
            val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, listOf("N/A"))
            listViewFavoritos.adapter = adapter
        } else {
            val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, nombresDestinos)
            listViewFavoritos.adapter = adapter
        }
        listViewFavoritos.setOnItemClickListener { parent, view, position, id ->
            val destinoBundle = MainActivity.favoritos[position]
            val intent = Intent(this@FavoritosActivity, DetalleDestinoActivity::class.java).apply {
                putExtras(destinoBundle)
            }
            startActivity(intent)
        }
    }

    private fun obtainNombresDestinos(destinos: List<Bundle>): List<String> {
        val nombres = mutableListOf<String>()
        for (destino in destinos) {
            val nombre = destino.getString("nombre")
            if (nombre != null) {
                nombres.add(nombre)
            }
        }
        return nombres
    }
}
