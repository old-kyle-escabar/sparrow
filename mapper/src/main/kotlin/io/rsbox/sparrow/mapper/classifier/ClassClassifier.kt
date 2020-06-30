package io.rsbox.sparrow.mapper.classifier

import io.rsbox.sparrow.asm.Class
import org.objectweb.asm.Opcodes.*
import kotlin.math.pow

/**
 * Copyright (c) 2020 RSBox
 *
 * Licensed under GNU General Public License v3.0
 * Please read the LICENSE file for more details.
 *
 * @author Kyle Escobar
 */

/**
 * Responsible for classifying classes together based on traits.
 */
class ClassClassifier : AbstractClassifier<Class>() {

    override fun init() {
        classTypeCheck.register("class type check", 20.0)
    }

    companion object {
        /**
         * Class type check classifier.
         */
        private val classTypeCheck = classifier<Class> { source, target ->
            val mask = ACC_ENUM or ACC_INTERFACE or ACC_ABSTRACT or ACC_ANNOTATION

            val resultSource = source.access and mask
            val resultTarget = target.access and mask

            (1 - Integer.bitCount(resultSource.toDouble().pow(resultTarget).toInt()) / 4).toDouble()
        }
    }
}