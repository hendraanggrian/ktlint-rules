package com.hendraanggrian.rulebook.ktlint

import com.pinterest.ktlint.rule.engine.core.api.ElementType.WHEN_ENTRY
import com.pinterest.ktlint.rule.engine.core.api.ElementType.WHITE_SPACE
import org.jetbrains.kotlin.com.intellij.lang.ASTNode

/**
 * [See wiki](https://github.com/hendraanggrian/rulebook/wiki/RemoveEmptyLineInSwitch).
 */
class RemoveEmptyLineInSwitchRule : RulebookRule("remove-empty-line-in-switch") {
    internal companion object {
        const val MSG = "remove.empty.line.in.switch"
    }

    override fun beforeVisitChildNodes(
        node: ASTNode,
        autoCorrect: Boolean,
        emit: (offset: Int, errorMessage: String, canBeAutoCorrected: Boolean) -> Unit
    ) {
        // first line of filter
        if (node.elementType != WHEN_ENTRY) {
            return
        }

        // get previous whitespace
        val whitespace = node.treePrev
        if (whitespace.elementType != WHITE_SPACE) {
            return
        }

        // report missing empty line
        if ("\n\n" in whitespace.text) {
            emit(whitespace.endOffset, Messages[MSG], false)
        }
    }
}
