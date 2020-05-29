package io.rsbox.sparrow.asm

import io.rsbox.sparrow.asm.util.JarUtil
import org.objectweb.asm.tree.ClassNode
import java.io.File

/**
 * Copyright (c) 2020 RSBox
 *
 * Licensed under GNU General Public License v3.0
 * Please read the LICENSE file for more details.
 *
 * @author Kyle Escobar
 */

/**
 * Represents a collection of classes.
 *
 * @property classes MutableList<ClassNode>
 * @constructor
 */
class ClassGroup(nodes: Collection<ClassNode>) {

    /**
     * The classes in the group.
     */
    val classes = nodes.map { Class(this, it) }.toMutableList()

    /**
     * The number of classes in the group.
     */
    val size: Int get() = classes.size

    /**
     * Gets a [Class] with a given name.
     *
     * @param name String
     * @return Class?
     */
    operator fun get(name: String): Class? = classes.firstOrNull { it.name == name }

    companion object {
        /**
         * Creates a new [ClassGroup] instance from a JAR file.
         *
         * @param file File
         * @return ClassGroup
         */
        fun fromJar(file: File): ClassGroup {
            if(!file.exists()) error("Unable to locate JAR file: '${file.name}'.")
            return ClassGroup(JarUtil.extractJar(file))
        }
    }
}