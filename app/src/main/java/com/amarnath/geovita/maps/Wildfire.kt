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
fun WildFire(p: PaddingValues) {
    Column(
        modifier = Modifier
            .padding(p)
            .padding(horizontal = 4.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        WildFireMetadata()
    }
}

@Composable
fun WildFireMetadata() {
    val data = remember { mutableStateOf("WildFire & Thermal Hotspots") }

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

            LottieLoading(R.raw.fire)

            Spacer(modifier = Modifier.size(16.dp))
            Text(
                text = "Satellite (VIIRS) Thermal Hotspots and Fire Activity",
                style = androidx.compose.ui.text.TextStyle(fontSize = 13.sp),
                color = Color(0xFF9EC2F6),
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "From- Visible Infrared Imaging Radiometer Suite",
                style = androidx.compose.ui.text.TextStyle(fontSize = 13.sp),
                color = Color(0xFFDA5A94),
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.size(8.dp))
            Text(
                text = "Fire Radiative Power (MW)",
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
                        text = "> 750 MW",
                        style = androidx.compose.ui.text.TextStyle(fontSize = 16.sp),
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.size(16.dp))
                    Canvas(modifier = Modifier.size(20.dp), onDraw = {
                        drawCircle(color = Color.Yellow)
                        drawCircle(color = Color.Red, radius = 20f)
                    })
                }
                Row (
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "300-749 MW",
                        style = androidx.compose.ui.text.TextStyle(fontSize = 16.sp),
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.size(8.dp))
                    Canvas(modifier = Modifier.size(15.dp), onDraw = {
                        drawCircle(color = Color.Yellow)
                        drawCircle(color = Color.Red, radius = 15f)
                    })
                }
                Row (
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "100-299 MW",
                        style = androidx.compose.ui.text.TextStyle(fontSize = 16.sp),
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.size(8.dp))
                    Canvas(modifier = Modifier.size(12.dp), onDraw = {
                        drawCircle(color = Color.Yellow)
                        drawCircle(color = Color.Red, radius = 12f)
                    })
                }
                Row (
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "10-99 MW",
                        style = androidx.compose.ui.text.TextStyle(fontSize = 16.sp),
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.size(8.dp))
                    Canvas(modifier = Modifier.size(8.dp), onDraw = {
                        drawCircle(color = Color.Yellow)
                        drawCircle(color = Color.Red, radius = 8f)
                    })
                }
                Row (
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "0-9 MW",
                        style = androidx.compose.ui.text.TextStyle(fontSize = 16.sp),
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.size(0.dp))
                    Canvas(modifier = Modifier.size(6.dp), onDraw = {
                        drawCircle(color = Color.Yellow)
                        drawCircle(color = Color.Red, radius = 6f)
                    })
                }
            }
        }
    }

    ArqGIS("https://www.arcgis.com/apps/mapviewer/index.html?webmap=65db720520f147a59165cdf7609afea2")
}