package org.nomolang.intellij

import com.intellij.lexer.LexerBase
import com.intellij.psi.TokenType
import com.intellij.psi.tree.IElementType

class NomoLexer : LexerBase() {
    private var buffer: CharSequence = ""
    private var startOffset: Int = 0
    private var endOffset: Int = 0
    private var tokenStart: Int = 0
    private var tokenEnd: Int = 0
    private var tokenType: IElementType? = null

    override fun start(buffer: CharSequence, startOffset: Int, endOffset: Int, initialState: Int) {
        this.buffer = buffer
        this.startOffset = startOffset
        this.endOffset = endOffset
        tokenStart = startOffset
        tokenEnd = startOffset
        tokenType = null
        advance()
    }

    override fun getState(): Int = 0

    override fun getTokenType(): IElementType? = tokenType

    override fun getTokenStart(): Int = tokenStart

    override fun getTokenEnd(): Int = tokenEnd

    override fun advance() {
        tokenStart = tokenEnd
        if (tokenStart >= endOffset) {
            tokenType = null
            return
        }

        val ch = buffer[tokenStart]
        tokenType = when {
            ch.isWhitespace() -> scanWhile { it.isWhitespace() }.also { tokenEnd = it }.let { TokenType.WHITE_SPACE }
            ch == '/' && peek(1) == '/' -> scanLineComment()
            ch == '/' && peek(1) == '*' -> scanBlockComment()
            ch == '"' -> scanString()
            ch == '\'' -> scanChar()
            ch == '#' && peek(1) == '[' -> scanAttribute()
            ch.isDigit() -> scanNumber()
            isIdentifierStart(ch) -> scanIdentifier()
            else -> scanSymbol()
        }
    }

    override fun getBufferSequence(): CharSequence = buffer

    override fun getBufferEnd(): Int = endOffset

    private fun scanLineComment(): IElementType {
        tokenEnd = scanWhile { it != '\n' && it != '\r' }
        return NomoTokenTypes.COMMENT
    }

    private fun scanBlockComment(): IElementType {
        var index = tokenStart + 2
        var depth = 1
        while (index < endOffset && depth > 0) {
            if (buffer[index] == '/' && index + 1 < endOffset && buffer[index + 1] == '*') {
                depth++
                index += 2
            } else if (buffer[index] == '*' && index + 1 < endOffset && buffer[index + 1] == '/') {
                depth--
                index += 2
            } else {
                index++
            }
        }
        tokenEnd = index
        return NomoTokenTypes.COMMENT
    }

    private fun scanString(): IElementType {
        tokenEnd = scanQuoted('"')
        return NomoTokenTypes.STRING
    }

    private fun scanChar(): IElementType {
        tokenEnd = scanQuoted('\'')
        return NomoTokenTypes.STRING
    }

    private fun scanAttribute(): IElementType {
        var index = tokenStart + 2
        while (index < endOffset) {
            val current = buffer[index]
            index++
            if (current == ']') {
                break
            }
            if (current == '\n' || current == '\r') {
                break
            }
        }
        tokenEnd = index
        return NomoTokenTypes.ATTRIBUTE
    }

    private fun scanNumber(): IElementType {
        var index = scanWhile { it.isDigit() }
        if (index < endOffset && buffer[index] == '.' && index + 1 < endOffset && buffer[index + 1].isDigit()) {
            index++
            while (index < endOffset && buffer[index].isDigit()) {
                index++
            }
        }
        tokenEnd = index
        return NomoTokenTypes.NUMBER
    }

    private fun scanIdentifier(): IElementType {
        tokenEnd = scanWhile { isIdentifierPart(it) }
        val text = buffer.subSequence(tokenStart, tokenEnd).toString()
        return when {
            text in KEYWORDS -> NomoTokenTypes.KEYWORD
            text == "task" && followedByFunctionKeyword() -> NomoTokenTypes.KEYWORD
            text in PRIMITIVE_TYPES -> NomoTokenTypes.TYPE
            text.firstOrNull()?.isUpperCase() == true -> NomoTokenTypes.TYPE
            else -> NomoTokenTypes.IDENTIFIER
        }
    }

    private fun followedByFunctionKeyword(): Boolean {
        var index = tokenEnd
        while (index < endOffset && buffer[index].isWhitespace()) {
            index++
        }
        if (index + 2 > endOffset || buffer.subSequence(index, index + 2).toString() != "fn") {
            return false
        }
        return index + 2 == endOffset || !isIdentifierPart(buffer[index + 2])
    }

    private fun scanSymbol(): IElementType {
        val three = if (tokenStart + 2 < endOffset) {
            buffer.subSequence(tokenStart, tokenStart + 3).toString()
        } else {
            ""
        }
        if (three in THREE_CHAR_OPERATORS) {
            tokenEnd = tokenStart + 3
            return NomoTokenTypes.OPERATOR
        }

        val two = if (tokenStart + 1 < endOffset) {
            buffer.subSequence(tokenStart, tokenStart + 2).toString()
        } else {
            ""
        }
        if (two in TWO_CHAR_OPERATORS) {
            tokenEnd = tokenStart + 2
            return NomoTokenTypes.OPERATOR
        }

        tokenEnd = tokenStart + 1
        return when (buffer[tokenStart]) {
            '+', '-', '*', '/', '%', '=', '!', '<', '>', '?', '&', '|', '^' -> NomoTokenTypes.OPERATOR
            '#', '.', ',', ':', '(', ')', '[', ']', '{', '}' -> NomoTokenTypes.PUNCTUATION
            else -> TokenType.BAD_CHARACTER
        }
    }

    private fun scanQuoted(quote: Char): Int {
        var index = tokenStart + 1
        while (index < endOffset) {
            val current = buffer[index]
            index++
            if (current == '\\' && index < endOffset) {
                index++
            } else if (current == quote) {
                break
            } else if (current == '\n' || current == '\r') {
                break
            }
        }
        return index
    }

    private fun scanWhile(predicate: (Char) -> Boolean): Int {
        var index = tokenStart
        while (index < endOffset && predicate(buffer[index])) {
            index++
        }
        return index
    }

    private fun peek(offset: Int): Char? {
        val index = tokenStart + offset
        return if (index < endOffset) buffer[index] else null
    }

    private fun isIdentifierStart(ch: Char): Boolean = ch == '_' || ch.isLetter()

    private fun isIdentifierPart(ch: Char): Boolean = ch == '_' || ch.isLetterOrDigit()

    private companion object {
        val KEYWORDS = setOf(
            "package",
            "import",
            "pub",
            "impl",
            "interface",
            "extern",
            "unsafe",
            "suspend",
            "fn",
            "struct",
            "enum",
            "const",
            "if",
            "else",
            "match",
            "panic",
            "as",
            "let",
            "mut",
            "return",
            "true",
            "false",
            "for",
            "in",
            "break",
            "continue",
            "defer",
        )
        val PRIMITIVE_TYPES = setOf(
            "bool",
            "i32",
            "i64",
            "u32",
            "u64",
            "f64",
            "char",
            "string",
            "void",
        )
        val THREE_CHAR_OPERATORS = setOf("&^=", "<<=", ">>=")
        val TWO_CHAR_OPERATORS = setOf(
            "==",
            "!=",
            "<=",
            ">=",
            "->",
            "=>",
            "+=",
            "-=",
            "*=",
            "/=",
            "%=",
            "&=",
            "^=",
            "|=",
            "++",
            "--",
            "&&",
            "||",
            "&^",
            "<<",
            ">>",
        )
    }
}
