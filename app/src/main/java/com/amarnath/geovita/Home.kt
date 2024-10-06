package com.amarnath.geovita

import android.Manifest
import android.content.pm.PackageManager
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.navigation.NavController
import com.arcgismaps.ApiKey
import com.arcgismaps.ArcGISEnvironment
import com.arcgismaps.geometry.Point
import com.arcgismaps.geometry.SpatialReference
import com.arcgismaps.mapping.ArcGISMap
import com.arcgismaps.mapping.BasemapStyle
import com.arcgismaps.mapping.Viewpoint
import com.arcgismaps.mapping.view.DrawStatus
import com.arcgismaps.mapping.view.Graphic
import com.arcgismaps.mapping.view.MapViewInteractionOptions
import com.arcgismaps.mapping.view.ViewLabelProperties
import com.arcgismaps.tasks.networkanalysis.Facility
import com.arcgismaps.toolkit.geoviewcompose.MapView
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource

val loggedInUser = mutableStateOf("`userName`")

@Composable
fun HomePage(p: PaddingValues, nav: NavController) {
    val ctx = androidx.compose.ui.platform.LocalContext.current
    val locationPair = remember { mutableStateOf(Pair(0.0, 0.0)) }

    Column(
        modifier = Modifier
            .padding(p)
            .padding(horizontal = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = androidx.compose.foundation.layout.Arrangement.Center
    ) {
        ElevatedCard(
            modifier = Modifier.padding(vertical = 4.dp, horizontal = 8.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFF353C3D),
            )
        ) {
            Column(
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Hey, ${loggedInUser.value} !",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFFFD600)
                )
                Spacer(modifier = Modifier.padding(8.dp))
                Text(
                    text = "Welcome to GeoVita",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF72AF4B)
                )
                Text(
                    text = "Mapping Life, Unveiling Insights.",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFC3DC79)
                )
                Text(
                    text = "Powered by ArcGIS",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFC3DC79),
                )

                LottieLoading(R.raw.anim)
            }
        }


        val menuItems = listOf(
            "Earth Quake",
            "Precipitation",
            "Wildfire",
            "Food Desert",
            "Emergency",
            "Air Quality",
            "Water Quality",
            "Calamity Zones",
            "UV Index",
            "5G Coverage",
            "Nearby Finder",
            "Ambulance Traffik"
        )
        val menuItemsIcons = listOf(
            R.drawable.earthquake_24dp_e8eaed_fill0_wght400_grad0_opsz24,
            R.drawable.rainy_24dp_e8eaed_fill0_wght400_grad0_opsz24,
            R.drawable.emergency_heat_24dp_e8eaed_fill0_wght400_grad0_opsz24,
            R.drawable.fastfood_24dp_e8eaed_fill0_wght400_grad0_opsz24,
            R.drawable.emergency_24dp_e8eaed_fill0_wght400_grad0_opsz24,
            R.drawable.air_24dp_e8eaed_fill0_wght400_grad0_opsz24,
            R.drawable.water_24dp_e8eaed_fill0_wght400_grad0_opsz24,
            R.drawable.landslide_24dp_e8eaed_fill0_wght400_grad0_opsz24,
            R.drawable.sunny_snowing_24dp_e8eaed_fill0_wght400_grad0_opsz24,
            R.drawable.wifi_24dp_e8eaed_fill0_wght400_grad0_opsz24,
            R.drawable.explore_nearby_24dp_e8eaed_fill0_wght400_grad0_opsz24,
            R.drawable.ambulance_24dp_e8eaed_fill0_wght400_grad0_opsz24
        )

        val menuItemsColors = listOf(
            Color(0xFF4CAF50),
            Color(0xFF3468BB),
            Color(0xFFFFA726),
            Color(0xFFFF5722),
            Color(0xFFD32F2F),
            Color(0xFF4CAF50),
            Color(0xFF03A9F4),
            Color(0xFF795548),
            Color(0xFFFFEB3B),
            Color(0xFF8BC34A),
            Color(0xFF9E9E9E),
            Color(0xFFF44336)
        )

        val menuItemsNav =  listOf(
            "EarthQuake",
            "Precipitation",
            "Wildfire",
            "Food",
            "Emergency",
            "AQI",
            "WaterQuality",
            "CalamityZones",
            "UVIndex",
            "5GCoverage",
            "NearbyFinder",
            "AmbulanceTraffik"
        )

        ElevatedCard(
            modifier = Modifier
                .padding(vertical = 2.dp, horizontal = 8.dp)
                .verticalScroll(rememberScrollState()),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFF353C3D),
            ),
            shape = RoundedCornerShape(4.dp)
        ) {
            val splitByThree = menuItems.chunked(3)
            Column(
                modifier = Modifier.padding(
                    4.dp
                )
                    .fillMaxWidth()
            ) {
                splitByThree.forEach { row ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = androidx.compose.foundation.layout.Arrangement.Center
                    ) {
                        row.forEachIndexed { _, item ->
                            LazyItem(
                                item,
                                menuItemsIcons[menuItems.indexOf(item)],
                                menuItemsColors[menuItems.indexOf(item)],
                                menuItemsNav[menuItems.indexOf(item)],
                                nav
                            )
                        }
                    }
                }
            }
        }

        ElevatedCard(
            modifier = Modifier.padding(vertical = 4.dp, horizontal = 10.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFF353C3D),
            ),
            shape = RoundedCornerShape(4.dp)
        ) {
            val isLoading = remember { mutableStateOf(true) }
            Column {
                ArcGISEnvironment.apiKey =
                    ApiKey.create("AAPK0844337409204e97adf6606573bb4065LG2XsG7ErJOEo7kuicBPdesFgnbH7g7eDTxIwQQ_sxv2AJeklp_TEoJl4Uzf3BLL")

                val map = ArcGISMap(BasemapStyle.ArcGISDarkGray)
                if (locationPair.value.first == 0.0 && locationPair.value.second == 0.0) {
                    val accuracy = Priority.PRIORITY_HIGH_ACCURACY
                    val client =
                        LocationServices.getFusedLocationProviderClient(ctx)

                    if (ActivityCompat.checkSelfPermission(
                            ctx,
                            Manifest.permission.ACCESS_FINE_LOCATION
                        ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                            ctx,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                        ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                            ctx,
                            Manifest.permission.POST_NOTIFICATIONS
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        RequestLocationPermission(
                            onPermissionGranted = {},
                            onPermissionDenied = {},
                            onPermissionsRevoked = {}
                        )
                    }

                    client.getCurrentLocation(accuracy, CancellationTokenSource().token)
                        .addOnSuccessListener { loc ->
                            loc?.let {
                                locationPair.value = Pair(loc.latitude, loc.longitude)
                                map.initialViewpoint =
                                    Viewpoint(loc.latitude, loc.longitude, 72000.0, null);
                            }
                        }
                        .addOnFailureListener {
                            Log.e("Location", "Failed to get location.")
                        }
                }

                val facilitiesList = listOf(
                    Facility(Point(-1.3042129900625112E7, 3860127.9479775648, SpatialReference.webMercator())),
                    Facility(Point(-1.3042193400557665E7, 3862448.873041752, SpatialReference.webMercator())),
                    Facility(Point(-1.3046882875518233E7, 3862704.9896770366, SpatialReference.webMercator())),
                    Facility(Point(-1.3040539754780494E7, 3862924.5938606677, SpatialReference.webMercator())),
                    Facility(Point(-1.3042571225655518E7, 3858981.773018156, SpatialReference.webMercator())),
                    Facility(Point(-1.3039784633928463E7, 3856692.5980474586, SpatialReference.webMercator())),
                    Facility(Point(-1.3049023883956768E7, 3861993.789732541, SpatialReference.webMercator()))
                )

                facilitiesList.forEach {
                    val gpx = Graphic(
                        geometry = it.geometry
                    )

                    // map.operationalLayers.add(Layer(gpx))
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp)
                ) {
                    if (isLoading.value) {
                        LottieLoading(R.raw.mapload, 280)
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
                                isLoading.value = false
                            }
                        },
                    )
                }
            }
        }
    }
}

@Composable
fun LazyItem(item: String, i: Int, color: Color, s: String, nav: NavController) {
    ElevatedCard(
        modifier = Modifier.padding(4.dp)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = {
                    nav.navigate(s)
                }
            ),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF293031),
        ),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(8.dp)
                .height(100.dp)
                .width(100.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = androidx.compose.foundation.layout.Arrangement.Center
        ) {
            Icon(
                painter = painterResource(id = i),
                contentDescription = item,
                tint = color,
                modifier = Modifier.size(48.dp)
            )
            Text(
                text = item,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFF5F5F5),
                modifier = Modifier.align(
                    Alignment.CenterHorizontally
                )
            )
        }
    }
}