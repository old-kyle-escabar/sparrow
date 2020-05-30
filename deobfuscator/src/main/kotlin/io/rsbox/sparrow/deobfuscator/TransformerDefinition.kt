package io.rsbox.sparrow.deobfuscator

import kotlin.reflect.KClass

/**
 * Copyright (c) 2020 RSBox
 *
 * Licensed under GNU General Public License v3.0
 * Please read the LICENSE file for more details.
 *
 * @author Kyle Escobar
 */

annotation class TransformerDefinition(val dependsOn: KClass<out Transformer> = Transformer::class)