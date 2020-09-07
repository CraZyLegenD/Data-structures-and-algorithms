import graphs.Edge
import graphs.EdgeType
import graphs.Graph
import graphs.Vertex
import graphs.adjacency.AdjacencyList
import queue.priority.AbstractPriorityQueue
import queue.priority.comparator.ComparatorPriorityQueue
import kotlin.Comparator
import kotlin.math.roundToInt

/**
 * Created by crazy on 9/7/20 to long live and prosper !
 */

object Prim {

    private fun <T> addAvailableEdges(vertex: Vertex<T>, graph: Graph<T>, visited: Set<Vertex<T>>, priorityQueue: AbstractPriorityQueue<Edge<T>>) {
        graph.edges(vertex).forEach { edge ->
            if (edge.destination !in visited) { priorityQueue.enqueue(edge) }
        }
    }

    fun <T> produceMinimumSpanningTree(graph: AdjacencyList<T>): Pair<Double, AdjacencyList<T>> {
        var cost = 0.0
        val adjacencyList = AdjacencyList<T>()
        val visited = mutableSetOf<Vertex<T>>()
        val comparator = Comparator<Edge<T>> { first, second ->
            val firstWeight = first.weight ?: 0.0
            val secondWeight = second.weight ?: 0.0
            (secondWeight - firstWeight).roundToInt()
        }
        val priorityQueue = ComparatorPriorityQueue(comparator)
        adjacencyList.copyVertices(graph)

        val start = graph.allVertices.firstOrNull() ?: return Pair(cost, adjacencyList)

        visited.add(start)
        addAvailableEdges(start, graph, visited, priorityQueue)

        while (true) {
            val smallestEdge = priorityQueue.dequeue() ?: break
            val vertex = smallestEdge.destination
            if (vertex in visited) continue
            visited.add(vertex)
            cost += smallestEdge.weight ?: 0.0
            adjacencyList.add(EdgeType.UNDIRECTED, smallestEdge.source, smallestEdge.destination, smallestEdge.weight)
            addAvailableEdges(vertex, graph, visited, priorityQueue)
        }

        return Pair(cost, adjacencyList)
    }

}
