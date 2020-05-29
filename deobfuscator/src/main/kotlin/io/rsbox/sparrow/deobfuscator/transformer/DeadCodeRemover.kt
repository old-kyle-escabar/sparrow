package io.rsbox.sparrow.deobfuscator.transformer

import io.rsbox.sparrow.asm.ClassGroup
import io.rsbox.sparrow.deobfuscator.Transformer
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
 * Responsible for removing unreachable code.
 */
class DeadCodeRemover : Transformer {

    override fun transform(group: ClassGroup) {
        var counter = 0

        group.classes.forEach { c ->
            c.methods.forEach { m ->
                try {
                    val frames = Analyzer(BasicInterpreter()).analyze(c.name, m.node)
                    val insns = m.node.instructions.toArray()
                    for(i in frames.indices) {
                        if(frames[i] == null) {
                            m.node.instructions.remove(insns[i])
                            counter++
                        }
                    }
                } catch(e : Exception) {
                    throw Exception("${c.name}.${m.name}${m.desc}", e)
                }
            }
        }

        Logger.info("Removed $counter dead code bytecode frames.")
    }
}