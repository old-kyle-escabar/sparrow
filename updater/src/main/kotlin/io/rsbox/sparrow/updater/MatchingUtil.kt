package io.rsbox.sparrow.updater

import io.rsbox.sparrow.asm.*
import java.util.*
import kotlin.math.abs
import kotlin.math.max

/**
 * Copyright (c) 2020 RSBox
 *
 * Licensed under GNU General Public License v3.0
 * Please read the LICENSE file for more details.
 *
 * @author Kyle Escobar
 */

/**
 * Holds global utility methods for comparing data sets used for matching.
 */
object MatchingUtil {

    fun checkPotentialEquality(a: Class, b: Class): Boolean {
        if(a == b) return true
        if(a.match != null) return a.match == b
        if(b.match != null) return b.match == a
        return true
    }

    fun checkPotentialEquality(a: Method, b: Method): Boolean {
        if(a == b) return true
        if(a.match != null) return a.match == b
        if(b.match != null) return b.match == a
        if(!checkPotentialEquality(a.owner, b.owner)) return false
        return true
    }

    fun checkPotentialEquality(a: Field, b: Field): Boolean {
        if(a == b) return true
        if(a.match != null) return a.match == b
        if(b.match != null) return b.match == a
        if(!checkPotentialEquality(a.owner, b.owner)) return false
        return true
    }

    fun compareCounts(countA: Int, countB: Int): Double {
        val delta = abs(countA - countB)
        if(delta == 0) return 1.0

        return (1 - delta / max(countA, countB)).toDouble()
    }

    inline fun <reified T> compareSets(setA: MutableSet<T>, setB: MutableSet<T>): Double {
        val oldSize = setB.size

        val cloned = setB.toTypedArray().copyOf(oldSize).toMutableList()

        cloned.removeAll(setA)

        val matched = oldSize - cloned.size
        val total = setA.size - matched + oldSize

        return if(total == 0) 1.0 else (matched / total).toDouble()
    }

    private inline fun <reified T : Matchable<*>> compareNodeSets(setA: MutableSet<T>, setB: MutableSet<T>, predicate: (T, T) -> Boolean): Double {
        if(setA.isEmpty() || setB.isEmpty()) {
            return if(setA.isEmpty() && setB.isEmpty()) 1.0 else 0.0
        }

        val clonedA = setA.toTypedArray().copyOf(setA.size).toMutableList()
        val clonedB = setB.toTypedArray().copyOf(setB.size).toMutableList()

        val total = setA.size + setB.size
        var unmatched = 0


    }
}