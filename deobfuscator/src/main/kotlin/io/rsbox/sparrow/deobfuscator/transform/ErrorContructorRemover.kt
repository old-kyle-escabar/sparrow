package io.rsbox.sparrow.deobfuscator.transform

import io.rsbox.sparrow.deobfuscator.Transformer
import io.rsbox.sparrow.deobfuscator.asm.ClassGroup
import org.objectweb.asm.Opcodes.*
import org.objectweb.asm.Type
import org.objectweb.asm.tree.MethodNode
import org.tinylog.kotlin.Logger

/**
 * Copyright (c) 2020 RSBox
 *
 * Licensed under GNU General Public License v3.0
 * Please read the LICENSE file for more details.
 *
 * @author Kyle Escobar
 */

/**
 * Removes contructors which handle the error exception throwing.
 * This is required because when removing all RuntimeExceptions,
 * some of the left over constructors are not handled.
 *
 * @property priority Int
 */
class ErrorContructorRemover : Transformer {

    override val priority = 6

    override fun transform(group: ClassGroup) {
        var counter = 0

        group.forEach { c ->
            val methodIterator = c.methods.iterator()
            while(methodIterator.hasNext()) {
                val m = methodIterator.next()
                if(m.hasErrorConstructor) {
                    methodIterator.remove()
                    counter++
                }
            }
        }

        Logger.info("Removed $counter method error constructors.")
    }

    private val MethodNode.hasErrorConstructor: Boolean get() {
        if (this.name != "<init>") return false
        if (Type.getArgumentTypes(this.desc).isNotEmpty()) return false
        if (this.exceptions != listOf(Type.getType(Throwable::class.java).internalName)) return false
        val insns = this.instructions.toArray().filter { it.opcode > 0 }.iterator()
        if (!insns.hasNext() || insns.next().opcode != ALOAD) return false
        if (!insns.hasNext() || insns.next().opcode != INVOKESPECIAL) return false
        if (!insns.hasNext() || insns.next().opcode != NEW) return false
        if (!insns.hasNext() || insns.next().opcode != DUP) return false
        if (!insns.hasNext() || insns.next().opcode != INVOKESPECIAL) return false
        if (!insns.hasNext() || insns.next().opcode != ATHROW) return false
        return !insns.hasNext()
    }
}