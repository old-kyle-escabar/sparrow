package io.rsbox.sparrow.updater

import io.rsbox.sparrow.asm.ClassGroup
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
 * Represents a JAR mapping updater.
 *
 * @property sourceGroup ClassGroup
 * @property referenceGroup ClassGroup
 * @constructor
 */
class Updater private constructor(val sourceGroup: ClassGroup, val referenceGroup: ClassGroup) {

    /**
     * Public constructor for loading JAR files into [ClassGroup]s
     *
     * @param sourceJar File
     * @param referenceJar File
     * @constructor
     */
    constructor(sourceJar: File, referenceJar: File) : this(ClassGroup.fromJar(sourceJar), ClassGroup.fromJar(referenceJar))

    /**
     * Matches all the classes, methods, and fields.
     *
     * @param threads Int
     * @param matchDelta Int
     */
    fun matchAll(threads: Int = 4, matchDelta: Int = 0) {

    }
}