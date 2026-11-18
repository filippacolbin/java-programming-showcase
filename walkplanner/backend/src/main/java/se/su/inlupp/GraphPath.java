// PROG2 VT2026
// Grupp 043
// Noah Ander 
// Filippa Colbin
// Natalija Karlsson
package se.su.inlupp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Oförändlig implementation av Path interface.
 * Representerar en väg genom en graf som en ordnad lista av kanter.
 *
 * @param <T> typen av noder i vägen
 */
public class GraphPath<T> implements Path<T> {

    // Vägens startnod
    private final T start;
    // Kanterna lagras i ordning från start till mål
    private final List<Edge<T>> edges;

    /**
     * Skapar ny väg genom grafen
     * @param start vägens startnod
     * @param edges lista av kanter i ordning från start till mål
     */
    public GraphPath(T start, List<Edge<T>> edges) {
        this.start = start;
        // Skapar oföränderlig kopia från externa förändringar 
        this.edges = Collections.unmodifiableList(new ArrayList<>(edges));
    }

    @Override
    public T getStart() {
        return start;
    }

    @Override
    public T getEnd() {
        // Om vägen saknar kanter är start och slut samma nod
        if (edges.isEmpty()) return start;

        // Sista kanten är vägens slutdestination
        return edges.get(edges.size() - 1).getDestination();
    }

    @Override
    public int getTotalTime() {
        int total = 0;
        // Summerar tiden för alla kanter i vägen
        for (Edge<T> edge : edges) total += edge.getTime();
        return total;
    }

    @Override
    public List<Edge<T>> getEdges() {
        // Returnerar listan av kanter
        return edges;
    }

    @Override
    public List<T> getNodes() {
        List<T> nodes = new ArrayList<>();

        // Startnoden läggs först i vägen
        nodes.add(start);

        // Varje kant leder till nästa nod
        for (Edge<T> edge : edges) nodes.add(edge.getDestination());
        return nodes;
    }

    @Override
    public Iterator<Edge<T>> iterator() {
        // Möjliggör iteration över vägens kanter med foreach-loop
        return edges.iterator();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        // Skapar en läsbar representation av hela vägen
        sb.append("Path from ").append(start).append(" to ").append(getEnd()).append(":\n");
        T current = start;
        for (Edge<T> edge : edges) {
            sb.append("  ").append(current)
              .append(" --[").append(edge.getName())
              .append(", time=").append(edge.getTime())
              .append("]--> ").append(edge.getDestination()).append("\n");
            current = edge.getDestination();
        }
        sb.append("Total time: ").append(getTotalTime());
        return sb.toString();
    }
}
