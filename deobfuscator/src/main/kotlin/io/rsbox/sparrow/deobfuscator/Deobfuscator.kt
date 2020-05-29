package io.rsbox.sparrow.deobfuscator

import io.rsbox.sparrow.asm.ClassGroup
import io.rsbox.sparrow.deobfuscator.transformer.DeadCodeRemover
import io.rsbox.sparrow.deobfuscator.transformer.PredicateCheckRemover
import io.rsbox.sparrow.deobfuscator.transformer.TryCatchRemover
import org.tinylog.kotlin.Logger
import java.io.File
import kotlin.system.exitProcess

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

    private val transformers = arrayOf(
        PredicateCheckRemover(),
        TryCatchRemover(),
        DeadCodeRemover()
    )

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
            false
        }
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

        transformers.forEach {
            Logger.info("Running transformer '${it::class.java.simpleName}'.")
            it.transform(group)
        }

        Logger.info("Completed deobfuscator transformations.")
    }
}