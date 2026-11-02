package edu.temple.composablebrowser

import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { MaterialTheme { Surface { BrowserScreen() } } }
    }
}

fun fixUrl(input: String): String {
    val url = input.trim()
    return if (url.isEmpty() || url.startsWith("https://", ignoreCase = true)
        || url.startsWith("http://", ignoreCase = true)) url else "https://$url"
}

@Composable
fun BrowserScreen() {
    var text by remember { mutableStateOf("") }
    var urlToLoad by remember { mutableStateOf("") }
    val keyboard = LocalSoftwareKeyboardController.current

    Column(Modifier.fillMaxSize().safeDrawingPadding().imePadding().padding(8.dp)) {
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            TextField(
                value = text,
                onValueChange = { text = it },
                label = { Text(stringResource(R.string.enter_url)) },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Uri),
                modifier = Modifier.weight(1f)
            )
            Spacer(Modifier.width(8.dp))
            Button(onClick = {
                text = fixUrl(text)
                urlToLoad = text
                keyboard?.hide()
            }, enabled = text.isNotBlank()) { Text(stringResource(R.string.go)) }
        }
        AndroidView(
            factory = { context ->
                WebView(context).apply {
                    webViewClient = WebViewClient()
                    settings.javaScriptEnabled = true
                }
            },
            update = { webView ->
                // Remember the request so typing cannot reload a redirected page.
                if (urlToLoad.isNotEmpty() && webView.tag != urlToLoad) {
                    webView.tag = urlToLoad
                    webView.loadUrl(urlToLoad)
                }
            },
            onRelease = { it.destroy() },
            modifier = Modifier.fillMaxWidth().weight(1f).padding(top = 8.dp)
        )
    }
}
