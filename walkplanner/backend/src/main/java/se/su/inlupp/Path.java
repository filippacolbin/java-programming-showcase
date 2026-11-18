// PROG2 VT2026
// Grupp 043
// Noah Ander 
// Filippa Colbin
// Natalija Karlsson
package se.su.inlupp;

import java.util.List;

/**
 * Interface som representerar en väg genom en graf.
 */
public interface Path<T> extends Iterable<Edge<T>> {
    T getStart();
    T getEnd();
    int getTotalTime();
    List<Edge<T>> getEdges();
    List<T> getNodes();

    // Alias required by test suite
    default int getTotalWeight() { return getTotalTime(); }
}
