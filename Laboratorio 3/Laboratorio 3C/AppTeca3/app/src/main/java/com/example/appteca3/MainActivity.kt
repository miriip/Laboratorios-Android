package com.example.appteca3

import android.os.Bundle
import android.graphics.Color
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("VIDA", "Main → onCreate")
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.light(Color.WHITE, Color.WHITE),
            navigationBarStyle = SystemBarStyle.light(Color.WHITE, Color.WHITE)
        )
        setContent {
            AppTeca3Theme {
                PantallaAppTeca()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("VIDA", "Main → onDestroy")
    }
}

@Composable
fun AppTeca3Theme(content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = lightColorScheme(), content = content)
}

@Composable
fun PantallaAppTeca(vm: AppTecaViewModel = viewModel()) {
    val lista by vm.listaVisible.collectAsStateWithLifecycle()
    val modoFav by vm.modoSoloFavoritas.collectAsStateWithLifecycle()
    val seleccionada by vm.appSeleccionada.collectAsStateWithLifecycle()
    var textoBusqueda by rememberSaveable { mutableStateOf("") }
    val safeInsets = Modifier.statusBarsPadding().navigationBarsPadding()

    val app = seleccionada
    if (app != null) {
        DetalleApp(
            app = app,
            onFavoritoClick = { vm.alternarFavorita(app) },
            onVolver = { vm.volverALista() }
        )
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .then(safeInsets)
        ) {
            OutlinedTextField(
                value = textoBusqueda,
                onValueChange = { nuevo ->
                    textoBusqueda = nuevo
                    vm.buscar(nuevo)
                },
                label = { Text("Buscar por nombre o categoría…") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                singleLine = true
            )

            Button(
                onClick = { vm.alternarModo() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Text(if (modoFav) "★ Solo favoritas" else "☆ Todas")
            }

            ListaApps(
                apps = lista,
                onAppClick = { vm.seleccionar(it) },
                onFavoritoClick = { vm.alternarFavorita(it) },
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun ListaApps(
    apps: List<App>,
    onAppClick: (App) -> Unit,
    onFavoritoClick: (App) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier.fillMaxWidth()) {
        items(apps, key = { it.id }) { app ->
            FilaApp(
                app = app,
                onClick = { onAppClick(app) },
                onFavoritoClick = { onFavoritoClick(app) }
            )
        }
    }
}

@Composable
fun FilaApp(app: App, onClick: () -> Unit, onFavoritoClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(app.nombre, style = MaterialTheme.typography.titleMedium)
            Text(app.categoria, style = MaterialTheme.typography.bodySmall)
        }
        Text(
            text = if (app.esFavorita) "★" else "☆",
            fontSize = 24.sp,
            modifier = Modifier
                .clickable { onFavoritoClick() }
                .padding(8.dp)
        )
    }
}

@Composable
fun DetalleApp(app: App, onFavoritoClick: () -> Unit, onVolver: () -> Unit) {
    BackHandler(onBack = onVolver)
    val safeInsets = Modifier.statusBarsPadding().navigationBarsPadding()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .then(safeInsets)
            .padding(24.dp),
        verticalArrangement = Arrangement.Top
    ) {
        Text(app.nombre, style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.Bold)
        Text(app.categoria, style = MaterialTheme.typography.bodyMedium)
        Spacer(Modifier.height(16.dp))
        Text(app.descripcion, style = MaterialTheme.typography.bodyLarge)
        Spacer(Modifier.height(24.dp))
        Button(onClick = onFavoritoClick) {
            Text(if (app.esFavorita) "★ Quitar de favoritas" else "☆ Marcar favorita")
        }
    }
}
