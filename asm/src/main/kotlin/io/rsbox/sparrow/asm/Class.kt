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
 * Represents an ASM class object.
 *
 * @property group ClassGroup
 * @property node ClassNode
 * @constructor
 */
class Class(val group: ClassGroup, val node: ClassNode) {

    /**
     * The name of the class.
     */
    val name get() = node.name

    /**
     * The abstract class name this class extends.
     */
    val superName get() = node.superName

    /**
     * The access bitpack of the class.
     */
    val access get() = node.access

    /**
     * The class object ASM [Type].
     */
    val type get() = Type.getObjectType(name)

    /**
     * The inherited class ASM [Type].
     */
    val superType get() = Type.getObjectType(superName)

    /**
     * The class name of the implemented interface classes.
     */
    val interfaceNames get() = node.interfaces

    /**
     * The interfaces [Class] objects.
     */
    val interfaces get() = node.interfaces.mapNotNull { group[it] }

    /**
     * The methods belonging to this class object.
     */
    val methods = node.methods.map { Method(group, this, it) }

    /**
     * The fields belonging to this class object.
     */
    val fields = node.fields.map { Field(group, this, it) }

    /**
     * A unique identifier for the class object.
     */
    val id get() = type

    /**
     * A string representation of this class.
     *
     * @return String
     */
    override fun toString(): String = name
}