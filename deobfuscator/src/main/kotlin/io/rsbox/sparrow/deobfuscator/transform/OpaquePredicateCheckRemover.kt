package io.rsbox.sparrow.deobfuscator.transform

import io.rsbox.sparrow.deobfuscator.Transformer
import io.rsbox.sparrow.deobfuscator.asm.ClassGroup
import org.objectweb.asm.Opcodes.*
import org.objectweb.asm.Type
import org.objectweb.asm.tree.*
import org.tinylog.kotlin.Logger
import java.lang.IllegalStateException
import java.lang.reflect.Modifier

/**
 * Copyright (c) 2020 RSBox
 *
 * Licensed under GNU General Public License v3.0
 * Please read the LICENSE file for more details.
 *
 * @author Kyle Escobar
 */

/**
 * Removes the check for the last parameter primitive constant. If the garbage value
 * is removed or changed an [IllegalStateException] is thrown.
 *
 * This transformer removes the check and the exception code.
 */
class OpaquePredicateCheckRemover : Transformer {

    override val priority = 8

    override fun transform(group: ClassGroup) {
        var counter = 0

        /**
         * Loop through each method, in each class.
         */
        group.forEach classLoop@ { c ->
            c.methods.forEach methodLoop@ { m ->
                val insns = m.instructions.iterator()
                val lastParamIndex = m.lastParamIndex

                /**
                 * Loop through each instruction inside of
                 * the method.
                 */
                while(insns.hasNext()) {
                    val insn = insns.next()

                    /**
                     * The number of instructions to delete to remove
                     * the predicate check.
                     */
                    val deleteInsnCount = if(insn.matchExceptionPattern(lastParamIndex)) {
                        7
                    } else if(insn.matchesReturnPattern(lastParamIndex)) {
                        4
                    } else {
                        continue
                    }

                    val label = (insn.next.next as JumpInsnNode).label.label

                    /**
                     * Remove the ILOAD current instruction.
                     */
                    insns.remove()

                    /**
                     * Repeat for the remaining instructions of the check.
                     */
                    repeat(deleteInsnCount - 1) {
                        insns.next()
                        insns.remove()
                        counter++
                    }

                    insns.add(JumpInsnNode(GOTO, LabelNode(label)))
                }
            }
        }

        Logger.info("Removed $counter opaque predicate garbage value checks.")
    }

    /**
     * Gets the index of the last parameter in the method.
     */
    private val MethodNode.lastParamIndex: Int get() {
        val offset = if(Modifier.isStatic(access)) 1 else 0
        return (Type.getArgumentsAndReturnSizes(desc) shr 2) - offset - 1
    }

    /**
     * Checks if the instruction matches the predicate check instruction pattern.
     *
     * @receiver AbstractInsnNode
     * @return Boolean
     */
    private fun AbstractInsnNode.matchExceptionPattern(paramIndex: Int): Boolean {
        val i0 = this
        if(i0.opcode != ILOAD) return false
        i0 as VarInsnNode

        if(i0.`var` != paramIndex) return false

        val i1 = i0.next
        if(!i1.isIntValue()) return false

        val i2 = i1.next
        if(!i2.isIf()) return false

        val i3 = i2.next
        if(i3.opcode != NEW) return false

        val i4 = i3.next
        if(i4.opcode != DUP) return false

        val i5 = i4.next
        if(i5.opcode != INVOKESPECIAL) return false
        i5 as MethodInsnNode
        if(i5.owner != Type.getInternalName(IllegalStateException::class.java)) return false

        val i6 = i5.next
        if(i6.opcode != ATHROW) return false

        return true
    }

    /**
     * Checks if the current instruction matches the return
     * opaque predicate check pattern
     *
     * @receiver AbstractInsnNode
     * @param paramIndex Int
     * @return Boolean
     */
    private fun AbstractInsnNode.matchesReturnPattern(paramIndex: Int): Boolean {
        val i0 = this
        if(i0.opcode != ILOAD) return false
        i0 as VarInsnNode
        if(i0.`var` != paramIndex) return false

        val i1 = i0.next
        if(!i1.isIntValue()) return false

        val i2 = i1.next
        if(!i2.isIf()) return false

        val i3 = i2.next
        if(!i3.isReturn()) return false

        return true
    }

    /**
     * Whether a given instruction is pushing an [Int] to the stack.
     *
     * @receiver AbstractInsnNode
     * @return Boolean
     */
    private fun AbstractInsnNode.isIntValue(): Boolean {
        return when(opcode) {
            LDC -> (this as LdcInsnNode).cst is Int
            SIPUSH, BIPUSH, ICONST_0, ICONST_1, ICONST_2, ICONST_3, ICONST_4, ICONST_5, ICONST_M1 -> true
            else -> false
        }
    }

    /**
     * Whether a given instruction is an IF statement.
     *
     * @receiver AbstractInsnNode
     * @return Boolean
     */
    private fun AbstractInsnNode.isIf(): Boolean {
        return this is JumpInsnNode && this.opcode != GOTO
    }

    private fun AbstractInsnNode.isReturn(): Boolean {
        return when(opcode) {
            RETURN, ARETURN, DRETURN, FRETURN, IRETURN, LRETURN -> true
            else -> false
        }
    }
}