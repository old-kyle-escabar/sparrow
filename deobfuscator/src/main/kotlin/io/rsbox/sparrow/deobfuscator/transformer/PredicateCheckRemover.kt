package io.rsbox.sparrow.deobfuscator.transformer

import io.rsbox.sparrow.asm.ClassGroup
import io.rsbox.sparrow.asm.util.intValue
import io.rsbox.sparrow.asm.util.isIntValue
import io.rsbox.sparrow.deobfuscator.Transformer
import org.objectweb.asm.Opcodes
import org.objectweb.asm.Opcodes.*
import org.objectweb.asm.Type
import org.objectweb.asm.tree.*
import org.tinylog.kotlin.Logger
import java.lang.IllegalStateException
import java.lang.reflect.Modifier
import java.util.*

/**
 * Copyright (c) 2020 RSBox
 *
 * Licensed under GNU General Public License v3.0
 * Please read the LICENSE file for more details.
 *
 * @author Kyle Escobar
 */

/**
 * Responsible for removing the checks for the opaque predicates.
 * These are also known in other clients as the garbage values.
 */
class PredicateCheckRemover : Transformer {

    private val ISE_EXCEPTION_NAME = Type.getInternalName(IllegalStateException::class.java)

    override fun transform(group: ClassGroup) {
        val passingArgs = TreeMap<String, Int>()
        var returns = 0
        var exceptions = 0

        group.classes.forEach { c ->
            c.methods.forEach { m ->
                val insns = m.node.instructions.iterator()
                val lastParamIndex = m.node.lastParamIndex

                while(insns.hasNext()) {
                    val insn = insns.next()
                    val toDelete = if(insn.matchesReturn(lastParamIndex)) {
                        returns++
                        4
                    } else if(!insn.matchesException(lastParamIndex)) {
                        exceptions++
                        7
                    } else {
                        continue
                    }

                    val constPushed = insn.next.next.intValue
                    val ifOpcode = insn.next.next.opcode
                    val label = (insn.next.next as JumpInsnNode).label.label
                    insns.remove()
                    repeat(toDelete - 1) {
                        insns.next()
                        insns.remove()
                    }

                    insns.add(JumpInsnNode(GOTO, LabelNode(label)))
                    passingArgs["${c.name}.${m.name}${m.desc}"] = passingVal(constPushed, ifOpcode)
                }
            }
        }

        Logger.info("Removed $returns returns and $exceptions exceptions predicate checks.")
    }

    private fun AbstractInsnNode.matchesReturn(lastParamIndex: Int): Boolean {
        val i0 = this
        if (i0.opcode != ILOAD) return false
        i0 as VarInsnNode
        if (i0.`var` != lastParamIndex) return false
        val i1 = i0.next
        if (!i1.isIntValue) return false
        val i2 = i1.next
        if (!i2.isIf) return false
        val i3 = i2.next
        if (!i3.isReturn) return false
        return true
    }

    private fun AbstractInsnNode.matchesException(lastParamIndex: Int): Boolean {
        val i0 = this
        if (i0.opcode != ILOAD) return false
        i0 as VarInsnNode
        if (i0.`var` != lastParamIndex) return false
        val i1 = i0.next
        if (!i1.isIntValue) return false
        val i2 = i1.next
        if (!i2.isIf) return false
        val i3 = i2.next
        if (i3.opcode != NEW) return false
        val i4 = i3.next
        if (i4.opcode != DUP) return false
        val i5 = i4.next
        if (i5.opcode != INVOKESPECIAL) return false
        i5 as MethodInsnNode
        if (i5.owner != ISE_EXCEPTION_NAME) return false
        val i6 = i5.next
        if (i6.opcode != ATHROW) return false
        return true
    }

    private val MethodNode.lastParamIndex: Int get() {
        val offset = if (Modifier.isStatic(access)) 1 else 0
        return (Type.getArgumentsAndReturnSizes(desc) shr 2) - offset - 1
    }

    private fun passingVal(pushed: Int, ifOpcode: Int): Int {
        return when(ifOpcode) {
            IF_ICMPEQ -> pushed
            IF_ICMPGE,
            IF_ICMPGT -> pushed + 1
            IF_ICMPLE,
            IF_ICMPLT,
            IF_ICMPNE -> pushed - 1
            else -> error(ifOpcode)
        }
    }

    private val AbstractInsnNode.isIf: Boolean get() {
        return this is JumpInsnNode && opcode != Opcodes.GOTO
    }

    private val AbstractInsnNode.isReturn: Boolean get() {
        return when (opcode) {
            RETURN, ARETURN, DRETURN, FRETURN, IRETURN, LRETURN -> true
            else -> false
        }
    }
}