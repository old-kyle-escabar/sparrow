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
 * Represents an abstract classifier class.
 *
 * @param T
 */
abstract class AbstractClassifier<T> {

    /**
     * Private classifier registry.
     */
    private val classifiers = mutableListOf<Classifier<T>>()

    /**
     * Registers a classifier in the private registry.
     *
     * @receiver Classifier<T>
     */
    fun Classifier<T>.register(name: String, weight: Double) {
        this.name = name
        this.weight = weight

        classifiers.add(this)
    }

    /**
     * Abstract initialization of the classifier.
     */
    abstract fun init()
}