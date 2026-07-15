package org.nomolang.intellij

import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.execution.configurations.PathEnvironmentVariableUtil
import com.intellij.openapi.project.Project
import com.redhat.devtools.lsp4ij.server.OSProcessStreamConnectionProvider

/**
 * Launches the `nomo-lsp` language server as an external process and exposes its
 * stdio streams to LSP4IJ.
 */
class NomoLanguageServer(project: Project) : OSProcessStreamConnectionProvider() {
    init {
        val commandLine = GeneralCommandLine(resolveExecutable())
        commandLine.withWorkDirectory(project.basePath)
        super.setCommandLine(commandLine)
    }

    private fun resolveExecutable(): String {
        val name = languageServerExecutableName(System.getProperty("os.name"))
        val onPath = PathEnvironmentVariableUtil.findInPath(name)
        return onPath?.absolutePath ?: name
    }
}

internal fun languageServerExecutableName(osName: String): String =
    if (osName.lowercase().contains("win")) "nomo-lsp.exe" else "nomo-lsp"
