package com.example.taller1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupSpinner()
        setupExplorarButton()
        setupFavoritosButton()
        setupRecomendadosButton()
    }

    private fun setupSpinner() {
        val spinCategorias = findViewById<Spinner>(R.id.spinCategorias)
        val categorias = resources.getStringArray(R.array.categorias)
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, categorias)
        spinCategorias.adapter = adapter
    }

    private fun setupExplorarButton() {
        val btnExplorar = findViewById<Button>(R.id.btnExplorar)
        btnExplorar.setOnClickListener {
            navigateToDestinosTuristicosActivity()
        }
    }

    private fun navigateToDestinosTuristicosActivity() {
        val spinCategorias = findViewById<Spinner>(R.id.spinCategorias)
        val categoriaSeleccionada = spinCategorias.selectedItem.toString()
        val intent = Intent(applicationContext, DestinosTuristicosActivity::class.java).apply {
            putExtra("categoriaSeleccionada", categoriaSeleccionada)
        }
        startActivity(intent)
    }

    companion object {
        val favoritos: MutableList<Bundle> = mutableListOf()
    }

    private fun setupFavoritosButton() {
        val btnFavoritos = findViewById<Button>(R.id.btnFavoritos)
        btnFavoritos.setOnClickListener {
            navigateToFavoritosActivity()
        }
    }

    private fun navigateToFavoritosActivity() {
        val intent = Intent(this, FavoritosActivity::class.java)
        startActivity(intent)
    }

    private fun setupRecomendadosButton() {
        val btnRecomendacion = findViewById<Button>(R.id.btnRecomendaciones)
        btnRecomendacion.setOnClickListener {
            navigateToRecomendacionesActivity()
        }
    }
    private fun navigateToRecomendacionesActivity() {
        val intent = Intent(this, RecomendacionesActivity::class.java)
        startActivity(intent)
    }
}
