package io.rsbox.sparrow.mapper.classifier

import io.rsbox.sparrow.asm.Class
import io.rsbox.sparrow.asm.ClassGroup
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
 * Classifies classes
 */
class ClassClassifier : AbstractClassifier<Class>() {

    override fun init() {
        classTypeCheck.register()
    }

    companion object {
        /**
         * Class type check classifier.
         */
        private val classTypeCheck = object : Classifier<Class> {
            override val name = "class type check"

            override val weight = 20.0

            override fun calculateScore(src: Class, target: Class, group: ClassGroup): Double {
                val mask = ACC_ENUM or ACC_INTERFACE or ACC_ANNOTATION or ACC_ABSTRACT
                val resultA = src.access and mask
                val resultB = target.access and mask

                return (1 - Integer.bitCount(resultA.toDouble().pow(resultB).toInt()) / 4).toDouble()
            }
        }
    }
}