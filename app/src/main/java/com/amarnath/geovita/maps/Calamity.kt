package com.amarnath.geovita.maps

import android.annotation.SuppressLint
import android.location.Geocoder
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.ComposeMapColorScheme
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException
import java.util.Locale

fun sendLatLngToPythonAPI(
    lat: Double, lon: Double,
    updateMessage: (String) -> Unit,
    updateInfo: (String) -> Unit,
    updateAlert: (String) -> Unit
) {
    Log.d("Bruhhh", "Sending request to Python API")
    val client = OkHttpClient()
    val server = "https://4ccb-2409-40f3-20-c9d8-a065-e9aa-b07b-9767.ngrok-free.app"
    val url = "${server}/weather"

    val json = JSONObject()
    json.put("lat", lat)
    json.put("lon", lon)

    val body = json.toString().toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())

    val request = Request.Builder()
        .url(url)
        .post(body)
        .build()

    client.newCall(request).enqueue(object : Callback {
        override fun onFailure(call: Call, e: IOException) {
//            e.printStackTrace()
            Log.d("Balls", "Failed to send request ${e.printStackTrace()}")
        }

        override fun onResponse(call: Call, response: Response) {
            val responseData = response.body?.string()
            if (response.isSuccessful && responseData != null) {
                val data = JSONObject(responseData)
                val newMessage = data.getString("message")
                val newInfo = data.getString("info")

                updateMessage(newMessage)
                updateInfo(newInfo)
                updateAlert("Alert: ${data.getString("alert")}")
//                Log.d("Workingg", "Response: $responseData")
            } else {
                println("Request failed: $responseData")
            }
        }
    })
}

@Composable
fun Weather_warning() {
    var location by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }
    var additionalInfo by remember { mutableStateOf("") }
    var alert by remember { mutableStateOf("") }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF252533))
            .padding(bottom = 10.dp)
            .verticalScroll(rememberScrollState())

    ) {
        Text(
            text = "Weather Alert",
            style = TextStyle(
                fontWeight = FontWeight.Bold,
                fontSize = 30.sp
            ),
            color = Color(0xFFB3E5FC),
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)

        )
        Spacer(modifier = Modifier.height(10.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .height(400.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Color(0xFFf8f8f2))
        ) {
            Map(
                modifier = Modifier.fillMaxWidth(),
                updateLocation = { newLocation -> location = newLocation },
                updateMessage = { newMessage -> message = newMessage },
                updateInfo = { newInfo -> additionalInfo = newInfo },
                updateAlert = { newAlert -> alert = newAlert }
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(0.dp)
                    .height(64.dp)
                    .background(Color(0x6B646464))
            )
            Text(
                text = "Map",
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp
                ),
                color = Color(0xFFB3E5FC),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, start = 16.dp)

            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(Color(0xFF282a36)),

            ) {
            Text(
                text = location,
                style = TextStyle(
                    fontSize = 24.sp
                ),
                color = Color(0xFFE1F5FE),
                modifier = Modifier.padding(16.dp)
            )
            Text(
                text = message,
                style = TextStyle(
                    fontSize = 24.sp
                ),
                color = Color(0xFFE1F5FE),
                modifier = Modifier.padding(16.dp)
            )
            Text(
                text = additionalInfo,
                style = TextStyle(
                    fontSize = 18.sp
                ),
                color = Color(0xFFE1F5FE),
                modifier = Modifier.padding(16.dp)
            )
            Text(
                text = alert,
                style = TextStyle(
                    fontSize = 18.sp
                ),
                color = Color(0xFFE1F5FE),
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}

@SuppressLint("UnrememberedMutableState")
@Composable
fun Map(
    modifier: Modifier, updateLocation: (String) -> Unit,
    updateMessage: (String) -> Unit,
    updateInfo: (String) -> Unit, updateAlert: (String) -> Unit
) {
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.Builder()
            .target(LatLng(9.53, 76.82))
            .zoom(15f)
            .build()
    }

    val currentLatLng = cameraPositionState.position.target
    LaunchedEffect(cameraPositionState.isMoving) {
        if (!cameraPositionState.isMoving) {
            Log.d(
                "Map",
                "Camera moved to: Lat: ${currentLatLng.latitude}, Lng: ${currentLatLng.longitude}"
            )
            sendLatLngToPythonAPI(
                currentLatLng.latitude, currentLatLng.longitude,
                updateMessage, updateInfo, updateAlert
            )
        }
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        var markerState by remember { mutableStateOf<MarkerState?>(null) }
        var locationName by remember { mutableStateOf("Unknown location") }
        markerState = MarkerState(position = LatLng(9.53, 76.82))
//        sendLatLngToPythonAPI(9.53, 76.82, updateMessage, updateInfo, updateAlert)
        getLocationName(LocalContext.current, 9.53, 76.82) { newLocation ->
            locationName = newLocation
            updateLocation(newLocation)
        }

        val context = LocalContext.current
        var debouncerHandler: Handler = Handler(Looper.getMainLooper())
        var debouncerRunnable: Runnable? = null
        val DEBOUNCE_DELAY = 500L

        GoogleMap(
            mapColorScheme = ComposeMapColorScheme.DARK,
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            onMapClick = { latLng ->
                debouncerRunnable?.let {
                    debouncerHandler.removeCallbacks(it)
                }

                debouncerRunnable = Runnable {
                    markerState = MarkerState(position = latLng)

                    sendLatLngToPythonAPI(
                        latLng.latitude,
                        latLng.longitude,
                        updateMessage,
                        updateInfo,
                        updateAlert
                    )

                    getLocationName(context, latLng.latitude, latLng.longitude) { newLocation ->
                        locationName = newLocation
                        updateLocation(newLocation)
                    }
                }

                debouncerHandler.postDelayed(debouncerRunnable!!, DEBOUNCE_DELAY)
            }
        ) {
            markerState?.let {
                Marker(
                    state = it,
                    title = "Click to remove",
                    onInfoWindowClick = {
                        markerState = null
                    }
                )
            }
        }

//        Text(
//            text = "Current Position:\nLat ${currentLatLng.latitude}\nLng ${currentLatLng.longitude}",
//            modifier = Modifier
//                .padding(16.dp)
//                .align(Alignment.BottomStart),
//            style = TextStyle(
//                fontSize = 16.sp,
//            ),
//            color = Color.Black
//        )
    }
}

fun getLocationName(
    context: android.content.Context,
    lat: Double, lon: Double,
    onLocationRetrieved: (String) -> Unit
) {
    val geocoder = Geocoder(context, Locale.getDefault())
    val addresses = geocoder.getFromLocation(lat, lon, 1)

    if (!addresses.isNullOrEmpty()) {
        val address = addresses[0]
        val location = "${address.locality.ifEmpty { "" }}, ${address.adminArea}, ${address.countryName}"
        Log.d("Location", "Location: $location")
        onLocationRetrieved(location)
    } else {
        onLocationRetrieved("Unknown location")
    }
}