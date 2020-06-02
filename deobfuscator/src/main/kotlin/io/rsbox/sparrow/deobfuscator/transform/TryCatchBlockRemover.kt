package io.rsbox.sparrow.deobfuscator.transform

import io.rsbox.sparrow.deobfuscator.Transformer
import io.rsbox.sparrow.deobfuscator.asm.ClassGroup
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
 * Removes the [RuntimeException] try-catch blocks.
 */
class TryCatchBlockRemover : Transformer {

    override val priority = 4

    override fun transform(group: ClassGroup) {
        var counter = 0

        group.forEach { c ->
            c.methods.forEach methodLoop@ { m ->
                val size = m.tryCatchBlocks.size
                m.tryCatchBlocks.removeIf { it.type == Type.getInternalName(RuntimeException::class.java) }

                counter += size - m.tryCatchBlocks.size
            }
        }

        Logger.info("Removed $counter RuntimeException try-catch blocks.")
    }
}