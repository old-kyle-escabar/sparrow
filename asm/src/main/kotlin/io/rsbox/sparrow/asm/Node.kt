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
 * Represents an ASM node type.
 *
 * @param T
 */
interface Node<T> {

    /**
     * Whether the name of this node is obfuscated.
     *
     * @return Boolean
     */
    fun isNameObfuscated(): Boolean
}