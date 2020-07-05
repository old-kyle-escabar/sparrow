package io.rsbox.sparrow.updater.classifier

import io.rsbox.sparrow.asm.Class
import io.rsbox.sparrow.updater.MatchingUtil
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
 * Responsible for classifying classes
 */
class ClassClassifier : NodeClassifier<Class>() {

    override fun init() {
        register(classTypeCheck, 20)
        register(hierarchyDepth, 1)
    }

    companion object {
        /**
         * Class type check classifier
         */
        private val classTypeCheck = classifier<Class>("class type check") { a, b ->
            val mask = ACC_ENUM or ACC_INTERFACE or ACC_ANNOTATION or ACC_ABSTRACT
            val resA = a.access and mask
            val resB = b.access and mask

            (1 - (Integer.bitCount(resA.toDouble().pow(resB).toInt()) / 4)).toDouble()
        }

        /**
         * Calculates the number of parent classes two given class have.
         */
        private val hierarchyDepth = classifier<Class>("hierarchy depth") { a, b ->
            var countA = 0
            var countB = 0

            var classA = a.group[a.superName]
            while(classA != null) {
                classA = a.group[classA.superName]
                countA++
            }

            var classB = b.group[b.superName]
            while(classB != null) {
                classB = b.group[classB.superName]
                countB++
            }

            MatchingUtil.compareCounts(countA, countB)
        }
    }
}