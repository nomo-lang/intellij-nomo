package org.nomolang.intellij

import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.openapi.project.Project
import com.intellij.util.io.PathEnvironmentVariableUtil
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
        val name = if (System.getProperty("os.name").lowercase().contains("win")) {
            "nomo-lsp.exe"
        } else {
            "nomo-lsp"
        }
        val onPath = PathEnvironmentVariableUtil.findInPath(name)
        return onPath?.absolutePath ?: name
    }
}
