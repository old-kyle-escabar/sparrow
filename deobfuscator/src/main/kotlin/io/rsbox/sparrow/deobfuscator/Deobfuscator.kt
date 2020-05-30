package io.rsbox.sparrow.deobfuscator

import io.github.classgraph.ClassGraph
import io.rsbox.sparrow.asm.ClassGroup
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
 * Represents a OSRS gamepack Deobfuscator.
 *
 * @constructor
 */
class Deobfuscator() {

    private lateinit var source: File
    private lateinit var output: File

    constructor(source: File) : this() {
        this.source = source
    }

    constructor(source: File, output: File) : this() {
        this.source = source
        this.output = output
    }

    /**
     * The current loaded class group. Loaded from [source] JAR file.
     */
    lateinit var group: ClassGroup
        private set

    /**
     * Loads the [source] JAR file into the loaded class group.
     *
     * @return Boolean
     */
    fun loadSource(): Boolean {
        return try {
            group = ClassGroup.fromJar(source)

            Logger.info("Loaded source JAR into class group. Found '${group.size} classes'.")

            true
        } catch(e : Exception) {
            Logger.error("An error occurred when loading the source JAR file.", e)
            throw Exception(e)
        }
    }

    /**
     * Exports the [group] to the [output] jar file.
     */
    fun exportGroup() {
        Logger.info("Preparing to export to JAR file.")
        group.toJar(output)
    }

    /**
     * Executes the deobfuscator.
     */
    fun deobfuscate() {
        /**
         * Ensure the source JAR has been loaded into the class group.
         */
        if(!this::group.isInitialized) error("Source JAR file must be loaded.")

        Logger.info("Running deobfuscator.")

        val transformers = this.findTransformers().toMutableList()
        Logger.info("Found ${transformers.size} bytecode transformers.")

        transformers.sortBy { it.priority }

        transformers.forEach {
            Logger.info("Running transformer '${it::class.java.simpleName}'.")
            it.transform(group)
        }

        Logger.info("Completed deobfuscator transformations.")
    }

    private fun findTransformers(): List<Transformer> {
        Logger.info("Scanning for bytecode transformers.")

        val scan = ClassGraph().enableAllInfo()
            .whitelistPackages("io.rsbox.sparrow.deobfuscator")
            .scan()

        val resultList = scan.getClassesImplementing("io.rsbox.sparrow.deobfuscator.Transformer")
        return resultList.loadClasses().map { it.getDeclaredConstructor().newInstance() as Transformer }
    }
}