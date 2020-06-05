package io.rsbox.sparrow.deobfuscator.transform

import io.rsbox.sparrow.deobfuscator.Transformer
import io.rsbox.sparrow.deobfuscator.asm.ClassNodeGroup
import org.objectweb.asm.Opcodes.GOTO
import org.objectweb.asm.tree.JumpInsnNode
import org.objectweb.asm.tree.LabelNode
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
 * Removes GOTO and JUMP instructions that cause analyzer
 * execution fall off.
 */
class GotoRemover : Transformer {

    override fun transform(group: ClassNodeGroup) {
        var counter = 0

        group.forEach { c ->
            c.methods.forEach { m ->
                val instructions = m.instructions.iterator()
                while(instructions.hasNext()) {
                    val insn0 = instructions.next()

                    if(insn0.opcode != GOTO) continue
                    insn0 as JumpInsnNode

                    val insn1 = insn0.next
                    if(insn1 == null || insn1 !is LabelNode) continue

                    if(insn0.label == insn1) {
                        instructions.remove()
                        counter++
                    }
                }
            }
        }

        Logger.info("Removed $counter GOTO instruction jumps.")
    }
}