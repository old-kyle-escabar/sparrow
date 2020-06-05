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
 * @property classes MutableList<Class>
 * @constructor
 */
class ClassGroup private constructor(nodes: Collection<ClassNode>) {

    /**
     * The [Class] objects contained in this group.
     */
    val classes = nodes.map { Class(this, it) }.toMutableList()

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