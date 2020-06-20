package io.rsbox.sparrow.mapper.classifier

import io.rsbox.sparrow.asm.ClassGroup

/**
 * Copyright (c) 2020 RSBox
 *
 * Licensed under GNU General Public License v3.0
 * Please read the LICENSE file for more details.
 *
 * @author Kyle Escobar
 */

interface Classifier<T> {

    val name: String

    val weight: Double

    fun calculateScore(src: T, target: T, group: ClassGroup): Double
}