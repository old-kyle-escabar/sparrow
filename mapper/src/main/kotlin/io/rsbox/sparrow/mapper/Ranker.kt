package io.rsbox.sparrow.mapper

import io.rsbox.sparrow.asm.ClassGroup

/**
 * Copyright (c) 2020 RSBox
 *
 * Licensed under GNU General Public License v3.0
 * Please read the LICENSE file for more details.
 *
 * @author Kyle Escobar
 */

interface Ranker<T> {
    fun rank(src: T, targets: Array<T>, group: ClassGroup): List<RankResult<T>>
}