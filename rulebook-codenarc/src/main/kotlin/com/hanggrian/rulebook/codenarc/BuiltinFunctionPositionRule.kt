package com.hanggrian.rulebook.codenarc

import com.hanggrian.rulebook.codenarc.internals.Messages
import com.hanggrian.rulebook.codenarc.internals.hasAnnotation
import org.codehaus.groovy.ast.ClassNode
import org.codehaus.groovy.ast.MethodNode
import org.codenarc.rule.AbstractAstVisitor

/** [See wiki](https://github.com/hanggrian/rulebook/wiki/Rules/#builtin-function-position) */
public class BuiltinFunctionPositionRule : RulebookRule() {
    override fun getName(): String = "BuiltinFunctionPosition"

    override fun getAstVisitorClass(): Class<*> = Visitor::class.java

    internal companion object {
        const val MSG = "builtin.function.position"

        private val BUILTIN_FUNCTIONS =
            setOf(
                "toString",
                "hashCode",
                "equals",
                "clone",
                "finalize",
            )

        private fun MethodNode.isBuiltinFunction() =
            hasAnnotation("Override") && name in BUILTIN_FUNCTIONS
    }

    public class Visitor : AbstractAstVisitor() {
        override fun visitClassComplete(node: ClassNode) {
            super.visitClassComplete(node)

            // collect functions
            // in Groovy, static members have specific keyword
            val functions = node.methods.filter { !it.isStatic }

            for ((i, function) in functions.withIndex()) {
                // target special function
                function
                    .takeIf { it.isBuiltinFunction() }
                    ?: continue

                // checks for violation
                functions
                    .subList(i, functions.size)
                    .takeIf { nodes -> nodes.any { !it.isBuiltinFunction() } }
                    ?: continue
                addViolation(function, Messages.get(MSG, function.name))
            }
        }
    }
}
