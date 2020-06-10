package io.rsbox.sparrow.mapping

import io.rsbox.sparrow.asm.Class
import io.rsbox.sparrow.asm.Method

/**
 * Copyright (c) 2020 RSBox
 *
 * Licensed under GNU General Public License v3.0
 * Please read the LICENSE file for more details.
 *
 * @author Kyle Escobar
 */

/**
 * Represents a group of mappings for classes, methods, and fields.
 */
class Mappings {

    /**
     * Class mappings.
     */
    val classMappings = mutableListOf<Mapping<Class>>()

    /**
     * Method mappings.
     */
    val methodMappings = mutableListOf<Mapping<Method>>()
}