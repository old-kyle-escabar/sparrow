package io.rsbox.sparrow.mapper

import com.google.common.util.concurrent.ThreadFactoryBuilder
import io.rsbox.sparrow.asm.Class
import io.rsbox.sparrow.asm.ClassGroup
import io.rsbox.sparrow.asm.Field
import io.rsbox.sparrow.asm.Method
import io.rsbox.sparrow.mapper.classifier.ClassClassifier
import org.tinylog.kotlin.Logger
import java.io.File

/**
 * Copyright (c) 2020 RSBox
 *
 * Licensed under GNU General Public License v3.0
 * Please read the LICENSE file for more details.
 *
 * @author Kyle Escobar
 */

/**
 * Represents a mapper which generates mappings for class groups.
 *
 * @property sourceJar File
 * @property referenceJar File
 * @constructor
 */
class Mapper(val sourceJar: File, val referenceJar: File) {

    /**
     * The source Jar class group
     */
    internal lateinit var sourceGroup: ClassGroup

    /**
     * The reference Jar class group
     */
    internal lateinit var referenceGroup: ClassGroup

    /**
     * The ranked classified results
     */
    private lateinit var classResults: Set<List<RankResult<Class>>>
    private lateinit var methodResults: Set<List<RankResult<Method>>>
    private lateinit var fieldResults: Set<List<RankResult<Field>>>

    /**
     * Loads both the source and reference JAR files.
     */
    fun loadJars() {
        Logger.info("Loading mapper JAR files.")

        sourceGroup = ClassGroup.fromJar(sourceJar)
        referenceGroup = ClassGroup.fromJar(referenceJar)

        Logger.info("Loaded Source JAR [${sourceGroup.classes.size} classes] Reference JAR [${referenceGroup.classes.size}].")
    }

    /**
     * Runs the analysis between both JAR files.
     *
     * @param threads The number of threads to run on.
     */
    fun classifyAll(threads: Int = 4) {
        Logger.info("Preparing to classify all in parallel.")

        classResults = classifyClasses()
    }

    /**
     * Classifies all the classes.
     *
     * @return List<RankResult<Class>>
     */
    private fun classifyClasses(): Set<List<RankResult<Class>>> {
        Logger.info("Analyzing classes...")

        val classifier = ClassClassifier()
        classifier.init()

        val results = hashSetOf<List<RankResult<Class>>>()

        sourceGroup.classes.forEach {
            val res = classifier.rank(it, referenceGroup.classes.toTypedArray(), it.group)
                .sortedByDescending { it.score }
            results.add(res)
        }

        return results
    }
}