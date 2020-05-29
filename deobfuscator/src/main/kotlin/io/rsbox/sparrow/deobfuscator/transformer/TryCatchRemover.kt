package io.rsbox.sparrow.deobfuscator.transformer

import io.rsbox.sparrow.asm.ClassGroup
import io.rsbox.sparrow.deobfuscator.Transformer
import org.objectweb.asm.Type
import org.tinylog.kotlin.Logger
import java.lang.RuntimeException

/**
 * Copyright (c) 2020 RSBox
 *
 * Licensed under GNU General Public License v3.0
 * Please read the LICENSE file for more details.
 *
 * @author Kyle Escobar
 */

/**
 * Responsible for removing unused try-catch blocks.
 */
class TryCatchRemover : Transformer {

    private val RUNTIME_EXCEPTION_TYPE = Type.getInternalName(RuntimeException::class.java)

    override fun transform(group: ClassGroup) {
        var counter = 0

        group.classes.forEach { c ->
            c.methods.forEach { m ->
                val size = m.node.tryCatchBlocks.size
                m.node.tryCatchBlocks.removeIf { it.type == RUNTIME_EXCEPTION_TYPE }
                counter += size - m.node.tryCatchBlocks.size
            }
        }

        Logger.info("Removed $counter runtime exception try-catch blocks.")
    }
}