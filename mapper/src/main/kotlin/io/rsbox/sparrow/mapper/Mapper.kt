package io.rsbox.sparrow.mapper

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
 * Represents a mapper which generates mappings for class groups.
 *
 * @property sourceJar File
 * @property referenceJar File
 * @constructor
 */
class Mapper(val sourceJar: File, val referenceJar: File) {

    /**
     * The source Jar class group
     */
    internal lateinit var sourceGroup: ClassGroup

    /**
     * The reference Jar class group
     */
    internal lateinit var referenceGroup: ClassGroup

    /**
     * Loads both the source and reference JAR files.
     */
    fun loadJars() {
        Logger.info("Loading mapper JAR files.")

        sourceGroup = ClassGroup.fromJar(sourceJar)
        referenceGroup = ClassGroup.fromJar(referenceJar)

        Logger.info("Loaded Source JAR [${sourceGroup.classes.size} classes] Reference JAR [${referenceGroup.classes.size}].")
    }

    /**
     * Runs the analysis between both JAR files.
     *
     * @param threads The number of threads to run on.
     */
    fun classifyAll(threads: Int = 4) {
        Logger.info("Preparing to classify all in parallel.")


    }
}