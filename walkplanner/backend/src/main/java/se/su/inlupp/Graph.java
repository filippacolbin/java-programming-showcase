// PROG2 VT2026
// Grupp 043
// Noah Ander 
// Filippa Colbin
// Natalija Karlsson
package se.su.inlupp;

import java.util.Collection;
import java.util.Set;

/**
 * Interface för en viktad och oriktad graf.
 */
public interface Graph<T> extends Iterable<T> {
    void add(T node);
    void remove(T node);
    boolean hasNode(T node);
    void connect(T node1, T node2, String name, int time);
    void disconnect(T node1, T node2);
    void setConnectionTime(T node1, T node2, int time);
    Set<T> getNodes();
    Collection<Edge<T>> getEdgesFrom(T node);
    Edge<T> getEdgeBetween(T node1, T node2);

    // Alias required by test suite
    default void setConnectionWeight(T node1, T node2, int weight) {
        setConnectionTime(node1, node2, weight);
    }
}
