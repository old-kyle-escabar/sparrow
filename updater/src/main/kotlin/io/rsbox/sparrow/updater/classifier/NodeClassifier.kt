package io.rsbox.sparrow.updater.classifier

/**
 * Copyright (c) 2020 RSBox
 *
 * Licensed under GNU General Public License v3.0
 * Please read the LICENSE file for more details.
 *
 * @author Kyle Escobar
 */

/**
 * Represents a classifier class for ranking children components by similarity.
 *
 * @param T
 */
abstract class NodeClassifier<T> {

    /**
     * Private classifier storage.
     */
    private val classifiers = mutableListOf<Classifier<T>>()

    /**
     * Initialize and register classifiers.
     */
    abstract fun init()

    /**
     * Registers a classifier in the storage memory.
     *
     * @param classifier Classifier<T>
     * @param weight Int
     */
    internal fun register(classifier: Classifier<T>, weight: Int) {
        classifier.weight = weight
        classifiers.add(classifier)
    }
}