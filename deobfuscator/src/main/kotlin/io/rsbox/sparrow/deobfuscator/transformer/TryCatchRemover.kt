package io.rsbox.sparrow.deobfuscator.transformer

import io.rsbox.sparrow.asm.ClassGroup
import io.rsbox.sparrow.deobfuscator.Transformer
import io.rsbox.sparrow.deobfuscator.TransformerDefinition
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
 * Responsible for removing the [RuntimeException] try-catch blocks within the source code.
 */
@TransformerDefinition
class TryCatchRemover : Transformer {

    override val priority = 1

    override fun transform(group: ClassGroup) {
        var counter = 0

        group.classes.forEach classLoop@ { c ->
            c.asm.methods.forEach methodLoop@ { m ->
                if(m.tryCatchBlocks.size == 0) return@methodLoop

                val initSize = m.tryCatchBlocks.size
                m.tryCatchBlocks.removeIf { it.type == Type.getInternalName(RuntimeException::class.java) }
                counter += initSize - m.tryCatchBlocks.size
            }
        }

        Logger.info("Removed $counter try-catch blocks.")
    }
}