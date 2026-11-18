// PROG2 VT2026
// Grupp 043
// Noah Ander 
// Filippa Colbin
// Natalija Karlsson
package se.su.inlupp;

import java.util.*;

/**
 * Söker väg i grafen med  Breadth-First Search (BFS).
 * Hittar alltid vägen med minst antal kanter. 
 *
 * @param <T> typen av noder i grafen
 */
public class BFSPathFinder<T> implements PathFinder<T> {

    @Override
    public Path<T> findPath(Graph<T> graph, T from, T to) {
        // Kontroll att båda noderna finns
        if (!graph.hasNode(from) || !graph.hasNode(to)) return null;

        // Direkt väg om start och mål är samma
        if (from.equals(to)) return new GraphPath<>(from, new ArrayList<>());

        // predecessor[node] = noden vi kom från + kanten vi använde
        Map<T, T> prevNode = new HashMap<>();
        Map<T, Edge<T>> prevEdge = new HashMap<>();
        prevNode.put(from, null);

        // Används till BFS-sökning
        Queue<T> queue = new LinkedList<>();
        queue.add(from);

        while (!queue.isEmpty()) {
            T current = queue.poll();
            for (Edge<T> edge : graph.getEdgesFrom(current)) {
                T neighbor = edge.getDestination();

                // Besöker endast obesökta noder
                if (!prevNode.containsKey(neighbor)) {
                    prevNode.put(neighbor, current);
                    prevEdge.put(neighbor, edge);

                    // Avslutar direkt när målet hittas
                    if (neighbor.equals(to)) {
                        return buildPath(from, to, prevNode, prevEdge);
                    }
                    queue.add(neighbor);
                }
            }
        }
        return null;
    }

    private Path<T> buildPath(T from, T to, Map<T, T> prevNode, Map<T, Edge<T>> prevEdge) {
        LinkedList<Edge<T>> edges = new LinkedList<>();
        T current = to;
        // Skapar vägen baklänges från mål till start
        while (!current.equals(from)) {
            edges.addFirst(prevEdge.get(current));
            current = prevNode.get(current);
        }
        return new GraphPath<>(from, new ArrayList<>(edges));
    }
}
