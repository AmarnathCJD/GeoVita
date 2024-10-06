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
fun Calm(p: PaddingValues) {
    Column(
        modifier = Modifier
            .padding(p)
            .padding(horizontal = 4.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CalmMetadata()
    }
}

@Composable
fun CalmMetadata() {
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
                text = "Calamity Zone Classification",
                style = androidx.compose.ui.text.TextStyle(fontSize = 13.sp),
                color = Color(0xFFDA5A94),
                fontWeight = FontWeight.Bold
            )
        }
    }

    ArqGIS("https://www.arcgis.com/apps/mapviewer/index.html?webmap=89a25b4992614e6494a2d07224716ebe")
}