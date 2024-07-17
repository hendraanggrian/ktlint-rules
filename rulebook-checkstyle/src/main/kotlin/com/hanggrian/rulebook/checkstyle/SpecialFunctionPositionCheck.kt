package com.hanggrian.rulebook.checkstyle

import com.hanggrian.rulebook.checkstyle.internals.Messages
import com.hanggrian.rulebook.checkstyle.internals.hasAnnotation
import com.hanggrian.rulebook.checkstyle.internals.hasModifier
import com.hanggrian.rulebook.checkstyle.internals.nextSiblings
import com.puppycrawl.tools.checkstyle.api.DetailAST
import com.puppycrawl.tools.checkstyle.api.TokenTypes.IDENT
import com.puppycrawl.tools.checkstyle.api.TokenTypes.LITERAL_STATIC
import com.puppycrawl.tools.checkstyle.api.TokenTypes.METHOD_DEF

/**
 * [See wiki](https://github.com/hanggrian/rulebook/wiki/Rules/#special-function-position)
 */
public class SpecialFunctionPositionCheck : Check() {
    override fun getRequiredTokens(): IntArray = intArrayOf(METHOD_DEF)

    override fun visitToken(node: DetailAST) {
        // target special function
        val ident =
            node
                .takeIf { it.isSpecialFunction() }
                ?.findFirstToken(IDENT)
                ?: return

        // checks for violation
        node.nextSiblings
            .takeIf { siblings ->
                // in Java, static members have specific keyword
                siblings.any {
                    it.type == METHOD_DEF &&
                        !it.isSpecialFunction() &&
                        !it.hasModifier(LITERAL_STATIC)
                }
            } ?: return
        log(node, Messages.get(MSG, ident.text))
    }

    internal companion object {
        const val MSG = "special.function.position"

        private val SPECIAL_FUNCTIONS =
            setOf(
                "toString",
                "hashCode",
                "equals",
                "clone",
                "finalize",
            )

        private fun DetailAST.isSpecialFunction() =
            hasAnnotation("Override") &&
                findFirstToken(IDENT)?.text in SPECIAL_FUNCTIONS
    }
}
