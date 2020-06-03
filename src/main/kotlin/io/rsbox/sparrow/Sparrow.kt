package io.rsbox.sparrow

import com.github.ajalt.clikt.core.subcommands
import io.rsbox.sparrow.command.DeobfuscatorCommand
import io.rsbox.sparrow.command.SparrowCommand

/**
 * Copyright (c) 2020 RSBox
 *
 * Licensed under GNU General Public License v3.0
 * Please read the LICENSE file for more details.
 *
 * @author Kyle Escobar
 */

class Sparrow {

    companion object {
        /**
         * Static main method
         */
        @JvmStatic
        fun main(args: Array<String>) {
            /**
             * Invoke Sparrow command line.
             */

            val subcommands = listOf(
                DeobfuscatorCommand()
            )

            SparrowCommand()
                .subcommands(subcommands)
                .main(args)
        }
    }
}