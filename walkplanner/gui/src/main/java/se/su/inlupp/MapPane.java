// PROG2 VT2026
// Grupp 043
// Noah Ander 
// Filippa Colbin
// Natalija Karlsson

package se.su.inlupp;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.application.Platform;

import java.io.File;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * Klassen ansvarar för att rita upp kartan, platser, kopplingar, vägar.
 * Kommunicerar med GraphManager och fungerar som programmets vy.
 * Fält och metoder matchar UML diagrammet.
 */
public class MapPane extends Pane {

    // Hanterar grafdata som ska visas på kartan
    private final GraphManager manager;

    // Platserna användaren har vält som start/mål
    private Place selectedStart;
    private Place selectedGoal;

    // Används internt för rendering av bakgrundsbild och markerad väg (ej i UML)
    private ImageView backgroundImage;
    private Path<Place> highlightedPath;

    // Callback används för att meddela WalkPlannerMain val ändras 
    private Runnable onSelectionChanged;

    public MapPane(GraphManager manager) {
        this.manager = manager;

        // Standardstorlek för kartområdet
        setPrefSize(800, 600);
        // Netural bakgrundsfärg
        setStyle("-fx-background-color: #f0f0f0;");
    }

    public void setOnSelectionChanged(Runnable callback) {
        this.onSelectionChanged = callback;
    }

    // -----------------------------------------------------------------------
    // Selection access (used by WalkPlannerMain)
    // -----------------------------------------------------------------------
    public Place getSelectedStart() { return selectedStart; }
    public Place getSelectedGoal()  { return selectedGoal; }

    public void clearSelection() {
        // Återställer val och markerad väg
        selectedStart = null;
        selectedGoal = null;
        highlightedPath = null;
        drawMap();
    }

    // -----------------------------------------------------------------------
    // UML methods
    // -----------------------------------------------------------------------

    // Ritar om hela kartan från början
    public void drawMap() {

        // Behåller bakgrundsbilden när kartan ritas om
        ImageView bg = backgroundImage;
        getChildren().clear();
        if (bg != null) {
            getChildren().add(bg);
            backgroundImage = bg;
        }

        // Kopplingar ritas före noderna så de hamnar ovan
        drawConnections();
        drawPlaces();
    }

    // Ritar alla platser på kartan
    public void drawPlaces() {
        for (Place place : manager.getPlaces()) {
            drawPlace(place);
        }
    }

    // Ritar alla kopplingar mellan platser
    public void drawConnections() {

        // Hindrar att samma kant ritas två gånger
        Set<String> drawn = new HashSet<>();
        Set<Place> places = manager.getPlaces();

        // Samlar alla kanter som tillhör markerade väge
        Set<String> highlightedKeys = new HashSet<>();
        if (highlightedPath != null) {
            List<Place> nodes = highlightedPath.getNodes();
            for (int i = 0; i + 1 < nodes.size(); i++) {
                highlightedKeys.add(edgeKey(nodes.get(i), nodes.get(i + 1)));
            }
        }

        for (Place place : places) {
            Collection<Edge<Place>> edges = manager.getEdgesFrom(place);
            for (Edge<Place> edge : edges) {
                Place dest = edge.getDestination();
                String key = edgeKey(place, dest);
                if (!drawn.contains(key)) {
                    drawn.add(key);

                    // Markerade vägar ritas med annan för för tydlighetens skull 
                    drawEdge(place, dest, edge, highlightedKeys.contains(key));
                }
            }
        }
    }

    // Markerar visuellt en hittad väg på kartan
    public void highlightPath(Path<Place> path) {
        this.highlightedPath = path;

        // Kartan måste ritar för att markeringen ska synas
        drawMap();
    }

    // Möjligör att flytta platsen med musen
    public void enableDragging(Circle circle, Place place) {
        double[] dragOffset = new double[2];
        circle.setOnMousePressed(e -> {
            dragOffset[0] = e.getSceneX() - place.getX();
            dragOffset[1] = e.getSceneY() - place.getY();
        });

        circle.setOnMouseDragged(e -> {
            double newX = e.getSceneX() - dragOffset[0];
            double newY = e.getSceneY() - dragOffset[1];
            place.setPosition(newX, newY);

            // Move this node directly — don't tear down/rebuild the scene mid-drag
            circle.setCenterX(newX);
            circle.setCenterY(newY);
        });

        circle.setOnMouseReleased(e -> Platform.runLater(this::drawMap)); // full redraw (edges, labels) once, after release
    }

    // -----------------------------------------------------------------------
    // Background image
    // -----------------------------------------------------------------------
    public void setBackgroundImage(File imageFile) {
        Image img = new Image(imageFile.toURI().toString());
        backgroundImage = new ImageView(img);
        backgroundImage.setPreserveRatio(true);
        backgroundImage.setFitWidth(getPrefWidth());
        backgroundImage.setFitHeight(getPrefHeight());
        backgroundImage.setMouseTransparent(true);
        drawMap();
    }

    // -----------------------------------------------------------------------
    // Private drawing helpers
    // -----------------------------------------------------------------------
    private void drawEdge(Place from, Place to, Edge<Place> edge, boolean highlighted) {
        Line line = new Line(from.getX(), from.getY(), to.getX(), to.getY());

        // Markerad väg visas med anna färg och tjocklek
        line.setStroke(highlighted ? Color.ORANGERED : Color.GRAY);
        line.setStrokeWidth(highlighted ? 4 : 2);

        line.setMouseTransparent(true);

        double mx = (from.getX() + to.getX()) / 2;
        double my = (from.getY() + to.getY()) / 2;
        Text label = new Text(mx + 4, my - 4, edge.getName() + " (" + edge.getTime() + ")");
        label.setFont(Font.font(11));
        label.setFill(highlighted ? Color.ORANGERED : Color.DIMGRAY);
        label.setMouseTransparent(true);

        getChildren().addAll(line, label);
    }

    private void drawPlace(Place place) {
        boolean isStart = place.equals(selectedStart);
        boolean isGoal  = place.equals(selectedGoal);

        boolean isSelected = isStart || isGoal;

        Circle circle = new Circle(place.getX(), place.getY(), 12);

        // Olika färger används för att tydligt visa start/mål
        circle.setFill(isStart ? Color.GOLD : isGoal ? Color.LIMEGREEN : Color.DODGERBLUE);
        circle.setStroke(isSelected ? Color.DARKORANGE : Color.DARKBLUE);
        circle.setStrokeWidth(2);

        Text label = new Text(place.getX() + 15, place.getY() + 5, place.getName());

        label.setFont(Font.font(13));

        // Texten blockerar ej klick på cirkeln
        label.setMouseTransparent(true);

        // Första klick = start (guld), andra klick = mål (grön)
        circle.setOnMouseClicked(e -> {
            e.consume();
            if (place.equals(selectedStart)) {
                selectedStart = null;
            } else if (place.equals(selectedGoal)) {
                selectedGoal = null;
            } else if (selectedStart == null) {
                selectedStart = place;
            } else if (selectedGoal == null) {
                selectedGoal = place;
            }

            // Meddela WalkPlannerMain att användarens val har ändrats
            if (onSelectionChanged != null) onSelectionChanged.run();
            drawMap();
        });

        enableDragging(circle, place);

        getChildren().addAll(circle, label);
    }

    private String edgeKey(Place a, Place b) {

        // Skapar unik nyckel oberoende av riktining
        // så samma kant ej hanteras som två olika
        if (a.getName().compareTo(b.getName()) <= 0) {
            return a.getName() + "||" + b.getName();
        }
        return b.getName() + "||" + a.getName();
    }
}
