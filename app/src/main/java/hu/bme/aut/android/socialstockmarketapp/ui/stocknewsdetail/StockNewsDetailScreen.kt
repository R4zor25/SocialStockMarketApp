package hu.bme.aut.android.socialstockmarketapp.ui.stocknewsdetail

import android.graphics.Bitmap
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.LinearLayout
import androidx.compose.foundation.layout.*
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavHostController
import kotlin.math.roundToInt

@Composable
fun StockNewsDetailScreen(navController: NavHostController, newsUrl: String) {
    val url = remember { mutableStateOf(newsUrl) }
    val visibility = remember { mutableStateOf(false) }
    val progress = remember { mutableStateOf(0.0F) }

    Box(
        modifier = Modifier.fillMaxSize(),
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier
                    .padding(8.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically

            ) {

                if (visibility.value) {
                    CircularProgressIndicator(
                        color = Color(0xFFE30022)
                    )
                    Text(
                        text = "${progress.value.roundToInt()}%",
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Box(
                modifier = Modifier
                    .weight(2F)
            ) {
                AndroidView(factory = { context ->
                    WebView(context).apply {
                        layoutParams = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.MATCH_PARENT
                        )
                        settings.javaScriptEnabled = true

                        webViewClient = object : WebViewClient() {
                            override fun onPageStarted(
                                view: WebView, url: String,
                                favicon: Bitmap?
                            ) {
                                visibility.value = true
                            }

                            override fun onPageFinished(
                                view: WebView, url: String
                            ) {
                                visibility.value = false
                            }
                        }

                        // Set web view chrome client
                        webChromeClient = object : WebChromeClient() {
                            override fun onProgressChanged(
                                view: WebView, newProgress: Int
                            ) {
                                progress.value = newProgress.toFloat()
                            }
                        }

                        loadUrl(url.value)
                    }
                }, update = {
                    it.loadUrl(url.value)
                })
            }
        }
    }
}