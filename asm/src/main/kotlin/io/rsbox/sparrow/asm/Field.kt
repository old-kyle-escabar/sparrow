package io.rsbox.sparrow.asm

import org.objectweb.asm.Type
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
 * Represents a field which belongs to an ASM class.
 *
 * @property group ClassGroup
 * @property owner Class
 * @property node FieldNode
 * @constructor
 */
class Field(val group: ClassGroup, val owner: Class, val node: FieldNode) : Matchable<Field>() {

    /**
     * The name of the field
     */
    val name get() = node.name

    /**
     * The descriptor of the field.
     */
    val desc get() = node.desc

    /**
     * The type of field.
     */
    val type get() = Type.getType(desc)

    /**
     * The unique identifier for this field.
     */
    val id get() = owner.type to name

    /**
     * A string representation of the field.
     *
     * @return String
     */
    override fun toString(): String = "${owner.name}.$name"
}