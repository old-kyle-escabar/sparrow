package io.rsbox.sparrow.deobfuscator.asm

import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.tree.ClassNode
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
 * Represents a collection of [ClassNode] loaded from a
 * JAR file.
 */
class ClassGroup : MutableList<ClassNode> by mutableListOf() {

    /**
     * Adds a [ClassNode] from the raw class bytes.
     *
     * @param bytes ByteArray
     * @return Boolean
     */
    fun add(bytes: ByteArray): Boolean {
        val node = ClassNode()
        val reader = ClassReader(bytes)
        reader.accept(node, ClassReader.SKIP_FRAMES or ClassReader.SKIP_DEBUG)
        return this.add(node)
    }

    /**
     * Adds all classes contained inside of a JAR file.
     *
     * @param file File
     */
    fun addJar(file: File) {
        if(!file.exists()) error("Jar file: '${file.name}' does not exist.")

        JarFile(file).use { jar ->
            jar.entries().asSequence()
                .filter { it.name.endsWith(".class") }
                .forEach {
                    add(jar.getInputStream(it).readAllBytes())
                }
        }
    }

    /**
     * Exports the current class group to a JAR file.
     *
     * @param file File
     */
    fun toJar(file: File) {
        val jos = JarOutputStream(FileOutputStream(file))

        this.forEach {
            jos.putNextEntry(JarEntry(it.name + ".class"))

            val writer = ClassWriter(ClassWriter.COMPUTE_MAXS)
            it.accept(writer)

            jos.write(writer.toByteArray())
            jos.closeEntry()
        }

        jos.close()
    }

    /**
     * Gets a [ClassNode] with a given name.
     *
     * @param name String
     * @return ClassNode?
     */
    operator fun get(name: String): ClassNode? = this.firstOrNull { it.name == name }
}