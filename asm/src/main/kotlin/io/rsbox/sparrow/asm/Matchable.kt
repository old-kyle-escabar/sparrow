package io.rsbox.sparrow.asm

/**
 * Copyright (c) 2020 RSBox
 *
 * Licensed under GNU General Public License v3.0
 * Please read the LICENSE file for more details.
 *
 * @author Kyle Escobar
 */

abstract class Matchable<T> {

    var match: T? = null

    val isMatched: Boolean get() {
        return match != null
    }
}