package org.nomolang.intellij

import com.intellij.psi.tree.IElementType

object NomoTokenTypes {
    val KEYWORD = token("KEYWORD")
    val TYPE = token("TYPE")
    val IDENTIFIER = token("IDENTIFIER")
    val STRING = token("STRING")
    val NUMBER = token("NUMBER")
    val COMMENT = token("COMMENT")
    val ATTRIBUTE = token("ATTRIBUTE")
    val OPERATOR = token("OPERATOR")
    val PUNCTUATION = token("PUNCTUATION")

    private fun token(debugName: String): IElementType = IElementType(debugName, NomoLanguage)
}
