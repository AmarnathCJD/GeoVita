package com.amarnath.geovita

import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.amarnath.geovita.maps.AQI
import com.amarnath.geovita.maps.AccountPage
import com.amarnath.geovita.maps.Calm
import com.amarnath.geovita.maps.EarthQuake
import com.amarnath.geovita.maps.Emergency
import com.amarnath.geovita.maps.MapViewMain
import com.amarnath.geovita.maps.NearbyUsersMain
import com.amarnath.geovita.maps.Precip
import com.amarnath.geovita.maps.UVRays
import com.amarnath.geovita.maps.WaterQuality
import com.amarnath.geovita.maps.Weather_warning
import com.amarnath.geovita.maps.WildFire
import com.amarnath.geovita.ui.theme.GeoVitaTheme

val appName = "GeoVita"

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GeoVitaTheme {
                val nav = rememberNavController()
                Scaffold(modifier = Modifier.fillMaxSize(),
                    containerColor = Color(0xFF1C1C26),
                    topBar = { TopAppBar() },
                    bottomBar = { BottomAppBar(nav) }
                ) { innerPadding ->
                    NavHost(navController = nav, startDestination = "Home") {
                        composable(
                            "Home",
                        ) {
                            HomePage(innerPadding, nav)
                        }
                        composable(
                            "EarthQuake",
                        ) {
                            EarthQuake(innerPadding)
                        }
                        composable(
                            "WildFire",
                        ) {
                            WildFire(innerPadding)
                        }
                        composable(
                            "AQI",
                        ) {
                            AQI(innerPadding)
                        }
                        composable(
                            "Emergency",
                        ) {
                            Emergency(innerPadding)
                        }
                        composable(
                            "Precipitation",
                        ) {
                            Precip(innerPadding)
                        }
                        composable(
                            "Map",
                        ) {
                            MapViewMain(innerPadding)
                        }
                        composable(
                            "WaterQuality",
                        ) {
                            WaterQuality(innerPadding)
                        }
                        composable(
                            "UVIndex",
                        ) {
                            UVRays(innerPadding)
                        }
                        composable(
                            "Calamity",
                        ) {
                            Weather_warning()
                        }
                        composable(
                            "Nearbyfinder",
                        ) {
                            NearbyUsersMain(innerPadding)
                        }
                        composable(
                            "Account"
                        ) {
                            AccountPage(innerPadding)
                        }
                        composable(
                            "Ambulancetraffik"
                        ) {
                            Column (
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(Color(0xFF1C1C26))
                                    .padding(innerPadding),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                ElevatedCard(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(Color(0xFF1C1C26))
                                        .padding(innerPadding),

                                ) {
                                    Text(
                                        text = "`TODO: Ambulance Traffic`",
                                        style = MaterialTheme.typography.bodyLarge.copy(
                                            fontSize = 18.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFF98B24C),
                                            fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                                        ),
                                        modifier = Modifier.padding(16.dp)
                                            .align(Alignment.CenterHorizontally)
                                    )
                                }
                            }
                        }
                        composable(
                            "5GCoverage"
                        ) {
                            Column (
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(Color(0xFF1C1C26))
                                    .padding(innerPadding),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                ElevatedCard(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(Color(0xFF1C1C26))
                                        .padding(innerPadding),

                                    ) {
                                    Text(
                                        text = "`TODO: 5G Coverage`",
                                        style = MaterialTheme.typography.bodyLarge.copy(
                                            fontSize = 18.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFF98B24C),
                                            fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                                        ),
                                        modifier = Modifier.padding(16.dp)
                                            .align(Alignment.CenterHorizontally)
                                    )
                                }
                            }
                        }
                        composable(
                            "Food"
                        ) {
                            Column (
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(Color(0xFF1C1C26))
                                    .padding(innerPadding),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                ElevatedCard(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(Color(0xFF1C1C26))
                                        .padding(innerPadding),

                                    ) {
                                    Text(
                                        text = "`TODO: Food Desert`",
                                        style = MaterialTheme.typography.bodyLarge.copy(
                                            fontSize = 18.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFF98B24C),
                                            fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                                        ),
                                        modifier = Modifier.padding(16.dp)
                                            .align(Alignment.CenterHorizontally)
                                    )
                                }
                            }
                        }
                        composable(
                            "CalamityZones"
                        ) {
                            Calm(innerPadding)
                        }
                    }
                }
            }
        }

        WindowCompat.setDecorFitsSystemWindows(window, false)
        WindowInsetsControllerCompat(window, window.decorView).let { controller ->
            controller.hide(WindowInsetsCompat.Type.systemBars())
            controller.systemBarsBehavior =
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        window.attributes.layoutInDisplayCutoutMode =
            WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
    }
}

@Composable
fun TopAppBar() {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF353C3D),
            contentColor = Color(0xFFE8EAED)
        )
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = appName,
                modifier = Modifier.padding(vertical = 8.dp),
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontSize = 19.sp,
                    fontWeight = FontWeight.Bold
                )
            )
            Icon(
                painter = painterResource(id = R.drawable.travel_explore_24dp_e8eaed_fill0_wght400_grad0_opsz24),
                contentDescription = "Menu",
                tint = Color(0xFFE8EAED),
                modifier = Modifier
                    .padding(vertical = 16.dp)
                    .padding(start = 4.dp)
                    .size(24.dp)
            )
        }
    }
}

@Composable
fun BottomAppBar(nav: NavController) {
    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .height(70.dp)
            .padding(bottom = 0.dp)
            .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF201A21))
                .alpha(1f)
                .clip(RoundedCornerShape(20.dp))
                .border(
                    1.dp,
                    Color(0xFF51516C),
                    RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
                )
        ) {
            BottomIconItem(
                imageRes = R.drawable.air_24dp_e8eaed_fill0_wght400_grad0_opsz24,
                color = Color(0xFFE8EAED),
                name = "Calamity",
                nav = nav
            )
            BottomIconItem(
                imageRes = R.drawable.home_24dp_e8eaed_fill0_wght400_grad0_opsz24,
                color = Color(0xFFE8EAED),
                name = "Home",
                nav = nav
            )
            BottomIconItem(
                imageRes = R.drawable.location_on_24dp_e8eaed_fill0_wght400_grad0_opsz24,
                color = Color(0xFFE8EAED),
                name = "Map",
                nav = nav
            )
            BottomIconItem(
                imageRes = R.drawable.account_circle_24dp_e8eaed_fill0_wght400_grad0_opsz24,
                color = Color(0xFFE8EAED),
                name = "Account",
                nav = nav
            )
        }
    }
}