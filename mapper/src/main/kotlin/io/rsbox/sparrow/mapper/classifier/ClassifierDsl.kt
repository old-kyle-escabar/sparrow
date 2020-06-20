package io.rsbox.sparrow.mapper.classifier

import kotlin.math.log

/**
 * Copyright (c) 2020 RSBox
 *
 * Licensed under GNU General Public License v3.0
 * Please read the LICENSE file for more details.
 *
 * @author Kyle Escobar
 */

/**
 * Classifier DSL builder.
 *
 * @param init [@kotlin.ExtensionFunctionType] Function3<ClassifierDsl<T>, [@kotlin.ParameterName] T, [@kotlin.ParameterName] T, Double>
 * @return Classifier<T>
 */
fun <T> classifier(init: (source: T, target: T) -> Double): Classifier<T> {
    val classifier = ClassifierDsl<T>()
    classifier.setLogic(init)
    return classifier.build()
}

/**
 * Classifier DSL class container.
 *
 * @param T
 * @property logic Function2<[@kotlin.ParameterName] T, [@kotlin.ParameterName] T, Double>
 */
class ClassifierDsl<T> {

    /**
     * The logic to calculate a classifier score.
     */
    private lateinit var logic: (source: T, target: T) -> Double

    /**
     * Sets the logic
     *
     * @param logic Function2<[@kotlin.ParameterName] T, [@kotlin.ParameterName] T, Double>
     */
    internal fun setLogic(logic: (source: T, target: T) -> Double) {
        this.logic = logic
    }

    /**
     * Builds the classifier object.
     *
     * @return Classifier<T>
     */
    fun build(): Classifier<T> {
        /**
         * Return a object of the [Classifier] interface.
         */
        return object : Classifier<T> {
            override var name: String = ""

            override var weight: Double = 0.0

            override fun calculate(source: T, target: T): Double {
                return logic(source, target)
            }
        }
    }
}