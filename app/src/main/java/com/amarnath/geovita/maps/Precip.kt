package com.amarnath.geovita.maps

import android.annotation.SuppressLint
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.zIndex


@SuppressLint("SetJavaScriptEnabled")
@Composable
fun Precip(nav: PaddingValues) {
    val cfg = LocalConfiguration.current
    Column(
        modifier = Modifier
            .background(
                color = Color(0xFF313030)
            )
            .padding(
                top = 0.dp,
            )
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(
                    cfg.screenHeightDp.dp
                ),
        ) {
            Box(
                modifier = Modifier
            ) {
                ElevatedCard(
                    modifier = Modifier
                        .padding(horizontal = 12.dp)
                        .zIndex(2f)
                        .padding(top = 60.dp)
                        .align(Alignment.TopCenter)
                    ,
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFF353C3D),
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Precipitation Forecast",
                            style = androidx.compose.ui.text.TextStyle(fontSize = 14.sp),
                            color = Color(0xFF9EC2F6),
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                AndroidView(factory = {
                    WebView(it).apply {
                        layoutParams = ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT
                        )
                        webViewClient = WebViewClient()
                        settings.javaScriptEnabled = true
                        loadUrl("https://api-dmo-ug5m.vercel.app/")
                    }
                }, update = {
                    it.loadUrl("https://api-dmo-ug5m.vercel.app/")
                })
            }
        }
    }
}