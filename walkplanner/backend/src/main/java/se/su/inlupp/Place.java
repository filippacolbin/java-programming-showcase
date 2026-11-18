// PROG2 VT2026
// Grupp 043
// Noah Ander 
// Filippa Colbin
// Natalija Karlsson
package se.su.inlupp;

/**
 * Representerar en plats på kartan och fungear som en nod i grafen.
 * Klassen innehåller endast data för namn och position
 * Innehåller ej JavaFX-grafik
 */
public class Place {

    // Namn används som unik identifierare i grafen
    private final String name;
    // Position på kartan
    private double x;
    private double y;

    public Place(String name, double x, double y) {

        // Förhindrar att platser skapas utan giltigt namn
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name cannot be empty");
        }
        this.name = name;
        this.x = x;
        this.y = y;
    }

    public String getName() { return name; }
    public double getX() { return x; }
    public double getY() { return y; }

    public void setPosition(double x, double y) {
        // Uppdaterar platsens koordinater på kartan
        this.x = x;
        this.y = y;
    }

    @Override
    // Platsens namn visas istället för objektets minnesadress
    public String toString() { return name; }
}
