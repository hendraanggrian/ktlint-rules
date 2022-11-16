package com.hendraanggrian.lints.ktlint

import com.pinterest.ktlint.core.Rule
import com.pinterest.ktlint.core.ast.ElementType.CALL_EXPRESSION
import com.pinterest.ktlint.core.ast.ElementType.IDENTIFIER
import com.pinterest.ktlint.core.ast.ElementType.REFERENCE_EXPRESSION
import com.pinterest.ktlint.core.ast.ElementType.THROW
import com.pinterest.ktlint.core.ast.ElementType.VALUE_ARGUMENT
import com.pinterest.ktlint.core.ast.ElementType.VALUE_ARGUMENT_LIST
import org.jetbrains.kotlin.com.intellij.lang.ASTNode

/**
 * [See Guide](https://github.com/hendraanggrian/lints/blob/main/guides/exception-ambiguity.md).
 */
class ExceptionAmbiguityRule : Rule("exception-ambiguity") {
    internal companion object {
        const val ERROR_MESSAGE = "Exception '%s' is ambiguous."
    }

    override fun beforeVisitChildNodes(
        node: ASTNode,
        autoCorrect: Boolean,
        emit: (Int, String, Boolean) -> Unit
    ) {
        // first line of filter
        if (node.elementType != THROW) {
            return
        }

        // only target supertype
        val callExpression = node[CALL_EXPRESSION]
        val identifier = callExpression[REFERENCE_EXPRESSION][IDENTIFIER]
        if (identifier.text != "Exception" && identifier.text != "Error" &&
            identifier.text != "Throwable"
        ) {
            return
        }

        // report error if there is no message
        if (VALUE_ARGUMENT !in callExpression[VALUE_ARGUMENT_LIST]) {
            emit(identifier.startOffset, ERROR_MESSAGE.format(identifier.text), false)
        }
    }
}