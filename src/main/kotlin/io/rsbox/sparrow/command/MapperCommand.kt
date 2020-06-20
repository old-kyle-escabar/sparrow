package io.rsbox.sparrow.command

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.file
import com.github.ajalt.clikt.parameters.types.int
import io.rsbox.sparrow.mapper.Mapper

/**
 * Copyright (c) 2020 RSBox
 *
 * Licensed under GNU General Public License v3.0
 * Please read the LICENSE file for more details.
 *
 * @author Kyle Escobar
 */

/**
 * The mapper command.
 */
class MapperCommand : CliktCommand(
    name = "mapper",
    help = "Analyzes two JAR files and creates mappings for them.",
    printHelpOnEmptyArgs = true,
    invokeWithoutSubcommand = false
) {

    private val sourceJar by argument(name = "Source", help = "The obfuscated JAR to be mapped.").file(mustExist = true, canBeDir = false)
    private val referenceJar by argument(name = "Reference", help = "Reference renamed JAR file to build mappings from.").file(mustExist = true, canBeDir = false)
    private val threads by option("-t", "--threads", help = "The number of threads to run the matching on.").int().default(4)

    override fun run() {
        val mapper = Mapper(sourceJar, referenceJar)
        mapper.classify()
    }
}