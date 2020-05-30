package io.rsbox.sparrow.asm

import org.objectweb.asm.tree.ClassNode

/**
 * Copyright (c) 2020 RSBox
 *
 * Licensed under GNU General Public License v3.0
 * Please read the LICENSE file for more details.
 *
 * @author Kyle Escobar
 */

class Class(val group: ClassGroup, val asm: ClassNode) {

    override fun toString(): String = asm.name
}