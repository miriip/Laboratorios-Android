package com.example.appteca3

import android.util.Log
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class AppTecaViewModel : ViewModel() {
    private var query = ""
    private var soloFavoritas = false

    private val _listaVisible = MutableStateFlow<List<App>>(emptyList())
    val listaVisible: StateFlow<List<App>> = _listaVisible

    private val _modoSoloFavoritas = MutableStateFlow(false)
    val modoSoloFavoritas: StateFlow<Boolean> = _modoSoloFavoritas

    private val _appSeleccionada = MutableStateFlow<App?>(null)
    val appSeleccionada: StateFlow<App?> = _appSeleccionada

    init {
        Log.d("VIDA", "ViewModel → creado (${hashCode()})")
        aplicarFiltros()
    }

    fun buscar(texto: String) {
        query = texto.trim()
        aplicarFiltros()
    }

    fun alternarModo() {
        soloFavoritas = !soloFavoritas
        aplicarFiltros()
    }

    fun alternarFavorita(app: App) {
        val nuevas = Catalogo.apps.map {
            if (it.id == app.id) it.copy(esFavorita = !it.esFavorita) else it
        }
        Catalogo.apps.clear()
        Catalogo.apps.addAll(nuevas)
        aplicarFiltros()
    }

    fun seleccionar(app: App) {
        _appSeleccionada.value = Catalogo.apps.find { it.id == app.id }
    }

    fun volverALista() {
        _appSeleccionada.value = null
    }

    private fun aplicarFiltros() {
        val apps = Catalogo.apps.toList()
        var lista: List<App> = apps
        if (query.isNotEmpty()) {
            lista = lista.filter {
                it.nombre.contains(query, true) || it.categoria.contains(query, true)
            }
        }
        if (soloFavoritas) {
            lista = lista.filter { it.esFavorita }
        }
        _listaVisible.value = lista
        _modoSoloFavoritas.value = soloFavoritas
        _appSeleccionada.value = _appSeleccionada.value?.let { seleccionada ->
            apps.find { it.id == seleccionada.id }
        }
    }

    override fun onCleared() {
        Log.d("VIDA", "ViewModel → onCleared")
    }
}
