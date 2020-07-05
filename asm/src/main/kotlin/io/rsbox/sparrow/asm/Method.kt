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
 * Represents an ASM method which belongs to a class.
 *
 * @property group ClassGroup
 * @property owner Class
 * @property node MethodNode
 * @constructor
 */
class Method(val group: ClassGroup, val owner: Class, val node: MethodNode) : Matchable<Method> {

    /**
     * The matched type.
     */
    override var match: Method? = null

    /**
     * The name of the method.
     */
    val name get() = node.name

    /**
     * The descriptor of the method.
     */
    val desc get() = node.desc

    /**
     * The method type.
     */
    val type get() = Type.getMethodType(desc)

    /**
     * An unique identifier for this method.
     */
    val id get() = Triple(owner.type, name, type)

    /**
     * Whether the name is obfuscated or not.
     *
     * @return Boolean
     */
    override fun isNameObfuscated(): Boolean {
        if(name.length <= 2) return true
        if(name.startsWith("aa") && name.length == 3) return true
        if(name.startsWith("method")) return true
        return false
    }

    /**
     * A string representation of the method.
     *
     * @return String
     */
    override fun toString(): String = "${owner.name}.${name}${desc}"
}