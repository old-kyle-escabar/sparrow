package io.rsbox.sparrow.deobfuscator

import io.rsbox.sparrow.asm.ClassGroup

/**
 * Copyright (c) 2020 RSBox
 *
 * Licensed under GNU General Public License v3.0
 * Please read the LICENSE file for more details.
 *
 * @author Kyle Escobar
 */

/**
 * Represents a bytecode transformer
 */
interface Transformer {

    /**
     * The number priority of the transformer.
     * lowest -> highest
     * lowest runs first.
     * highest runs last.
     */
    val priority: Int

    /**
     * Execute the transformer logic.
     *
     * @param group ClassGroup
     */
    fun transform(group: ClassGroup)

}