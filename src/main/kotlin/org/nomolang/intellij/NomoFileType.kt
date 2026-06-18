package org.nomolang.intellij

import com.intellij.openapi.fileTypes.LanguageFileType
import javax.swing.Icon

object NomoFileType : LanguageFileType(NomoLanguage) {
    override fun getName(): String = "Nomo"

    override fun getDescription(): String = "Nomo source file"

    override fun getDefaultExtension(): String = "nomo"

    override fun getIcon(): Icon = NomoIcons.FILE
}
