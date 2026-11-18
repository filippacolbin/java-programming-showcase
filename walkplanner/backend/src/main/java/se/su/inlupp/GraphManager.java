// PROG2 VT2026
// Grupp 043
// Noah Ander 
// Filippa Colbin
// Natalija Karlsson
package se.su.inlupp;

import java.io.*;
import java.util.Collection;
import java.util.Set;

/**
 * Modellklass som hanterar grafen av platser och kopplingar. Model class that manages the graph of places.
 * Mellanhand mellan grafbiblioteket och GUI.
 * Innehåller ingen JavaFx logic.
 */
public class GraphManager {
    // Själva grafstrukturen som lagrar platser och kopplingar
    private final ListGraph<Place> graph = new ListGraph<>();
    // Algoritm för vägberäkning
    private PathFinder<Place> pathFinder = new BFSPathFinder<>();
    // Håller reda på om data behöver sparas
    private boolean unsavedChanges = false;
    // Sparar sökvägen
    private String loadedImagePath = null;

    // -----------------------------------------------------------------------
    // Algorithm selection (Strategy pattern)
    // -----------------------------------------------------------------------

    public void setPathFinder(PathFinder<Place> pathFinder) {
        // Gör det möjligt att byta sökalgoritm dynamiskt
        this.pathFinder = pathFinder;
    }

    // -----------------------------------------------------------------------
    // Node operations
    // -----------------------------------------------------------------------

    public void addPlace(String name, double x, double y) {
        Place place = new Place(name, x, y);
        graph.add(place);
        // Markerar att grafen ändrats sedan senaste sparning
        unsavedChanges = true;
    }

    public void removePlace(Place place) {
        graph.remove(place);
        // Borttagning påverkar grafens innehåll och måste sparas
        unsavedChanges = true;
    }

    // -----------------------------------------------------------------------
    // Edge operations
    // -----------------------------------------------------------------------

    public void connectPlaces(Place a, Place b, String name, int time) {
        graph.connect(a, b, name, time);
        // Ny koppling förändrar grafens struktur
        unsavedChanges = true;
    }

    public void disconnectPlaces(Place a, Place b) {
        graph.disconnect(a, b);
        unsavedChanges = true;
    }

    // -----------------------------------------------------------------------
    // Path finding
    // -----------------------------------------------------------------------

    public Path<Place> findRoute(Place from, Place to) {
        // Vägberäkningen till vald algoritm
        return pathFinder.findPath(graph, from, to);
    }

    // -----------------------------------------------------------------------
    // Graph access
    // -----------------------------------------------------------------------

    public Set<Place> getPlaces() {
        return graph.getNodes();
    }

    public Collection<Edge<Place>> getEdgesFrom(Place place) {
        return graph.getEdgesFrom(place);
    }

    public Edge<Place> getEdgeBetween(Place a, Place b) {
        return graph.getEdgeBetween(a, b);
    }

    public ListGraph<Place> getGraph() {
        return graph;
    }

    // -----------------------------------------------------------------------
    // Unsaved changes tracking
    // -----------------------------------------------------------------------

    // Varnar användaren innan data förloras
    public boolean hasUnsavedChanges() {
        return unsavedChanges;
    }

    public void clearUnsavedChanges() {
        unsavedChanges = false;
    }

    // -----------------------------------------------------------------------
    // Save / Load
    // -----------------------------------------------------------------------

    /**
     * Sparar grafen till textfil
     * Format:
     *   IMAGE:<path>          (bara om en bild används)
     *   PLACE:<name>:<x>:<y>  (1 per nod)
     *   EDGE:<from>:<to>:<name>:<time>  (en per riktat par, lagras en gång)
     */
    public void saveToFile(File file) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {

            // Sparar bakgrundsbild (om en används)
            if (loadedImagePath != null) {
                writer.println("IMAGE:" + loadedImagePath);
            }

            // Sparar alla platser med namn och position
            for (Place place : graph.getNodes()) {
                writer.println("PLACE:" + place.getName() + ":" + place.getX() + ":" + place.getY());
            }
            // Hindrar att samma kant sparas två gånger
            Set<Place> written = new java.util.HashSet<>();
            for (Place place : graph.getNodes()) {
                for (Edge<Place> edge : graph.getEdgesFrom(place)) {
                    Place dest = edge.getDestination();
                    if (!written.contains(dest)) {
                        writer.println("EDGE:" + place.getName() + ":" + dest.getName()
                                + ":" + edge.getName() + ":" + edge.getTime());
                    }
                }
                written.add(place);
            }
        }

        // Grafen sparas och innehåller inga osparade ändringar
        unsavedChanges = false;
    }

    /**
     * Läser in graf från textfil
     * returnerar sökvägen till bakgrundsbilden (om den finns)
     * annars null
     */
    public String loadFromFile(File file) throws IOException {
        // Tömmer nuvarande graf
        for (Place place : graph.getNodes().toArray(new Place[0])) {
            graph.remove(place);
        }
        loadedImagePath = null;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();

                if (line.startsWith("IMAGE:")) {
                    // Läser in sparad bakgrundsbild
                    loadedImagePath = line.substring(6);
                } else if (line.startsWith("PLACE:")) {
                    String[] parts = line.split(":");
                    String name = parts[1];
                    double x = Double.parseDouble(parts[2]);
                    double y = Double.parseDouble(parts[3]);
                    graph.add(new Place(name, x, y));
                } else if (line.startsWith("EDGE:")) {
                    String[] parts = line.split(":");
                    String fromName = parts[1];
                    String toName = parts[2];
                    String edgeName = parts[3];
                    int time = Integer.parseInt(parts[4]);
                    Place from = findPlaceByName(fromName);
                    Place to = findPlaceByName(toName);

                    // Kopplingen skapas bara om båda noderna finns
                    if (from != null && to != null) {
                        graph.connect(from, to, edgeName, time);
                    }
                }
            }
        }
        unsavedChanges = false;
        return loadedImagePath;
    }

    public void setLoadedImagePath(String path) {
        // Ändring av bild räknas också som osparad ändring
        this.loadedImagePath = path;
        unsavedChanges = true;
    }

    public Place findPlaceByName(String name) {

        // Kollar genom alla platser efter matchande namn
        for (Place place : graph.getNodes()) {
            if (place.getName().equals(name)) return place;
        }
        return null;
    }

    // Tar bort alla platser och kopplingar från grafen
    public void clear() {
        // Alla norder tas bort en i taget (inkl. tillhörande kanter)
        for (Place place : graph.getNodes().toArray(new Place[0])) {
            graph.remove(place);
        }
        loadedImagePath = null;
        unsavedChanges = false;
    }
}
