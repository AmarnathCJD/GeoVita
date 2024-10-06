package com.amarnath.geovita.maps

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.amarnath.geovita.LottieLoading
import com.amarnath.geovita.R

@Composable
fun AQI(p: PaddingValues) {
    Column(
        modifier = Modifier
            .padding(p)
            .padding(horizontal = 4.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        AQIMetadata()
    }
}

@Composable
fun AQIMetadata() {
    val data = remember { mutableStateOf("Air Quality Index") }

    ElevatedCard(
        modifier = Modifier.padding(horizontal = 12.dp)
            .fillMaxWidth()
        ,
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF353C3D),
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = data.value,
                style = androidx.compose.ui.text.TextStyle(fontSize = 16.sp),
                color = Color.White,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.size(16.dp))
            Text(
                text = "Map of Air Quality",
                style = androidx.compose.ui.text.TextStyle(fontSize = 13.sp),
                color = Color(0xFFDA5A94),
                fontWeight = FontWeight.Bold
            )
            LottieLoading(R.raw.aqi)
            Spacer(modifier = Modifier.size(8.dp))
            Text(
                text = "Ozone and PM (PM2.5 and PM10) - Current",
                style = androidx.compose.ui.text.TextStyle(fontSize = 13.sp),
                color = Color(0xFF9EC2F6),
                fontWeight = FontWeight.Bold
            )
            Column (
                modifier = Modifier.padding(8.dp),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ){
                Row (
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Good",
                        style = androidx.compose.ui.text.TextStyle(fontSize = 16.sp),
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(197.dp))
                    Canvas(modifier = Modifier.size(20.dp), onDraw = {
                        drawRect(color = Color.Green)
                    })
                }
                Row (
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Moderate",
                        style = androidx.compose.ui.text.TextStyle(fontSize = 16.sp),
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(150.dp))
                    Canvas(modifier = Modifier.size(20.dp), onDraw = {
                        drawRect(color = Color.Yellow)
                    })
                }
                Row (
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = "Unhealthy for Sensitive Groups",
                        style = androidx.compose.ui.text.TextStyle(fontSize = 16.sp),
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(0.dp))
                    Canvas(modifier = Modifier.size(20.dp), onDraw = {
                        drawRect(color = Color(0xFFFFA500))
                    })
                }
                Row (
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Unhealthy",
                        style = androidx.compose.ui.text.TextStyle(fontSize = 16.sp),
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(148.dp))
                    Canvas(modifier = Modifier.size(20.dp), onDraw = {
                        drawRect(color = Color.Red)
                    })
                }
                Row (
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Very Unhealthy",
                        style = androidx.compose.ui.text.TextStyle(fontSize = 16.sp),
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(111.dp))
                    Canvas(modifier = Modifier.size(20.dp), onDraw = {
                        drawRect(color = Color(0xFF8B008B))
                    })
                }
                Row (
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Hazardous",
                        style = androidx.compose.ui.text.TextStyle(fontSize = 16.sp),
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(143.dp))
                    Canvas(modifier = Modifier.size(20.dp), onDraw = {
                        drawRect(color = Color(0xFF754119))
                    })
                }
            }
        }
    }

    ArqGIS("https://www.arcgis.com/apps/mapviewer/index.html?webmap=a28d5ccc504c4b129d4b42cad48c8f96")
}