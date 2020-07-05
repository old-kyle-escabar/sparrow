package io.rsbox.sparrow.updater.classifier

/**
 * Copyright (c) 2020 RSBox
 *
 * Licensed under GNU General Public License v3.0
 * Please read the LICENSE file for more details.
 *
 * @author Kyle Escobar
 */

internal fun <T> classifier(name: String, logic: (T, T) -> Double ): Classifier<T> {
    val dsl = ClassifierDsl<T>(name)
    dsl.setLogic(logic)
    return dsl.build()
}

internal class ClassifierDsl<T>(val name: String) {

    private lateinit var logic: (T, T) -> Double

    fun setLogic(logic: (T, T) -> Double) {
        this.logic = logic
    }

    fun build(): Classifier<T> {
        return object : Classifier<T> {
            override val name = this@ClassifierDsl.name
            override var weight = 1
            override fun getScore(a: T, b: T): Double {
                return this@ClassifierDsl.logic(a, b)
            }
        }
    }
}