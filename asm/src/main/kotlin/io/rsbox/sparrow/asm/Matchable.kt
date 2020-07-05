package io.rsbox.sparrow.asm

/**
 * Copyright (c) 2020 RSBox
 *
 * Licensed under GNU General Public License v3.0
 * Please read the LICENSE file for more details.
 *
 * @author Kyle Escobar
 */

/**
 * Represents a matchable type.
 *
 * @param T
 */
interface Matchable<T> : Node<T> {

    /**
     * The matched type. Null by default.
     */
    var match: T?
}