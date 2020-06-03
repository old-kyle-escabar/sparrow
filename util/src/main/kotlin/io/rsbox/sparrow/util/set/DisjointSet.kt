package io.rsbox.sparrow.util.set

/**
 * Copyright (c) 2020 RSBox
 *
 * Licensed under GNU General Public License v3.0
 * Please read the LICENSE file for more details.
 *
 * @author Kyle Escobar
 */

/**
 * Represents a DisjointSet data structure.
 * Used to find unioned objects which intersect similar data types between
 * two mutable sets.
 *
 * @param T
 */
class DisjointSet<T> {

    /**
     * The backing set storage map.
     */
    private val map = hashMapOf<T, Node<T>>()

    /**
     * The backing type object collection
     */
    private val collection = hashSetOf<T>()

    /**
     * Represents a data object.
     *
     * @param T
     * @constructor
     */
    private class Node<T>(val data: T) {
        /**
         * The rank of the node.
         */
        var rank = 0

        /**
         * The parent of the node.
         */
        var parent = this

        /**
         * The node branching size.
         */
        var size = 0
    }

    /**
     * Whether the data has been set.
     *
     * @param data T
     * @return Boolean
     */
    fun exists(data: T): Boolean = map.containsKey(data)

    /**
     * The number of collections.
     */
    val collections get() = collection.size

    /**
     * Gets an iterator of the collection.
     *
     * @return Iterator<T>
     */
    fun iterator(): Iterator<T> = collection.iterator()

    /**
     * Find data in a set give a generic object.
     *
     * @param data T
     * @return T
     */
    fun findSet(data: T): T {
        return findSet(map[data] ?: error("Data '$data' not found in set.")).data
    }

    /**
     * Finds a data set given a [Node] object.
     *
     * @param node Node<T>
     * @return Node<T>
     */
    private fun findSet(node: Node<T>): Node<T> {
        val parent = node.parent

        return if(parent == node) {
            parent
        } else {
            node.parent = findSet(node.parent)
            node.parent
        }
    }

    /**
     * Gets the number of entries for a data set.
     *
     * @param data T
     * @return Int
     */
    fun count(data: T): Int {
        return findSet(map[data] ?: error("Data '$data' not found in set.")).size
    }

    /**
     * Adds data to a collection.
     *
     * @param data T
     * @return Boolean
     */
    fun add(data: T): Boolean {
        if(map.containsKey(data)) {
            return false
        }

        val node = Node(data)
        map[data] = node
        collection.add(node.data)

        return true
    }

    /**
     * Creates a union between two objects in the disjoint set.
     *
     * @param data1 T
     * @param data2 T
     * @return Boolean
     */
    fun union(data1: T, data2: T): Boolean {
        val node1 = map[data1] ?: error("Data '$data1' not found in set.")
        val node2 = map[data2] ?: error("Data '$data2' not found in set.")
        val parent1 = findSet(node1)
        val parent2 = findSet(node2)

        if(parent1.data!! == parent2.data) {
            return false
        }

        if(parent1.rank >= parent2.rank) {
            parent1.rank = if(parent1.rank == parent2.rank) parent1.rank + 1 else parent1.rank
            parent2.parent = parent1
            parent1.size += parent2.size
            collection.remove(parent2.data)
        } else {
            parent1.parent = parent2
            parent2.size += parent1.size
            collection.remove(parent1.data)
        }

        return true
    }
}