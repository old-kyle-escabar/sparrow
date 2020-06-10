package io.rsbox.sparrow.asm

import org.objectweb.asm.ClassReader
import org.objectweb.asm.tree.ClassNode
import java.io.File
import java.util.jar.JarFile

/**
 * Copyright (c) 2020 RSBox
 *
 * Licensed under GNU General Public License v3.0
 * Please read the LICENSE file for more details.
 *
 * @author Kyle Escobar
 */

/**
 * Represents a group of [Class] objects.
 *
 * @param nodes Collection<ClassNode>
 * @constructor
 */
class ClassGroup private constructor(nodes: Collection<ClassNode>) {

    /**
     * The [Class] objects contained in this group.
     */
    val classes = nodes.map { Class(this, it) }.toMutableList()

    /**
     * Finds a [Class] with a given name
     *
     * @param name String
     * @return Class?
     */
    operator fun get(name: String): Class? = classes.firstOrNull { it.name == name }

    /**
     * Replaces a [Class] with a new object at the given index.
     *
     * @param name String
     * @param value Class
     * @return Boolean
     */
    fun replace(name: String, value: Class): Boolean {
        if(this[name] == null) return false

        val index = classes.indexOf(this[name])
        classes.removeAt(index)
        classes.add(index, value)

        return true
    }

    /**
     * Removes a [Class] from the group.
     *
     * @param target Class
     * @return Boolean
     */
    fun remove(target: Class): Boolean {
        return classes.remove(target)
    }

    companion object {
        /**
         * Initializes a [ClassGroup] object from a JAR file.
         *
         * @param file File
         * @return ClassGroup
         */
        fun fromJar(file: File): ClassGroup {
            val nodes = mutableListOf<ClassNode>()

            JarFile(file).use { jar ->
                jar.stream()
                    .filter { it.name.endsWith(".class") }
                    .forEach {
                        val node = ClassNode()
                        val reader = ClassReader(jar.getInputStream(it))
                        reader.accept(node, ClassReader.SKIP_FRAMES or ClassReader.SKIP_DEBUG)

                        nodes.add(node)
                    }
            }

            return ClassGroup(nodes)
        }

        /**
         * Initializes a [ClassGroup] object from a collection of [ClassNode]s
         *
         * @param nodes Collection<ClassNode>
         * @return ClassGroup
         */
        fun fromNodes(nodes: Collection<ClassNode>): ClassGroup {
            return ClassGroup(nodes)
        }
    }
}