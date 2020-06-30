package io.rsbox.sparrow.deobfuscator.transform

import io.rsbox.sparrow.deobfuscator.Transformer
import io.rsbox.sparrow.deobfuscator.asm.ClassNodeGroup
import org.objectweb.asm.tree.LineNumberNode
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
 * Sorts methods by the number of lines.
 */
class MethodSorter : Transformer {

    override fun transform(group: ClassNodeGroup) {
        group.forEach { c ->
            val methodsByLineCount = c.methods.associateWith { (it.firstLineIndex) ?: Integer.MAX_VALUE }
            c.methods = c.methods.sortedBy { methodsByLineCount.getValue(it) }
        }

        Logger.info("Sorted methods by number of lines in all classes.")
    }

    private val MethodNode.firstLineIndex: Int? get() {
        this.instructions.forEach { insn ->
            if(insn is LineNumberNode) {
                return insn.line
            }
        }

        return null
    }
}