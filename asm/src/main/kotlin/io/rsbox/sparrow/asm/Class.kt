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
     * The access bitpack of the class.
     */
    val access get() = node.access

    /**
     * The class object ASM [Type].
     */
    val type get() = Type.getObjectType(name)
}