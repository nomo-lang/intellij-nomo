package org.nomolang.intellij

import com.intellij.psi.tree.IElementType
import kotlin.test.Test
import kotlin.test.assertEquals

class NomoLexerContractTest {
    @Test
    fun `canonical declarations and callable returns have stable fallback tokens`() {
        val source = """
            package hello_world

            interface Sink {
                fn close(self)
            }

            extern "C" {
                fn release(handle: i64)
            }

            suspend fn flush() {
            }

            fn register(callback: task fn(string) -> void) -> Result<void, string> {
                return Ok(void)
            }
        """.trimIndent()

        val tokens = tokens(source)

        assertEquals(NomoTokenTypes.KEYWORD, tokens.single { it.first == "suspend" }.second)
        assertEquals(NomoTokenTypes.KEYWORD, tokens.single { it.first == "task" }.second)
        assertEquals(
            listOf(NomoTokenTypes.TYPE, NomoTokenTypes.TYPE, NomoTokenTypes.TYPE),
            tokens.filter { it.first == "void" }.map { it.second },
        )
        assertEquals(
            listOf(NomoTokenTypes.OPERATOR, NomoTokenTypes.OPERATOR),
            tokens.filter { it.first == "->" }.map { it.second },
        )
    }

    @Test
    fun `task stays an identifier outside a callable type`() {
        val tokens = tokens("let task = worker")

        assertEquals(NomoTokenTypes.IDENTIFIER, tokens.single { it.first == "task" }.second)
    }

    private fun tokens(source: String): List<Pair<String, IElementType>> {
        val lexer = NomoLexer()
        lexer.start(source, 0, source.length, 0)
        return buildList {
            while (lexer.tokenType != null) {
                if (lexer.tokenType != com.intellij.psi.TokenType.WHITE_SPACE) {
                    add(source.substring(lexer.tokenStart, lexer.tokenEnd) to lexer.tokenType!!)
                }
                lexer.advance()
            }
        }
    }
}
