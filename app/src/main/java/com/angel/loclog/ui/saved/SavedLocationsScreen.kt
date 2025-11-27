package com.angel.loclog.ui.saved

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.angel.loclog.data.local.LocationEntity
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun SavedLocationsScreen(
    onBack: () -> Unit,
    viewModel: SavedLocationsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    var showDeleteDialog by remember { mutableStateOf<LocationEntity?>(null) }
    var showEditDialog by remember { mutableStateOf<LocationEntity?>(null) }

    // Show error toast if there is one
    LaunchedEffect(uiState.error) {
        uiState.error?.let {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Ubicaciones Guardadas") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Atrás"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        if (uiState.locations.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                // Show a progress bar while loading, or the empty message if not loading
                if (uiState.error == null) { // A simple way to check if we are loading for the first time
                    Text(text = "No hay ubicaciones guardadas")
                } else {
                    CircularProgressIndicator()
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.padding(paddingValues),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(uiState.locations, key = { it.id }) { location ->
                    LocationItem(
                        modifier = Modifier.animateItemPlacement(),
                        location = location,
                        onDelete = { showDeleteDialog = it },
                        onEdit = { showEditDialog = it }
                    )
                }
            }
        }
    }


    showDeleteDialog?.let { locationToDelete ->
        DeleteConfirmDialog(
            onConfirm = {
                viewModel.deleteLocation(locationToDelete.id)
                showDeleteDialog = null
            },
            onDismiss = { showDeleteDialog = null }
        )
    }

    showEditDialog?.let { locationToEdit ->
        var newTitle by remember(locationToEdit) { mutableStateOf(locationToEdit.title) }
        EditTitleDialog(
            currentTitle = newTitle,
            onTitleChange = { newTitle = it },
            onConfirm = {
                viewModel.updateLocationTitle(locationToEdit, newTitle)
                showEditDialog = null
            },
            onDismiss = { showEditDialog = null }
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun LocationItem(
    modifier: Modifier = Modifier,
    location: LocationEntity,
    onDelete: (LocationEntity) -> Unit,
    onEdit: (LocationEntity) -> Unit
) {
    val context = LocalContext.current

    Card(
        modifier = modifier
            .fillMaxWidth()
            .combinedClickable(
                onClick = {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(location.mapsLink))
                    context.startActivity(intent)
                },
                onLongClick = { onEdit(location) }
            ),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = location.title, style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = "Lat: ${location.latitude}, Lon: ${location.longitude}", style = MaterialTheme.typography.bodySmall)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = formatDate(location.timestamp), style = MaterialTheme.typography.bodySmall)
            }
            Spacer(modifier = Modifier.width(16.dp))
            IconButton(onClick = { onDelete(location) }) {
                Icon(imageVector = Icons.Default.Delete, contentDescription = "Borrar")
            }
        }
    }
}

@Composable
fun DeleteConfirmDialog(onConfirm: () -> Unit, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Confirmar borrado") },
        text = { Text("¿Estás seguro de que quieres borrar esta ubicación?") },
        confirmButton = { Button({ onConfirm() }) { Text("Borrar") } },
        dismissButton = { TextButton({ onDismiss() }) { Text("Cancelar") } }
    )
}

@Composable
fun EditTitleDialog(
    currentTitle: String,
    onTitleChange: (String) -> Unit,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Editar título") },
        text = {
            OutlinedTextField(
                value = currentTitle,
                onValueChange = onTitleChange,
                label = { Text("Nuevo título") },
                singleLine = true
            )
        },
        confirmButton = { Button({ onConfirm() }) { Text("Guardar") } },
        dismissButton = { TextButton({ onDismiss() }) { Text("Cancelar") } }
    )
}

private fun formatDate(timestamp: Long): String {
    if (timestamp == 0L) return "Fecha no disponible"
    val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())
    return sdf.format(Date(timestamp))
}

