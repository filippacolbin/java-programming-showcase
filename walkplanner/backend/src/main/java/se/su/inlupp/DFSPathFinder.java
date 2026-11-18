// PROG2 VT2026
// Grupp 043
// Noah Ander 
// Filippa Colbin
// Natalija Karlsson
package se.su.inlupp;

import java.util.*;

/**
 * Söker väg i grafen med Depth-First Search (DFS).
 * Hittar möjlig väg men inte nödvändigt vis den kortaste.
 *
 * @param <T> typer av noder i grafen
 */
public class DFSPathFinder<T> implements PathFinder<T> {

    @Override
    public Path<T> findPath(Graph<T> graph, T from, T to) {
        // Kontroll om våda noderna finns
        if (!graph.hasNode(from) || !graph.hasNode(to)) return null;
        // Direkt väg om start och mål är samma nod
        if (from.equals(to)) return new GraphPath<>(from, new ArrayList<>());

        List<Edge<T>> result = new ArrayList<>();
        // Håller reda på besökta noder
        Set<T> visited = new HashSet<>();

        // Startar DFS-sökningen
        if (dfs(graph, from, to, visited, result)) {
            return new GraphPath<>(from, result);
        }
        return null;
    }

    private boolean dfs(Graph<T> graph, T current, T target,
                        Set<T> visited, List<Edge<T>> path) {
        // Markerar noden som besökt                   
        visited.add(current);

        for (Edge<T> edge : graph.getEdgesFrom(current)) {
            T neighbor = edge.getDestination();
            // Besöker endast obesäkta noder
            if (!visited.contains(neighbor)) {
                path.add(edge);
                // Målet hittat
                if (neighbor.equals(target)) return true;

                // Sökning vidare i grafen
                if (dfs(graph, neighbor, target, visited, path)) return true;

                // Backtrack om inte vägen leder till målet 
                path.remove(path.size() - 1);
            }
        }

        return false;
    }
}
