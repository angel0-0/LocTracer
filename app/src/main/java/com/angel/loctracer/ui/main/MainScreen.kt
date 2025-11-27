package com.angel.loctracer.ui.main

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import kotlinx.coroutines.flow.collectLatest

@Composable
fun MainScreen(
    navController: NavController,
    viewModel: MainViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }

    val permissions = arrayOf(
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION
    )

    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissionsMap ->
        val allGranted = permissionsMap.values.all { it }
        if (allGranted) {
            viewModel.saveCurrentLocation()
        }
    }

    LaunchedEffect(key1 = Unit) {
        viewModel.locationSaved.collectLatest {
            snackbarHostState.showSnackbar("Ubicación guardada con éxito")
        }
    }

    LaunchedEffect(key1 = Unit) {
        viewModel.error.collectLatest {
            snackbarHostState.showSnackbar(it)
        }
    }

    MainScreenContent(
        snackbarHostState = snackbarHostState,
        onSaveClick = {
            if (permissions.all { ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED }) {
                viewModel.saveCurrentLocation()
            } else {
                launcher.launch(permissions)
            }
        },
        onNavigateToSaved = { navController.navigate("saved") }
    )
}

@Composable
fun MainScreenContent(
    snackbarHostState: SnackbarHostState,
    onSaveClick: () -> Unit,
    onNavigateToSaved: () -> Unit
) {
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                onClick = onSaveClick,
                modifier = Modifier.size(150.dp),
                shape = MaterialTheme.shapes.extraLarge,
                contentPadding = PaddingValues(0.dp)
            ) {
                Text(text = "📍", fontSize = 80.sp)
            }

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = onNavigateToSaved,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary
                )
            ) {
                Text(text = "guardados", color = Color.Black)
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    val snackbarHostState = remember { SnackbarHostState() }
    MainScreenContent(
        snackbarHostState = snackbarHostState,
        onSaveClick = {},
        onNavigateToSaved = {}
    )
}
