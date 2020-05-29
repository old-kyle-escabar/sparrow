package io.rsbox.sparrow.asm.util

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

object JarUtil {

    /**
     * Extracts classes from a JAR file into [ClassNode] objects.
     *
     * @param file File
     * @return Collection<ClassNode>
     */
    fun extractJar(file: File): Collection<ClassNode> {
        val results = mutableListOf<ClassNode>()

        JarFile(file).use { jar ->
            jar.entries().asSequence()
                .filter { it.name.endsWith(".class") }
                .forEach {
                    val node = ClassNode()
                    val reader = ClassReader(jar.getInputStream(it))
                    reader.accept(node, ClassReader.SKIP_FRAMES or ClassReader.SKIP_DEBUG)
                    results.add(node)
                }
        }

        return results
    }
}