package io.rsbox.sparrow.mapping

import io.rsbox.sparrow.asm.Class
import io.rsbox.sparrow.asm.Field
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

    /**
     * The field mappings.
     */
    val fieldMappings = mutableListOf<Mapping<Field>>()

    /**
     * Gets a [Class] that has been mapped to the target.
     *
     * @param target Class
     * @return Class?
     */
    fun getClassMapping(target: Class): Class? = classMappings.firstOrNull { it.mapped?.id == target.id }?.mapped

    /**
     * Gets a [Method] that has been mapped to the target.
     *
     * @param target Method
     * @return Method?
     */
    fun getMethodMapping(target: Method): Method? = methodMappings.firstOrNull { it.mapped?.id == target.id }?.mapped

    /**
     * Gets a [Field] that has been mapped to the target.
     *
     * @param target Field
     * @return Field?
     */
    fun getFieldMapping(target: Field): Field? = fieldMappings.firstOrNull { it.mapped?.id == target.id }?.mapped
}