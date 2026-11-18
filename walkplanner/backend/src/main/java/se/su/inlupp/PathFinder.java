// PROG2 VT2026
// Grupp 043
// Noah Ander 
// Filippa Colbin
// Natalija Karlsson
package se.su.inlupp;

/**
 * Interface för algorithmer som söker väd i en graf.
 * Används enligt Strategy Pattern för att kunna byta
 * mellan olika sökalgoritmer 
 * 
 * @param <T> typen av noder i grafen (sökning, start, mål)
 */
public interface PathFinder<T> {
    Path<T> findPath(Graph<T> graph, T from, T to);
}
