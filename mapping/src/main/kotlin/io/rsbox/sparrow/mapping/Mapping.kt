package io.rsbox.sparrow.mapping

/**
 * Copyright (c) 2020 RSBox
 *
 * Licensed under GNU General Public License v3.0
 * Please read the LICENSE file for more details.
 *
 * @author Kyle Escobar
 */

/**
 * An abstract representation of a mapped type.
 *
 * @param T
 */
interface Mapping<T> {

    /**
     * The original object.
     */
    val origin: T

    /**
     * The mapped object.
     * No mapping should be set to null
     */
    var mapped: T?

    /**
     * Does this object have a mapping.
     *
     * @return Boolean
     */
    fun hasMapping(): Boolean {
        return mapped != null
    }
}