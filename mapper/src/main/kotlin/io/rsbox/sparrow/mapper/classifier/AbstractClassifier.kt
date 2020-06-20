package io.rsbox.sparrow.mapper.classifier

/**
 * Copyright (c) 2020 RSBox
 *
 * Licensed under GNU General Public License v3.0
 * Please read the LICENSE file for more details.
 *
 * @author Kyle Escobar
 */

/**
 * Represents an abstract classifier type.
 *
 * @param T
 */
abstract class AbstractClassifier<T> {

    /**
     * An internal classifiers registry
     */
    internal val classifiers = mutableListOf<Classifier<T>>()

    /**
     * Registers a [Classifier]
     * @receiver Classifier<T>
     */
    internal fun Classifier<T>.register() {
        classifiers.add(this)
    }

    /**
     * Initializes the classifier
     */
    abstract fun init()
}