package edu.temple.composablebrowser

import android.graphics.Bitmap
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.platform.app.InstrumentationRegistry
import java.io.File
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test

class BrowserTest {
    @get:Rule val compose = createAndroidComposeRule<MainActivity>()

    @Test
    fun goNormalizesUrlAndLoadsWikipediaInsideTheWebView() {
        compose.onNodeWithText("Go").assertIsDisplayed().assertIsNotEnabled()
        compose.onNode(hasSetTextAction()).performTextInput("wikipedia.com")
        compose.onNodeWithText("Go").performClick()
        compose.onNode(hasSetTextAction()).assertTextContains("https://wikipedia.com")
        compose.waitUntil(60000) {
            var loaded = false
            compose.runOnIdle {
                loaded = webView(compose.activity.window.decorView)?.title?.contains("Wikipedia", ignoreCase = true) == true
            }
            loaded
        }
        var loadedUrl: String? = null
        compose.runOnIdle { loadedUrl = webView(compose.activity.window.decorView)!!.url }
        val instrumentation = InstrumentationRegistry.getInstrumentation()
        val bitmap = compose.onRoot().captureToImage().asAndroidBitmap()
        File(instrumentation.targetContext.getExternalFilesDir(null), "browser.png").outputStream().use {
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, it)
        }
        bitmap.recycle()
        // Editing alone must not load another URL or undo a redirect.
        compose.onNode(hasSetTextAction()).performTextReplacement("example.com")
        compose.runOnIdle { assertEquals(loadedUrl, webView(compose.activity.window.decorView)!!.url) }
    }

    private fun webView(view: View): WebView? {
        if (view is WebView) return view
        if (view is ViewGroup) {
            for (index in 0 until view.childCount) webView(view.getChildAt(index))?.let { return it }
        }
        return null
    }
}
