package io.rsbox.sparrow.mapper

/**
 * Copyright (c) 2020 RSBox
 *
 * Licensed under GNU General Public License v3.0
 * Please read the LICENSE file for more details.
 *
 * @author Kyle Escobar
 */

data class RankResult<T>(val subject: T, val target: T, val score: Double)