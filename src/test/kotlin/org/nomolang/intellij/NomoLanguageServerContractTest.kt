package org.nomolang.intellij

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class NomoLanguageServerContractTest {
    @Test
    fun `uses the platform language server executable name`() {
        assertEquals("nomo-lsp", languageServerExecutableName("Linux"))
        assertEquals("nomo-lsp", languageServerExecutableName("Mac OS X"))
        assertEquals("nomo-lsp.exe", languageServerExecutableName("Windows 11"))
    }

    @Test
    fun `plugin descriptor maps Nomo files to the nomo language server`() {
        val resource = javaClass.getResource("/META-INF/plugin.xml")
        assertNotNull(resource)
        val descriptor = resource.readText()

        assertTrue(descriptor.contains("factoryClass=\"org.nomolang.intellij.NomoLanguageServerFactory\""))
        assertTrue(descriptor.contains("languageMapping language=\"Nomo\" serverId=\"nomo\" languageId=\"nomo\""))
        assertTrue(descriptor.contains("extensions=\"nomo\""))
    }
}
