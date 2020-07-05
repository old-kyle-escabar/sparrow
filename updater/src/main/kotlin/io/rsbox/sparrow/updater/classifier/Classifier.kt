package io.rsbox.sparrow.updater.classifier

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

    var weight: Int

    fun getScore(a: T, b: T): Double
}