package io.rsbox.sparrow.asm

import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.tree.ClassNode
import org.tinylog.kotlin.Logger
import java.io.File
import java.io.FileOutputStream
import java.util.jar.JarEntry
import java.util.jar.JarFile
import java.util.jar.JarOutputStream

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
class ClassGroup private constructor(nodes: Collection<ClassNode>) {

    /**
     * The classes in the group.
     */
    val classes = nodes.map { Class(this, it) }

    /**
     * The number of classes in the group.
     */
    val size: Int get() = classes.size

    /**
     * Exports the current group to a JAR file.
     *
     * @param file File
     */
    fun toJar(file: File) {
        val jos = JarOutputStream(FileOutputStream(file))
        classes.forEach {
            jos.putNextEntry(JarEntry(it.asm.name + ".class"))

            val writer = ClassWriter(ClassWriter.COMPUTE_MAXS)
            it.asm.accept(writer)

            jos.write(writer.toByteArray())
            jos.closeEntry()
        }

        jos.close()

        Logger.info("Completed export to JAR file.")
    }

    companion object {
        /**
         * Creates a new [ClassGroup] instance from a JAR file.
         *
         * @param file File
         * @return ClassGroup
         */
        fun fromJar(file: File): ClassGroup {
            if(!file.exists()) error("Unable to locate JAR file: '${file.name}'.")

            val nodes = mutableListOf<ClassNode>()
            JarFile(file).use { jar ->
                jar.entries().asSequence()
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
    }
}