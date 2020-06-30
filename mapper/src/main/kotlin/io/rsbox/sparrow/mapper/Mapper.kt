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
 * Represents a classifier used to match JARs with different obfuscations.
 *
 * @property sourceGroup ClassGroup
 * @property referenceGroup ClassGroup
 * @constructor
 */
class Mapper(val sourceGroup: ClassGroup, val referenceGroup: ClassGroup) {

    /**
     * Loads the mapper from two JAR files.
     *
     * @param sourceJar File
     * @param referenceJar File
     * @constructor
     */
    constructor(sourceJar: File, referenceJar: File) : this(ClassGroup.fromJar(sourceJar), ClassGroup.fromJar(referenceJar))

    /**
     * Runs the classification.
     */
    fun classify() {
        Logger.info("Preparing to run JAR matching classifiers.")


    }
}