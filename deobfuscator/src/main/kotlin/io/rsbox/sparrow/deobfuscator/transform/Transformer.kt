package io.rsbox.sparrow.deobfuscator.transform

import io.rsbox.sparrow.deobfuscator.asm.ClassGroup

/**
 * Copyright (c) 2020 RSBox
 *
 * Licensed under GNU General Public License v3.0
 * Please read the LICENSE file for more details.
 *
 * @author Kyle Escobar
 */

interface Transformer {

    /**
     * The order in which the transformers are loaded.
     * Lowest = first to run.
     */
    val priority: Int

    /**
     * Runs the transformer logic.
     *
     * @param group ClassGroup
     */
    fun transform(group: ClassGroup)
}