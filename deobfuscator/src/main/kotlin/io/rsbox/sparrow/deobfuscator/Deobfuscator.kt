package io.rsbox.sparrow.deobfuscator

import io.github.classgraph.ClassGraph
import io.rsbox.sparrow.deobfuscator.asm.ClassGroup
import io.rsbox.sparrow.deobfuscator.transform.Transformer
import org.tinylog.kotlin.Logger
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
 * Represents a bytecode deobfuscator designed to make raw
 * OSRS gamepacks more readable.
 */
class Deobfuscator {

    /**
     * Represents the current loaded class group.
     */
    val group = ClassGroup()

    /**
     * The bytecode transformers to run on the [group]
     */
    private val transformers = this.scanTransformers()

    /**
     * Loads a JAR file into the [ClassGroup].
     *
     * @param file File
     */
    fun loadJar(file: File) {
        Logger.info("Loading classes from JAR file: '${file.name}'.")
        group.addJar(file)
    }

    /**
     * Exports the loaded [group] to a JAR file.
     *
     * @param file File
     */
    fun exportJar(file: File) {
        Logger.info("Exporting classes to JAR file: '${file.name}'.")
        group.toJar(file)
    }

    /**
     * Runs the deobfuscator.
     */
    fun deobfuscate() {
        Logger.info("Preparing deobfuscator.")

        transformers.forEach {
            Logger.info("Running transformer: '${it::class.java.simpleName}'.")
            it.transform(group)
        }

        Logger.info("Completed deobfuscation.")
    }

    private fun scanTransformers(): List<Transformer> {
        val scanner = ClassGraph()
            .enableAllInfo()
            .whitelistPackages("io.rsbox.sparrow.deobfuscator.transform")
            .scan()

        return scanner.getClassesImplementing("io.rsbox.sparrow.deobfuscator.transform.Transformer")
            .loadClasses()
            .map { it.getDeclaredConstructor().newInstance() as Transformer }
            .sortedBy { it.priority }
            .toList()
    }
}