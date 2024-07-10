package com.hanggrian.rulebook.ktlint

import com.pinterest.ktlint.cli.ruleset.core.api.RuleSetProviderV3
import com.pinterest.ktlint.rule.engine.core.api.RuleProvider

public class RulebookRuleSet : RuleSetProviderV3(Rule.ID) {
    override fun getRuleProviders(): Set<RuleProvider> =
        setOf(
            RuleProvider { BlockCommentLineJoiningRule() },
            RuleProvider { BlockCommentLineTrimmingRule() },
            RuleProvider { BlockCommentSpacingRule() },
            RuleProvider { BlockTagInitialLineSpacingRule() },
            RuleProvider { BlockTagPunctuationRule() },
            RuleProvider { ClassFinalNameBlacklistingRule() },
            RuleProvider { ClassNameAcronymCapitalizationRule() },
            RuleProvider { ClassOrganizationRule() },
            RuleProvider { CommentLineJoiningRule() },
            RuleProvider { CommentLineTrimmingRule() },
            RuleProvider { ConditionalBranchLineJoiningRule() },
            RuleProvider { EmptyCodeBlockConcisenessRule() },
            RuleProvider { ExceptionExtendingRule() },
            RuleProvider { ExceptionSubclassCatchingRule() },
            RuleProvider { ExceptionSubclassThrowingRule() },
            RuleProvider { FileInitialLineTrimmingRule() },
            RuleProvider { FileSizeLimitationRule() },
            RuleProvider { GenericsNameWhitelistingRule() },
            RuleProvider { IdentifierNameBlacklistingRule() },
            RuleProvider { IfFlatteningRule() },
            RuleProvider { InnerClassPositionRule() },
            RuleProvider { KotlinApiPriorityRule() },
            RuleProvider { QualifierConsistencyRule() },
            RuleProvider { SpecialFunctionPositionRule() },
            RuleProvider { TodoCommentStylingRule() },
        )
}