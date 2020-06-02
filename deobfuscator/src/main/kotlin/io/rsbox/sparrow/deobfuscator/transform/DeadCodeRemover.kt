package io.rsbox.sparrow.deobfuscator.transform

import io.rsbox.sparrow.deobfuscator.Transformer
import io.rsbox.sparrow.deobfuscator.asm.ClassGroup
import org.objectweb.asm.tree.analysis.Analyzer
import org.objectweb.asm.tree.analysis.BasicInterpreter
import org.tinylog.kotlin.Logger

/**
 * Copyright (c) 2020 RSBox
 *
 * Licensed under GNU General Public License v3.0
 * Please read the LICENSE file for more details.
 *
 * @author Kyle Escobar
 */

/**
 * Removes any dead frames which are never reached as a result
 * of reordering the control-flow.
 */
class DeadCodeRemover : Transformer {

    override val priority = 2

    override fun transform(group: ClassGroup) {
        var counter = 0

        group.forEach { c ->
            c.methods.forEach { m ->
                try {
                    val frames = Analyzer(BasicInterpreter()).analyze(c.name, m)
                    val insns = m.instructions.toArray()

                    for(i in frames.indices) {
                        if(frames[i] == null) {
                            m.instructions.remove(insns[i])
                            counter++
                        }
                    }
                } catch(e : Exception) {
                    Logger.error(e) { "Failed to remove dead code frame: ${c.name}.${m.name}${m.desc}" }
                }
            }
        }

        Logger.info("Removed $counter dead code frames.")
    }
}