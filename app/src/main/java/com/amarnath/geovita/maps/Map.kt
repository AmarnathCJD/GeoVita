package com.amarnath.geovita.maps

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.rememberCameraPositionState
import android.Manifest
import android.content.pm.PackageManager
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.amarnath.geovita.RequestLocationPermission
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.maps.android.compose.ComposeMapColorScheme
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState

@SuppressLint("UnrememberedMutableState")
@Composable
fun MapViewMain(paddingValues: PaddingValues) {
    val currentLocation = remember { mutableStateOf(LatLng(10.953551, 75.946148)) }
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(currentLocation.value, 11f)
    }

    if (currentLocation.value.latitude == 10.953551 && currentLocation.value.longitude == 75.946148) {
        val accuracy = Priority.PRIORITY_HIGH_ACCURACY
        val client = LocationServices.getFusedLocationProviderClient(LocalContext.current)
        if (ActivityCompat.checkSelfPermission(
                LocalContext.current,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                LocalContext.current,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            RequestLocationPermission(
                onPermissionGranted = {},
                onPermissionDenied = {},
                onPermissionsRevoked = {}
            )
            return
        }
        client.getCurrentLocation(accuracy, CancellationTokenSource().token)
            .addOnSuccessListener { loc ->
                loc?.let {
                    cameraPositionState.position = CameraPosition.fromLatLngZoom(
                        LatLng(it.latitude, it.longitude),
                        11f
                    )
                    println("Location: ${it.latitude}, ${it.longitude}")
                    currentLocation.value = LatLng(it.latitude, it.longitude)

                    cameraPositionState.position = CameraPosition.fromLatLngZoom(
                        LatLng(it.latitude, it.longitude),
                        11f
                    )
                }
            }
            .addOnFailureListener {
                currentLocation.value = LatLng(10.953551, 75.946148)
            }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                top = paddingValues.calculateTopPadding() - 77.dp,
                bottom = paddingValues.calculateBottomPadding(),
            )
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            val points = listOf<LatLng>(
                LatLng(8.8429, 76.0688),
                LatLng(7.1301, 75.4317),
                LatLng(9.4522, 72.6702),
                LatLng(9.3191, 76.7626),
                LatLng(9.4793, 80.8244),
                LatLng(10.6549, 74.5480),
                LatLng(9.6101, 76.7139),
                LatLng(9.9004, 77.3559),
                LatLng(9.5819, 76.1627),
                LatLng(9.8930, 76.0773)
            )

            val randAlerts = listOf<String>(
                "High Pollution",
                "Low Pollution",
                "Flooding",
                "High Pollution",
                "Low Pollution",
                "Flooding",
                "High Pollution",
                "Low Pollution",
                "Flooding",
                "High Pollution"
            )


            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState,
                onMyLocationClick = {
                    currentLocation.value = LatLng(it.latitude, it.longitude)
                },
                properties = MapProperties(
                    isTrafficEnabled = true,
                    isBuildingEnabled = true,
                    isIndoorEnabled = true,
                    isMyLocationEnabled = true,
                    mapType = MapType.NORMAL,
                ),
                uiSettings = MapUiSettings(
                    compassEnabled = true,
                    scrollGesturesEnabled = true,
                    scrollGesturesEnabledDuringRotateOrZoom = true,
                    rotationGesturesEnabled = true,
                    mapToolbarEnabled = false,
                    zoomControlsEnabled = true,
                    zoomGesturesEnabled = true,
                ),
                mapColorScheme = ComposeMapColorScheme.DARK,
                mergeDescendants = true
            ) {
                points.forEachIndexed { index, point ->
                    val alert = randAlerts[index]
                    Marker(
                        state = MarkerState(position = point),
                        title = "$index",
                        snippet = "Alert: $alert",
                    )
                }
            }
        }
    }
}