package org.nomolang.intellij

import com.intellij.lexer.Lexer
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors
import com.intellij.openapi.editor.HighlighterColors
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase
import com.intellij.psi.TokenType
import com.intellij.psi.tree.IElementType

class NomoSyntaxHighlighter : SyntaxHighlighterBase() {
    override fun getHighlightingLexer(): Lexer = NomoLexer()

    override fun getTokenHighlights(tokenType: IElementType): Array<TextAttributesKey> =
        pack(
            when (tokenType) {
                NomoTokenTypes.KEYWORD -> KEYWORD
                NomoTokenTypes.TYPE -> TYPE
                NomoTokenTypes.STRING -> STRING
                NomoTokenTypes.NUMBER -> NUMBER
                NomoTokenTypes.COMMENT -> COMMENT
                NomoTokenTypes.OPERATOR -> OPERATOR
                NomoTokenTypes.PUNCTUATION -> PUNCTUATION
                TokenType.BAD_CHARACTER -> BAD_CHARACTER
                else -> null
            },
        )

    companion object {
        val KEYWORD: TextAttributesKey =
            TextAttributesKey.createTextAttributesKey("NOMO_KEYWORD", DefaultLanguageHighlighterColors.KEYWORD)
        val TYPE: TextAttributesKey =
            TextAttributesKey.createTextAttributesKey("NOMO_TYPE", DefaultLanguageHighlighterColors.CLASS_NAME)
        val STRING: TextAttributesKey =
            TextAttributesKey.createTextAttributesKey("NOMO_STRING", DefaultLanguageHighlighterColors.STRING)
        val NUMBER: TextAttributesKey =
            TextAttributesKey.createTextAttributesKey("NOMO_NUMBER", DefaultLanguageHighlighterColors.NUMBER)
        val COMMENT: TextAttributesKey =
            TextAttributesKey.createTextAttributesKey("NOMO_COMMENT", DefaultLanguageHighlighterColors.LINE_COMMENT)
        val OPERATOR: TextAttributesKey =
            TextAttributesKey.createTextAttributesKey("NOMO_OPERATOR", DefaultLanguageHighlighterColors.OPERATION_SIGN)
        val PUNCTUATION: TextAttributesKey =
            TextAttributesKey.createTextAttributesKey("NOMO_PUNCTUATION", DefaultLanguageHighlighterColors.DOT)
        val BAD_CHARACTER: TextAttributesKey =
            TextAttributesKey.createTextAttributesKey("NOMO_BAD_CHARACTER", HighlighterColors.BAD_CHARACTER)
    }
}
