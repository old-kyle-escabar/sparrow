package io.rsbox.sparrow.deobfuscator

import io.rsbox.sparrow.asm.ClassGroup
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
 * @property source The input source JAR file.
 * @property output The output JAR file.
 * @constructor
 */
class Deobfuscator(private val source: File, private val output: File) {

    /**
     * The current loaded class group. Loaded from [source] JAR file.
     */
    private lateinit var group: ClassGroup

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

        Logger.info("Preparing deobfuscator.")
    }
}