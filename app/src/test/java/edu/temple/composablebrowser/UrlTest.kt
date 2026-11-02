package edu.temple.composablebrowser

import org.junit.Assert.assertEquals
import org.junit.Test

class UrlTest {
    @Test
    fun normalizesAddressesWithoutChangingExistingHttpSchemes() {
        mapOf(
            "wikipedia.com" to "https://wikipedia.com",
            "  wikipedia.com/wiki/Android  " to "https://wikipedia.com/wiki/Android",
            "https://example.com" to "https://example.com",
            "http://example.com" to "http://example.com",
            "HTTPS://example.com" to "HTTPS://example.com",
            "   " to ""
        ).forEach { (input, expected) -> assertEquals(expected, fixUrl(input)) }
    }
}
