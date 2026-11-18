// PROG2 VT2026
// Grupp 043
// Noah Ander 
// Filippa Colbin
// Natalija Karlsson
package se.su.inlupp;

import java.util.*;

/**
 * Implementering av en viktad, oriktad graf med hjälp av adjacency listsl
 * Varje koppling lagras som två riktade kanter för att möjliggöra 
 * navigering åt båda hållen mellan noder
 *
 * @param <T> typen av noder som lagras i grafen
 */
public class ListGraph<T> implements Graph<T> {
    // Kopplar varje nod till en lista av dess anslutna kanter
    private final Map<T, List<Edge<T>>> adjacencyList = new LinkedHashMap<>();

    // -----------------------------------------------------------------------
    // Inner edge class
    // -----------------------------------------------------------------------
    private static class GraphEdge<T> implements Edge<T> {
        // Noden som kanten leder till
        private final T destination;
        // Namn på kopplingen/vägen
        private final String name;
        // Restid för kanten
        private int time;

        GraphEdge(T destination, String name, int time) {
            this.destination = destination;
            this.name = name;
            this.time = time;
        }

        @Override
        public T getDestination() { return destination; }

        @Override
        public int getTime() { return time; }

        @Override
        public void setTime(int time) {
            // Negativa tider är ej tillåtna
            if (time < 0) throw new IllegalArgumentException("Time cannot be negative");
            this.time = time;
        }

        @Override
        public String getName() { return name; }

        @Override
        public String toString() {
            // Kanten blir lättare att läsa vid debuggning och utskrift
            return "till " + destination + " med " + name + " tar " + time;
        }
    }

    // -----------------------------------------------------------------------
    // Graph methods
    // -----------------------------------------------------------------------

    @Override
    public void add(T node) {
        // Förhindrar att samma nod läggs till flera gånger
        adjacencyList.putIfAbsent(node, new ArrayList<>());
    }

    @Override
    public void remove(T node) {
        if (!adjacencyList.containsKey(node)) {
            throw new NoSuchElementException("Node not found: " + node);
        }
        // Tar bort alla kanter som pekar mot noden för konsekvens i grafen
        for (T other : adjacencyList.keySet()) {
            adjacencyList.get(other).removeIf(e -> e.getDestination().equals(node));
        }
        adjacencyList.remove(node);
    }

    @Override
    public boolean hasNode(T node) {
        // Kontroll som kräver att noden finns
        return adjacencyList.containsKey(node);
    }

    @Override
    public void connect(T node1, T node2, String name, int time) {
        if (!adjacencyList.containsKey(node1) || !adjacencyList.containsKey(node2)) {
            throw new NoSuchElementException("One or both nodes not found");
        }

        // Ej tillåtet med negativa tider pga felaktiga beräkningar
        if (time < 0) {
            throw new IllegalArgumentException("Time cannot be negative");
        }

        // Ej tillåtet med dubbletter av samma noder
        if (getEdgeBetween(node1, node2) != null) {
            throw new IllegalStateException("An edge already exists between these nodes");
        }

        // Grafen är oriktad så kanten lagras i båda riktningar
        adjacencyList.get(node1).add(new GraphEdge<>(node2, name, time));
        adjacencyList.get(node2).add(new GraphEdge<>(node1, name, time));
    }

    @Override
    public void disconnect(T node1, T node2) {
        if (!adjacencyList.containsKey(node1) || !adjacencyList.containsKey(node2)) {
            throw new NoSuchElementException("One or both nodes not found");
        }
        if (getEdgeBetween(node1, node2) == null) {
            throw new IllegalStateException("No edge exists between these nodes");
        }
        adjacencyList.get(node1).removeIf(e -> e.getDestination().equals(node2));
        adjacencyList.get(node2).removeIf(e -> e.getDestination().equals(node1));
    }

    @Override
    public void setConnectionTime(T node1, T node2, int time) {
        if (!adjacencyList.containsKey(node1) || !adjacencyList.containsKey(node2)) {
            throw new NoSuchElementException("One or both nodes not found");
        }

        // Tiden måste vara giltig för korrekt data för grafen
        if (time < 0) {
            throw new IllegalArgumentException("Time cannot be negative");
        }
        Edge<T> e1 = getEdgeBetween(node1, node2);
        Edge<T> e2 = getEdgeBetween(node2, node1);
        if (e1 == null || e2 == null) {
            throw new NoSuchElementException("No edge exists between these nodes");
        }

        // Uppdaterar båda riktningarna i grafen
        e1.setTime(time);
        e2.setTime(time);
    }

    @Override
    public Set<T> getNodes() {
        // Returnerar kopia för att skydda datastruktur
        return new LinkedHashSet<>(adjacencyList.keySet());
    }

    @Override
    public Collection<Edge<T>> getEdgesFrom(T node) {
        if (!adjacencyList.containsKey(node)) {
            throw new NoSuchElementException("Node not found: " + node);
        }
        // Returnerar kopia så externa klasser inte kan ändra orginalet direkt
        return new ArrayList<>(adjacencyList.get(node));
    }

    @Override
    public Edge<T> getEdgeBetween(T node1, T node2) {
        if (!adjacencyList.containsKey(node1) || !adjacencyList.containsKey(node2)) {
            throw new NoSuchElementException("One or both nodes not found");
        }

        // Söker igenom alla kanter från första noden
        for (Edge<T> edge : adjacencyList.get(node1)) {
            if (edge.getDestination().equals(node2)) return edge;
        }
        return null;
    }

    @Override
    public Iterator<T> iterator() {
        // Grafen blir möjlig att iterera över med foreach-loop
        return adjacencyList.keySet().iterator();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        // Skapar läsbar text av hela grafen
        for (Map.Entry<T, List<Edge<T>>> entry : adjacencyList.entrySet()) {
            sb.append(entry.getKey()).append(":\n");
            for (Edge<T> edge : entry.getValue()) {
                sb.append("  ").append(edge).append("\n");
            }
        }
        return sb.toString();
    }
}
