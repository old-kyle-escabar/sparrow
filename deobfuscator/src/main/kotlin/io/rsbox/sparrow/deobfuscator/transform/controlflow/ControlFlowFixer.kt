package io.rsbox.sparrow.deobfuscator.transform.controlflow

import io.rsbox.sparrow.deobfuscator.Transformer
import io.rsbox.sparrow.deobfuscator.asm.ClassGroup
import org.objectweb.asm.tree.InsnList
import org.objectweb.asm.tree.LabelNode
import org.tinylog.kotlin.Logger
import java.util.*
import kotlin.collections.AbstractMap

/**
 * Copyright (c) 2020 RSBox
 *
 * Licensed under GNU General Public License v3.0
 * Please read the LICENSE file for more details.
 *
 * @author Kyle Escobar
 */

class ControlFlowFixer : Transformer {

    override fun transform(group: ClassGroup) {
        var counter = 0

        group.forEach { c ->
            c.methods.forEach { m ->
                if(m.tryCatchBlocks.isEmpty()) {
                    val analyzer = BlockAnalyzer()
                    analyzer.analyze(c.name, m)
                    m.instructions = buildInsnList(m.instructions, analyzer.blocks)

                    counter += analyzer.blocks.size
                }
            }
        }

        Logger.info("Reordered $counter control-flow blocks.")
    }

    private fun buildInsnList(insnList: InsnList, blocks: List<Block>): InsnList {
        val instructions = InsnList()

        if(blocks.isEmpty()) {
            return instructions
        }

        val labelMap = LabelMap()
        val stack: Queue<Block> = Collections.asLifoQueue(ArrayDeque())
        val placed = HashSet<Block>()

        stack.add(blocks.first())

        while(stack.isNotEmpty()) {
            val block = stack.remove()
            if(block in placed) continue
            placed.add(block)

            block.branches.forEach { stack.add(it.origin) }
            block.next?.let { stack.add(it) }

            for(i in block.startIndex until block.endIndex) {
                instructions.add(insnList[i].clone(labelMap))
            }
        }

        return instructions
    }

    private class LabelMap : AbstractMap<LabelNode, LabelNode>() {

        private val map = HashMap<LabelNode, LabelNode>()

        override val entries get() = throw IllegalStateException()
        override fun get(key: LabelNode): LabelNode = map.getOrPut(key) { LabelNode() }
    }
}