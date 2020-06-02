package io.rsbox.sparrow.deobfuscator.transform.euclidean

import java.math.BigInteger

/**
 * Copyright (c) 2020 RSBox
 *
 * Licensed under GNU General Public License v3.0
 * Please read the LICENSE file for more details.
 *
 * @author Kyle Escobar
 */

private val UNSIGNED_INT_MODULUS = BigInteger.ONE.shiftLeft(Integer.SIZE)

private val UNSIGNED_LONG_MODULUS = BigInteger.ONE.shiftLeft(java.lang.Long.SIZE)

fun invert(n: Int): Int = n.toBigInteger().modInverse(UNSIGNED_INT_MODULUS).toInt()

fun invert(n: Long): Long = n.toBigInteger().modInverse(UNSIGNED_LONG_MODULUS).toLong()

fun invert(n: Number): Number {
    return when (n) {
        is Int -> invert(n)
        is Long -> invert(n)
        else -> error(n)
    }
}

fun isInvertible(n: Int): Boolean = n and 1 == 1

fun isInvertible(n: Long): Boolean = isInvertible(n.toInt())

fun isInvertible(n: Number): Boolean {
    return when (n) {
        is Int, is Long -> isInvertible(n.toInt())
        else -> error(n)
    }
}