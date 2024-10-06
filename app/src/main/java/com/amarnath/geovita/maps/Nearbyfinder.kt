package com.amarnath.geovita.maps

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.amarnath.geovita.R
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException

@Composable
fun NearbyUsersMain(p: PaddingValues) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Color.White,
            )
            .padding(p),
    ) {
                NearbyUsers()
    }
}

@SuppressLint("DefaultLocale")
@Composable
fun NearbyUsers() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(top = 32.dp, bottom = 16.dp)
                .background(color = Color(0xFFF3E5F5), shape = RoundedCornerShape(8.dp)),
            horizontalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.explore_nearby_24dp_e8eaed_fill0_wght400_grad0_opsz24),
                contentDescription = "Location Icon",
                modifier = Modifier.padding(vertical = 12.dp),
                colorFilter = androidx.compose.ui.graphics.ColorFilter.tint(Color(0xFF311B92))
            )
            Text(
                "Nearby Users",
                modifier = Modifier.padding(16.dp),
                color = Color(0xFF311B92),
                fontWeight = FontWeight.Bold,
                fontSize = 21.sp
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(bottom = 24.dp)
                .background(color = Color(0xFFEDE7F6), shape = RoundedCornerShape(8.dp)),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                "Find the count of users near you, in case of emergency",
                modifier = Modifier.padding(16.dp),
                color = Color(0xFF311B92),
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
        }

        Text(
            "Enter the Event ID to find nearby users",
            modifier = Modifier.padding(16.dp),
            color = Color(0xFF311B92),
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp
        )

        val eventId = remember { mutableStateOf("") }
        val showError = remember { mutableStateOf(false) }
        val errorMessage = remember { mutableStateOf("") }
        val scanOnGoing = remember { mutableStateOf(false) }
        val showSuccess = remember { mutableStateOf(false) }
        val nearbyCount = remember { mutableIntStateOf(0) }

        TextField(
            value = eventId.value, onValueChange = { eventId.value = it },
            modifier = Modifier
                .padding(horizontal = 32.dp, vertical = 6.dp)
                .fillMaxWidth(),
            label = { Text("Event ID") },
            shape = RoundedCornerShape(8.dp)
        )

        if (showError.value) {
            Text(
                errorMessage.value,
                modifier = Modifier.padding(16.dp),
                color = Color(0xFFD32F2F),
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
        }

        val sliderValue = remember { mutableFloatStateOf(0.01f) }

        Text(
            "Select the radius range to search for",
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .padding(top = 16.dp),
            color = Color(0xFF311B92),
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp
        )

        Slider(
            value = sliderValue.floatValue,
            onValueChange = { sliderValue.floatValue = it },
            valueRange = 0f..0.25f,
            steps = 15,
            modifier = Modifier
                .padding(16.dp)
                .padding(horizontal = 24.dp),
            colors = androidx.compose.material3.SliderDefaults.colors(
                thumbColor = Color(0xFF311B92),
                activeTrackColor = Color(0xFF311B92),
                inactiveTrackColor = Color(0xFFC5CAE9)
            )
        )

        Text(
            "Radius: ${String.format("%.0f", sliderValue.floatValue * 100)} km",
            modifier = Modifier.padding(horizontal = 16.dp),
            color = Color(0xFF311B92),
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp
        )

        ElevatedButton(
            onClick = {
                showSuccess.value = false
                if (eventId.value.isEmpty()) {
                    showError.value = true
                    errorMessage.value = "Event ID cannot be empty"
                } else {
                    Thread.sleep(3000)
                    nearbyCount.intValue = 5
                }
            },
            modifier = Modifier.padding(24.dp),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (scanOnGoing.value) Color(0xFF7E57C2) else
                    Color(
                        0xFF9575CD
                    ),
                contentColor = Color.White
            ),
            border = BorderStroke(1.dp, Color(0xFF311B92))
        ) {
            Text(
                if (scanOnGoing.value) ".... Nearby Users" else "Scan Nearby Users",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = Color.White,
                modifier = Modifier.padding(8.dp)
            )
        }

        Box(
            modifier = Modifier
                .padding(horizontal = 12.dp)
                .background(color = Color(0xFFD1C4E9), shape = RoundedCornerShape(8.dp)),
            contentAlignment = Alignment.Center
        ) {
            Column {
                if (showSuccess.value)
                    Text(
                        "Event-ID: ${eventId.value} | Event-Title: Event 1",
                        modifier = Modifier
                            .padding(horizontal = 50.dp, vertical = 15.dp)
                            .align(Alignment.CenterHorizontally),
                        color = Color(0xFF311B92),
                        fontWeight = FontWeight.Bold,
                        fontSize = 17.sp
                    )
                Text(
                    nearbyCount.intValue.toString() + " - U",
                    fontWeight = FontWeight.Bold,
                    fontSize = 28.sp,
                    color = Color(0x706200EA),
                    modifier = Modifier
                        .padding(horizontal = 80.dp, vertical = 15.dp)
                        .align(Alignment.CenterHorizontally)
                )
            }
        }

        Text(
            "Note: The count of users is approximate and may vary",
            modifier = Modifier.padding(32.dp),
            color = Color(0xFF311B92),
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp
        )
    }
}
