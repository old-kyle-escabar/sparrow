package io.rsbox.sparrow.deobfuscator

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
     * Runs the transformer logic.
     *
     * @param group ClassGroup
     */
    fun transform(group: ClassGroup)
}