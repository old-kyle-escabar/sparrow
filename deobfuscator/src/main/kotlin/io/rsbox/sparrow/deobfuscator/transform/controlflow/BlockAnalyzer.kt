package io.rsbox.sparrow.deobfuscator.transform.controlflow

import org.objectweb.asm.tree.AbstractInsnNode.*
import org.objectweb.asm.tree.MethodNode
import org.objectweb.asm.tree.analysis.Analyzer
import org.objectweb.asm.tree.analysis.BasicInterpreter
import org.objectweb.asm.tree.analysis.BasicValue

/**
 * Copyright (c) 2020 RSBox
 *
 * Licensed under GNU General Public License v3.0
 * Please read the LICENSE file for more details.
 *
 * @author Kyle Escobar
 */

class BlockAnalyzer : Analyzer<BasicValue>(BasicInterpreter()) {

    val blocks = ArrayList<Block>()

    override fun init(owner: String, method: MethodNode) {
        val insnList = method.instructions
        var currentBlock = Block()

        blocks.add(currentBlock)

        for(i in 0 until insnList.size()) {
            val insn = insnList[i]
            currentBlock.endIndex++

            if(insn.next == null) break
            if(insn.next.type == LABEL ||
                    insn.type == JUMP_INSN ||
                    insn.type == LOOKUPSWITCH_INSN ||
                    insn.type == TABLESWITCH_INSN) {
                currentBlock = Block()
                currentBlock.startIndex = i + 1
                currentBlock.endIndex = i + 1
                blocks.add(currentBlock)
            }
        }
    }

    override fun newControlFlowEdge(insnIndex: Int, successorIndex: Int) {
        val b1 = findBlock(insnIndex)
        val b2 = findBlock(successorIndex)

        if(b1 != b2) {
            if(insnIndex + 1 == successorIndex) {
                b1.next = b2
                b2.prev = b1
            } else {
                b1.branches.add(b2)
            }
        }
    }

    private fun findBlock(insnIndex: Int): Block {
        return blocks.first { insnIndex in it.startIndex until it.endIndex }
    }
}