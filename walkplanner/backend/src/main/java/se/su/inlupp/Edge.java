// PROG2 VT2026
// Grupp 043
// Noah Ander 
// Filippa Colbin
// Natalija Karlsson
package se.su.inlupp;
/**
 * Interface som representerar en kant mellan
 * två noder i en graf.
 *
 * @param <T> typen av nod som kanten pekar mot
 */
public interface Edge<T> {
    T getDestination();
    int getTime();
    void setTime(int time);
    String getName();

    // Aliases required by test suite
    default int getWeight() { return getTime(); }
    default void setWeight(int weight) { setTime(weight); }
}
