package io.rsbox.sparrow.asm

import org.objectweb.asm.tree.FieldNode

/**
 * Copyright (c) 2020 RSBox
 *
 * Licensed under GNU General Public License v3.0
 * Please read the LICENSE file for more details.
 *
 * @author Kyle Escobar
 */

/**
 * Represents a field contained in a class.
 *
 * @property group ClassGroup
 * @property owner Class
 * @property node FieldNode
 * @constructor
 */
class Field(val group: ClassGroup, val owner: Class, val node: FieldNode) {

    val name get() = node.name

    val desc get() = node.desc

    val access get() = node.access

    override fun toString(): String = "${owner.name}.$name"

}