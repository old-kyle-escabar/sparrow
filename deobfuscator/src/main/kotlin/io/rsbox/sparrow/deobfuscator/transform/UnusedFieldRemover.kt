package io.rsbox.sparrow.deobfuscator.transform

import io.rsbox.sparrow.deobfuscator.Transformer
import io.rsbox.sparrow.deobfuscator.asm.ClassNodeGroup
import org.objectweb.asm.tree.FieldInsnNode
import org.tinylog.kotlin.Logger
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
 * Removes fields where are not invoked in any methods.
 */
class UnusedFieldRemover : Transformer {

    override fun transform(group: ClassNodeGroup) {
        var counter = 0

        val usedFields = group.flatMap { it.methods }
            .flatMap { it.instructions.toArray().asIterable() }
            .mapNotNull { it as? FieldInsnNode }
            .map { it.owner + "." + it.name }
            .toSet()

        group.forEach { c ->
            val fieldIterator = c.fields.iterator()
            while(fieldIterator.hasNext()) {
                val field = fieldIterator.next()
                val fName = c.name + "." + field.name
                if(!usedFields.contains(fName) && Modifier.isFinal(field.access)) {
                    fieldIterator.remove()
                    counter++
                }
            }
        }

        Logger.info("Removed $counter unused fields.")
    }
}