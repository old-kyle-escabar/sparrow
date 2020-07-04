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

    constructor(sourceJar: File, referenceJar: File) : this(ClassGroup.fromJar(sourceJar), ClassGroup.fromJar(referenceJar))
}