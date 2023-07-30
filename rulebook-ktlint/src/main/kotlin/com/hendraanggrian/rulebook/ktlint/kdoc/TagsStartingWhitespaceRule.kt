package com.hendraanggrian.rulebook.ktlint.kdoc

import com.hendraanggrian.rulebook.ktlint.RulebookRule
import com.hendraanggrian.rulebook.ktlint.siblingsUntil
import com.pinterest.ktlint.rule.engine.core.api.ElementType.KDOC_LEADING_ASTERISK
import com.pinterest.ktlint.rule.engine.core.api.ElementType.KDOC_SECTION
import com.pinterest.ktlint.rule.engine.core.api.ElementType.KDOC_TAG
import com.pinterest.ktlint.rule.engine.core.api.isWhiteSpaceWithNewline
import com.pinterest.ktlint.rule.engine.core.api.prevSibling
import org.jetbrains.kotlin.com.intellij.lang.ASTNode
import org.jetbrains.kotlin.psi.psiUtil.children

/**
 * [See wiki](https://github.com/hendraanggrian/rulebook/wiki/TagsStartingWhitespace).
 */
class TagsStartingWhitespaceRule : RulebookRule("tags-starting-whitespace") {
    internal companion object {
        const val ERROR_MESSAGE = "Missing empty line before '%s'."
    }

    override fun beforeVisitChildNodes(
        node: ASTNode,
        autoCorrect: Boolean,
        emit: (offset: Int, errorMessage: String, canBeAutoCorrected: Boolean) -> Unit
    ) {
        // first line of filter
        if (node.elementType != KDOC_SECTION) {
            return
        }

        // only allow first tag
        val kdocTag = (node.findChildByType(KDOC_TAG)) ?: return
        if (kdocTag.prevSibling { it.elementType == KDOC_TAG } != null) {
            return
        }

        // find previous leading asterisk
        val kdocTagAsterisk = kdocTag.prevKdocLeadingAsterisk ?: return
        val prevKdocTagLeadingAsterisk = kdocTagAsterisk.prevKdocLeadingAsterisk ?: return

        // check if last line is newline
        val siblings = prevKdocTagLeadingAsterisk.siblingsUntil(KDOC_LEADING_ASTERISK)
        val hasEmptyLine = siblings.size == 1 && siblings.single().isWhiteSpaceWithNewline()
        if (!hasEmptyLine) {
            val tagName = kdocTag.children().first().text
            emit(kdocTag.startOffset, ERROR_MESSAGE.format(tagName), false)
        }
    }

    private val ASTNode.prevKdocLeadingAsterisk: ASTNode?
        get() = prevSibling { it.elementType == KDOC_LEADING_ASTERISK }
}