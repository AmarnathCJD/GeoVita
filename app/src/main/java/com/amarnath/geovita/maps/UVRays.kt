package com.amarnath.geovita.maps

import android.annotation.SuppressLint
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.amarnath.geovita.LottieLoading
import com.amarnath.geovita.R

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun UVRays(nav: PaddingValues) {
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
                        .padding(horizontal = 0.dp)
                        .zIndex(1f)
                        .padding(top = 0.dp)
                        .align(Alignment.TopCenter)
                    ,
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFF111B1C),
                    ),
                    shape = RoundedCornerShape(16.dp, 16.dp, 0.dp, 0.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .padding(vertical = 16.dp)
                            .padding(top = 60.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "UV Index",
                            style = androidx.compose.ui.text.TextStyle(fontSize = 15.sp),
                            color = Color(0xFF9EC2F6),
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.size(16.dp))
                        LottieLoading(R.raw.fire, 30)
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
                        loadUrl("https://www.meteoblue.com/en/weather/maps#coords=3.6/22.29/82.95&map=uvIndex~daily~auto~sfc~none")
                    }
                }, update = {
                    it.loadUrl("https://www.meteoblue.com/en/weather/maps#coords=3.6/22.29/82.95&map=uvIndex~daily~auto~sfc~none")
                })
            }
        }
    }
}