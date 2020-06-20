package io.rsbox.sparrow.mapper.classifier

import io.rsbox.sparrow.asm.ClassGroup
import io.rsbox.sparrow.mapper.RankResult
import io.rsbox.sparrow.mapper.Ranker

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
abstract class AbstractClassifier<T> : Ranker<T> {

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
     * Ranks the classifier targets.
     *
     * @param src T
     * @param targets Array<T>
     * @param group ClassGroup
     * @return List<RankResult<T>>
     */
    override fun rank(src: T, targets: Array<T>, group: ClassGroup): List<RankResult<T>> {
        val results = mutableListOf<RankResult<T>>()

        targets.forEach { target ->
            var totalScore = 0.0

            classifiers.forEach { classifier ->
                val score = classifier.calculateScore(src, target, group) * classifier.weight
                totalScore += score
            }

            val result = RankResult(src, target, totalScore)
            results.add(result)
        }

        return results
    }

    /**
     * Initializes the classifier
     */
    abstract fun init()
}