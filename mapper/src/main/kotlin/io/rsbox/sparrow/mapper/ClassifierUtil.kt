package io.rsbox.sparrow.mapper

import io.rsbox.sparrow.asm.Class
import io.rsbox.sparrow.asm.Field
import io.rsbox.sparrow.asm.Matchable
import io.rsbox.sparrow.asm.Method
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
 * Contains utility methods for comparing parts of ASM data types and
 * computing a score based on their similarity.
 */
object ClassifierUtil {

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

    fun <T> compareSets(setA: MutableSet<T>, setB: MutableSet<T>): Double {
        val oldSize = setB.size
        setB.removeAll(setA)

        val matched = oldSize - setB.size
        val total = setA.size - matched + oldSize

        return (if(total == 0) 1 else matched / total).toDouble()
    }

    /**
     * Private utility methods
     */
}