package io.rsbox.sparrow.command

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.findOrSetObject
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option

/**
 * Copyright (c) 2020 RSBox
 *
 * Licensed under GNU General Public License v3.0
 * Please read the LICENSE file for more details.
 *
 * @author Kyle Escobar
 */

/**
 * The root Sparrow console command.
 *
 * @property config MutableMap<String, Any>
 */
class SparrowCommand : CliktCommand(
    name = "sparrow",
    help = "A dynamic OSRS updater and mappings generation toolbox.",
    printHelpOnEmptyArgs = true,
    invokeWithoutSubcommand = false
){

    /**
     * The command context configuration object
     */
    private val config by findOrSetObject { mutableMapOf<String, Any>() }

    /**
     * Enable verbose logging.
     */
    private val verbose by option("--verbose", help = "Enables verbose logging.").flag(default = false)

    override fun run() {
        config["verbose"] = verbose
    }
}