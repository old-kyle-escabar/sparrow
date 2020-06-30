package io.rsbox.sparrow.mapper.classifier

/**
 * Copyright (c) 2020 RSBox
 *
 * Licensed under GNU General Public License v3.0
 * Please read the LICENSE file for more details.
 *
 * @author Kyle Escobar
 */

interface Classifier<T> {

    /**
     * The name of the classifier
     */
    var name: String

    /**
     * The weight of the classifier.
     */
    var weight: Double

    /**
     * Calculates the score given two objects.
     *
     * @param source T
     * @param target T
     * @return Double
     */
    fun calculate(source: T, target: T): Double
}