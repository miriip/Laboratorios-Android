package com.example.holayo

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

// Tu modelo de datos: una línea, y ya tiene igualdad, copia y representación.
data class Perfil(
    val nombre: String,
    val dato: String,
    val apodo: String? // el ? declara: "puede no haber apodo" — y el compilador lo vigila
)

class MainActivity : AppCompatActivity() {

    private val perfil = Perfil(
        nombre = "Miranda Guerra",
        dato = "Estoy cursando Aplicaciones Móviles",
        apodo = null // probá también con un apodo real: "Rama"
    )

    private var saludoFormal = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val tvSaludo = findViewById<TextView>(R.id.tvSaludo)
        val tvDato = findViewById<TextView>(R.id.tvDato)
        val btnSaludar = findViewById<Button>(R.id.btnSaludar)

        // Si hay apodo se usa; si es null, el nombre. El operador ?: es el "plan B".
        val comoLlamarme = perfil.apodo ?: perfil.nombre
        tvSaludo.text = "Hola, soy $comoLlamarme"
        tvDato.text = perfil.dato

        // Una lambda: la función que se ejecuta cuando el botón se toca.
        btnSaludar.setOnClickListener {
            saludoFormal = !saludoFormal
            tvSaludo.text = if (saludoFormal)
                "Hola, soy $comoLlamarme"
            else
                "¡Buenas! Acá $comoLlamarme"
        }
    }
}

