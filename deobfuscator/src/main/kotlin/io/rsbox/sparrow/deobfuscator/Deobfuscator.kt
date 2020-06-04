package io.rsbox.sparrow.deobfuscator

import io.rsbox.sparrow.deobfuscator.asm.ClassGroup
import io.rsbox.sparrow.deobfuscator.transform.*
import io.rsbox.sparrow.deobfuscator.transform.controlflow.ControlFlowFixer
import io.rsbox.sparrow.deobfuscator.transform.euclidean.MultiplierRemover
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
     * NOTE* The order is VERY important. Some of the transformers are dependent on others
     * to be executed prior.
     *
     * Future plans to add 'dependson' annotation in future version.
     */
    private val transformers = listOf(
        UnusedMethodRemover::class,
        UnusedFieldRemover::class,
        MultiplierRemover::class,
        ControlFlowFixer::class,
        FieldInliner::class,
        TryCatchBlockRemover::class,
        ErrorContructorRemover::class,
        GotoRemover::class,
        DeadCodeRemover::class,
        OpaquePredicateCheckRemover::class,
        OpaquePredicateArgRemover::class,
        FieldSorter::class,
        MethodSorter::class
    )

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
     * @param rename [Boolean] Whether to enabled the renamer transformer.
     */
    fun deobfuscate(rename: Boolean = true) {
        Logger.info("Preparing deobfuscator.")

        transformers.forEach {
            Logger.info("Running transformer: '${it.java.simpleName}'.")

            val transformer = it.java.getDeclaredConstructor().newInstance()
            transformer.transform(group)
        }

        if(rename) {
            Logger.info("Renaming all nodes.")

            val transformer = Renamer::class.java.getDeclaredConstructor().newInstance()
            transformer.transform(group)

            Logger.info("Completed renaming all nodes.")
        }

        Logger.info("Completed deobfuscation.")
    }
}