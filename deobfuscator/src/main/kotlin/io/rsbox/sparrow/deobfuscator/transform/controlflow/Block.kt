package io.rsbox.sparrow.deobfuscator.transform.controlflow

/**
 * Copyright (c) 2020 RSBox
 *
 * Licensed under GNU General Public License v3.0
 * Please read the LICENSE file for more details.
 *
 * @author Kyle Escobar
 */

class Block {

    var startIndex = 0

    var endIndex = 0

    var prev: Block? = null

    var next: Block? = null

    val origin: Block get() {
        var curr = this
        var last = prev

        while(last != null) {
            curr = last
            last = curr.prev
        }

        return curr
    }

    val branches = ArrayList<Block>()
}