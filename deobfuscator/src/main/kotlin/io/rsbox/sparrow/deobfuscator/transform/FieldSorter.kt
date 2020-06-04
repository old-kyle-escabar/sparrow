package io.rsbox.sparrow.deobfuscator.transform

import io.rsbox.sparrow.deobfuscator.Transformer
import io.rsbox.sparrow.deobfuscator.asm.ClassGroup
import org.objectweb.asm.Type
import org.objectweb.asm.tree.FieldNode
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
 * Sorts the fields in each class based on comparator specs
 */
class FieldSorter : Transformer {

    override fun transform(group: ClassGroup) {
        group.forEach { c ->
            c.fields = c.fields.sortedWith(FIELD_COMPARATOR)
        }

        Logger.info("Re-ordered non-static fields within classes.")
    }

    private val FIELD_COMPARATOR: Comparator<FieldNode> = compareBy<FieldNode> { !Modifier.isStatic(it.access) }
        .thenBy { Modifier.toString(it.access and Modifier.fieldModifiers()) }
        .thenBy { Type.getType(it.desc).className }
        .thenBy { it.name }
}