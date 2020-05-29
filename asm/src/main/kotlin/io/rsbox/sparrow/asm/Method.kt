package io.rsbox.sparrow.asm

import org.objectweb.asm.Type
import org.objectweb.asm.tree.MethodNode

/**
 * Copyright (c) 2020 RSBox
 *
 * Licensed under GNU General Public License v3.0
 * Please read the LICENSE file for more details.
 *
 * @author Kyle Escobar
 */

/**
 * Represents a method contained in a class
 *
 * @property group ClassGroup
 * @property owner Class
 * @property node MethodNode
 * @constructor
 */
class Method(val group: ClassGroup, val owner: Class, val node: MethodNode) {

    val name get() = node.name

    val desc get() = node.desc

    val type get() = Type.getMethodType(desc)

    val access get() = node.access

    val returnType get() = Type.getReturnType(desc)

    val argTypes get() = Type.getArgumentTypes(desc)

    override fun toString(): String = "${owner.name}.$name$desc"
}