package io.rsbox.sparrow.command

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.required
import com.github.ajalt.clikt.parameters.types.file
import io.rsbox.sparrow.TestClient

/**
 * Copyright (c) 2020 RSBox
 *
 * Licensed under GNU General Public License v3.0
 * Please read the LICENSE file for more details.
 *
 * @author Kyle Escobar
 */

class TestClientCommand : CliktCommand(
    name = "testclient",
    help = "Runs a test Jagex client with a given gamepack jar.",
    printHelpOnEmptyArgs = true
) {

    private val gamepack by option("-g", "--gamepack", help = "The gamepack to test with.").file(canBeDir = false, mustExist = true).required()

    override fun run() {
        val testClient = TestClient(gamepack)
        testClient.start()
    }
}