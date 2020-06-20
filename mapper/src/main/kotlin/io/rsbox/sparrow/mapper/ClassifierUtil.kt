package io.rsbox.sparrow.mapper

import io.rsbox.sparrow.asm.Class
import io.rsbox.sparrow.asm.Field
import io.rsbox.sparrow.asm.Matchable
import io.rsbox.sparrow.asm.Method
import org.objectweb.asm.tree.AbstractInsnNode
import org.objectweb.asm.tree.AbstractInsnNode.INT_INSN
import org.objectweb.asm.tree.AbstractInsnNode.VAR_INSN
import org.objectweb.asm.tree.IntInsnNode
import org.objectweb.asm.tree.VarInsnNode
import java.util.function.BiPredicate
import java.util.function.ToIntBiFunction
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

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

    /**
     * Compares method instructions between two given methods.
     *
     * @param insnA AbstractInsnNode
     * @param insnB AbstractInsnNode
     * @param listA T
     * @param listB T
     * @param posProvider ToIntBiFunction<T, AbstractInsnNode>
     * @param methodA Method
     * @param methodB Method
     * @return Boolean
     */
    private fun <T> compareInstructions(
        insnA: AbstractInsnNode,
        insnB: AbstractInsnNode,
        listA: T,
        listB: T,
        posProvider: ToIntBiFunction<T, AbstractInsnNode>,
        methodA: Method?,
        methodB: Method?
    ) : Boolean {
        if(insnA.opcode != insnB.opcode) return false

        when(insnA.type) {
            /**
             * INT Instruction type
             */
            INT_INSN -> {
                val a = insnA as IntInsnNode
                val b = insnB as IntInsnNode

                return a.operand == b.operand
            }

            /**
             * VAR Instruction type
             */
            VAR_INSN -> {
                // TODO
            }
        }

        return true
    }

    private fun <T, U> compareLists(listA: T, listB: T, elementConsumer: ListElementConsumer<T, U>, sizeConsumer: ListSizeConsumer<T>, elementComparator: BiPredicate<U, U>): Double {
        val sizeA = sizeConsumer.apply(listA)
        val sizeB = sizeConsumer.apply(listB)

        if(sizeA == 0 && sizeB == 0) return 1.0
        if(sizeA == 0 || sizeB == 0) return 0.0

        if(sizeA == sizeB) {
            var match = true

            for(i in 0 until sizeA) {
                if(!elementComparator.test(elementConsumer.apply(listA, i), elementConsumer.apply(listB, i))) {
                    match = false
                    break
                }
            }

            if(match) return 1.0
        }

        val v0 = IntArray(sizeB + 1)
        val v1 = IntArray(sizeB + 1)

        for(i in v0.indices) {
            v0[i] = i
        }

        for(i in 0 until sizeA) {
            v1[0] = i + 1

            for(j in 0 until sizeB) {
                val cost = if(elementComparator.test(elementConsumer.apply(listA, i), elementConsumer.apply(listB, j))) 0.0 else 1.0
                v1[j + 1] = min(min(v1[j] + 1, v0[j + 1] + 1), (v0[j] + cost).toInt())
            }

            for(j in v0.indices) {
                v0[j] = v1[j]
            }
        }

        val distance = v1[sizeB]
        val upperBound = max(sizeA, sizeB)
        assert(distance in 0..upperBound)

        return (1 - distance / upperBound).toDouble()
    }

    private interface ListElementConsumer<T, U> {
        fun apply(list: T, pos: Int): U
    }

    private interface ListSizeConsumer<T> {
        fun apply(list: T): Int
    }
}