package io.rsbox.sparrow.deobfuscator.transform

import io.rsbox.sparrow.deobfuscator.Transformer
import io.rsbox.sparrow.deobfuscator.asm.ClassGroup
import org.jgrapht.Graph
import org.jgrapht.graph.DefaultDirectedGraph
import org.jgrapht.graph.DefaultEdge
import org.jgrapht.traverse.DepthFirstIterator
import org.objectweb.asm.commons.ClassRemapper
import org.objectweb.asm.commons.SimpleRemapper
import org.objectweb.asm.tree.ClassNode
import org.objectweb.asm.tree.FieldNode
import org.objectweb.asm.tree.MethodNode
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
 * Renames all the methods to a more readable format.
 */
class Renamer : Transformer {

    override fun transform(group: ClassGroup) {
        val mappings = mutableListOf<Mapping>()
        val hierarchy = group.buildHierarchyGraph()

        var classCounter = 0
        var methodCounter = 0
        var fieldCounter = 0

        /**
         * Build class mappings
         */
        group.forEach { c ->
            val mapping = Mapping()

            if(c.name.length <= 2) {
                mapping.classMapping = c to "class${++classCounter}"
            } else {
                mapping.classMapping = c to c.name
            }

            /**
             * First pass methods
             */
            c.methods.forEach { m ->
                val inheritedFrom = m.inheritedFrom(hierarchy, c)

                if(inheritedFrom.isEmpty()) {
                    mapping.methodMappings.add(m to "method${++methodCounter}")
                }
            }

            mappings.add(mapping)
        }

        /**
         * Generate the flat mapping hashmap
         */
        val flatMappings = hashMapOf<String, String>()
        mappings.forEach {
            flatMappings[it.classMapping.first.name] = it.classMapping.second
            it.methodMappings.forEach { m ->
                flatMappings[it.classMapping.first.name + "." + m.first.name + m.first.desc] = m.second
            }
        }

        /**
         * Apply the renaming tranformations.
         */
        val remapper = SimpleRemapper(flatMappings)

        group.forEachIndexed { index, c ->
            val node = ClassNode()
            c.accept(ClassRemapper(node, remapper))

            group[index] = node
        }

        Logger.info("Renamed '$classCounter classes', '$methodCounter methods', and '$fieldCounter fields'.")
    }

    private class Mapping {
        lateinit var classMapping: Pair<ClassNode, String>
        val methodMappings: MutableList<Pair<MethodNode, String>> = mutableListOf()
        val fieldMappings: MutableList<Pair<FieldNode, String>> = mutableListOf()
    }

    /**
     * Builds a JGrapht [Graph] for the class hierarchy.
     *
     * @receiver ClassGroup
     * @return Graph<ClassNode, DefaultEdge>
     */
    private fun ClassGroup.buildHierarchyGraph(): Graph<ClassNode, DefaultEdge> {
        val graph = DefaultDirectedGraph<ClassNode, DefaultEdge>(DefaultEdge::class.java)

        this.forEach { c ->
            graph.addVertex(c)
        }

        this.forEach { c ->
            val superNode = this[c.superName]
            if(graph.containsVertex(superNode)) {
                graph.addEdge(c, superNode)
            }

            c.interfaces.forEach { i ->
                val interfaceNode = this[i]
                if(graph.containsVertex(interfaceNode)) {
                    graph.addEdge(c, interfaceNode)
                }
            }
        }

        return graph
    }

    /**
     * Whether the method is inherited from a super class or interface.
     *
     * @receiver MethodNode
     * @param hierarchy Graph<ClassNode, DefaultEdge>
     * @param owner ClassNode
     * @return List<MethodNode>
     */
    private fun MethodNode.inheritedFrom(hierarchy: Graph<ClassNode, DefaultEdge>, owner: ClassNode): List<MethodNode> {
        val it = DepthFirstIterator(hierarchy, owner)

        val inheritedClasses = it.iterator().asSequence().toList()
            .filter { it != owner }
        val inheritedMethods = inheritedClasses.flatMap { it.methods }

        return inheritedMethods.filter { it.name == name && it.desc == desc }
    }
}