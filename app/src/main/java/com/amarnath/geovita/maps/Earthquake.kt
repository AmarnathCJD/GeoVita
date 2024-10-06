package com.amarnath.geovita.maps

import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
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
import com.arcgismaps.ApiKey
import com.arcgismaps.ArcGISEnvironment
import com.arcgismaps.mapping.ArcGISMap
import com.arcgismaps.mapping.view.DrawStatus
import com.arcgismaps.mapping.view.MapViewInteractionOptions
import com.arcgismaps.mapping.view.ViewLabelProperties
import com.arcgismaps.toolkit.geoviewcompose.MapView

const val ArqAPIKey = "AAPK0844337409204e97adf6606573bb4065LG2XsG7ErJOEo7kuicBPdesFgnbH7g7eDTxIwQQ_sxv2AJeklp_TEoJl4Uzf3BLL"

@Composable
fun EarthQuake(p: PaddingValues) {
    Column(
        modifier = Modifier
            .padding(p)
            .padding(horizontal = 4.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        EarthQuakeMetadata()
    }
}

@Composable
fun EarthQuakeMetadata() {
    val data = remember { mutableStateOf("EarthQuake Live Updates") }

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

            LottieLoading(R.raw.quake)

            Spacer(modifier = Modifier.size(16.dp))
            Text(
                text = "Events by Magnitude",
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
                        text = "Mag 7.5 or >",
                        style = androidx.compose.ui.text.TextStyle(fontSize = 16.sp),
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.size(16.dp))
                    Canvas(modifier = Modifier.size(20.dp), onDraw = {
                        drawCircle(color = Color.Black)
                    })
                }
                Row (
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Mag 6 to 7.5",
                        style = androidx.compose.ui.text.TextStyle(fontSize = 16.sp),
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.size(8.dp))
                    Canvas(modifier = Modifier.size(15.dp), onDraw = {
                        drawCircle(color = Color.Red)
                    })
                }
                Row (
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Mag 4.5 to 6",
                        style = androidx.compose.ui.text.TextStyle(fontSize = 16.sp),
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.size(8.dp))
                    Canvas(modifier = Modifier.size(12.dp), onDraw = {
                        drawCircle(color = Color.Yellow)
                    })
                }
                Row (
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Mag 3 to 4.5",
                        style = androidx.compose.ui.text.TextStyle(fontSize = 16.sp),
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.size(1.dp))
                    Canvas(modifier = Modifier.size(8.dp), onDraw = {
                        drawCircle(color = Color.Blue)
                    })
                }
                Row (
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Mag < 3",
                        style = androidx.compose.ui.text.TextStyle(fontSize = 16.sp),
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.size(22.dp))
                    Canvas(modifier = Modifier.size(6.dp), onDraw = {
                        drawCircle(color = Color.Gray)
                    })
                }
            }
        }
    }

    ArqGIS("https://www.arcgis.com/apps/mapviewer/index.html?webmap=249bbba191704b0088d04f81a6405912")
}

@Composable
fun ArqGIS(url: String) {
    ElevatedCard(
        modifier = Modifier.padding(vertical = 12.dp, horizontal = 14.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent,
        ),
        shape = RoundedCornerShape(4.dp)
    ) {
        Column {
            ArcGISEnvironment.apiKey =
                ApiKey.create(ArqAPIKey)

            val map = ArcGISMap(url)

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(480.dp)
            ) {
                val loadingFinished = remember { mutableStateOf(false) }
                if (!loadingFinished.value) {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        color = Color.White
                    )
                }
                MapView(
                    modifier = Modifier.fillMaxSize(),
                    arcGISMap = map,
                    isAttributionBarVisible = false,
                    onDoubleTap = { x ->
                        println("double: $x")
                    },
                    onLongPress = { x ->
                        //map.
                    },
                    viewLabelProperties = ViewLabelProperties(
                        animationEnabled = true,
                        labelingEnabled = true,
                    ),
                    mapViewInteractionOptions = MapViewInteractionOptions(
                        isMagnifierEnabled = true,
                        isRotateEnabled = false, isZoomEnabled = true
                    ),
                    onDrawStatusChanged = { status ->
                        if (status == DrawStatus.Completed) {
                            loadingFinished.value = true
                        }
                    },
                )
            }
        }
    }
}