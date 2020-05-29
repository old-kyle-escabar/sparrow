package io.rsbox.sparrow.asm

import org.objectweb.asm.Type
import org.objectweb.asm.tree.ClassNode

/**
 * Copyright (c) 2020 RSBox
 *
 * Licensed under GNU General Public License v3.0
 * Please read the LICENSE file for more details.
 *
 * @author Kyle Escobar
 */

/**
 * Represents a java class.
 *
 * @property group ClassGroup
 * @property node ClassNode
 * @constructor
 */
class Class(val group: ClassGroup, val node: ClassNode) {

    val name get() = node.name

    val superName get() = node.superName

    val type get() = Type.getObjectType(name)

    val access get() = node.access

    val methods = node.methods
        .filter { it.name.length <= 2 }
        .map { Method(group, this, it) }

    val fields = node.fields
        .filter { it.name.length <= 2 }
        .map { Field(group, this, it) }

    override fun toString(): String = node.name
}