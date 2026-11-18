// PROG2 VT2026
// Grupp 043
// Noah Ander 
// Filippa Colbin
// Natalija Karlsson

package se.su.inlupp;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

/**
 * Huvudklass för Walk Planner-applikationen.
 * Ansvarar för att koppla ihop GUI (MapPane) med modell-logik (GraphManager)
 * och hanterar alla användarens interaktioner (menyer, knappar och klick).
 */
public class WalkPlannerMain extends Application {

    // Hanterar hela grafen (platser, kanter och vägberäkning)
    // används som “backend” för applikationen
    private final GraphManager manager = new GraphManager();

    // Visuell karta där användaren placerar och väljer platser
    private MapPane mapPane;

    // Behövs för fil-dialogerna men syns inte i UML
    private Stage primaryStage;

    // -----------------------------------------------------------------------
    // UML method: start()
    // -----------------------------------------------------------------------

    @Override
    public void start(Stage stage) {
        this.primaryStage = stage;

        BorderPane root = new BorderPane();
        mapPane = new MapPane(manager);
        root.setCenter(mapPane);

        MenuBar menuBar = createMenuBar();
        HBox toolbar = createToolbar();

        // VBox används för att placera meny och toolbar ovanför kartan
        root.setTop(new VBoxWithSpacing(menuBar, toolbar));

        // Klick på tom bakgrund skapar ny plats
        mapPane.setOnMouseClicked(event -> {
            // if-sats som ignorerar klick på befintliga objekt
            if (event.getTarget() == mapPane) {
                handleAddPlaceAt(event.getX(), event.getY());
            }
        });

        Scene scene = new Scene(root, 950, 720);
        stage.setTitle("Promenadplanerare");
        stage.setScene(scene);

        // Stoppar programmet att stänga av vid osparade ändringar
        stage.setOnCloseRequest(e -> {
            if (!confirmIfUnsaved()) e.consume();
        });

        stage.show();
    }

    // -----------------------------------------------------------------------
    // UML method: createMenuBar()
    // -----------------------------------------------------------------------

    private MenuBar createMenuBar() {
        // Meny för fil-operationer (ny, öppna, spara, osv.)
        Menu fileMenu = new Menu("Fil");

        MenuItem newItem = new MenuItem("Ny");
        newItem.setOnAction(e -> handleNew());

        MenuItem openItem = new MenuItem("Öppna...");
        openItem.setOnAction(e -> handleOpen());

        MenuItem saveItem = new MenuItem("Spara...");
        saveItem.setOnAction(e -> handleSave());

        // Bakgrundsbild påverkar hur kartan visas
        MenuItem loadImageItem = new MenuItem("Ladda bakgrundsbild...");
        loadImageItem.setOnAction(e -> handleLoadImage());

        MenuItem exitItem = new MenuItem("Avsluta");
        exitItem.setOnAction(e -> {
            // if-sats för kontroll av osparat arbete
            if (confirmIfUnsaved()) Platform.exit();
        });

        fileMenu.getItems().addAll(newItem, openItem, saveItem,
                new SeparatorMenuItem(), loadImageItem,
                new SeparatorMenuItem(), exitItem);

        // Meny för att välja algoritm för vägberäkning
        Menu algorithmMenu = new Menu("Algoritm");
        ToggleGroup group = new ToggleGroup();

        RadioMenuItem bfsItem = new RadioMenuItem("BFS – kortast antal delsträckor");
        bfsItem.setToggleGroup(group);
        bfsItem.setSelected(true);

        // BFS används för att hitta kortaste väg
        bfsItem.setOnAction(e -> manager.setPathFinder(new BFSPathFinder<>()));

        RadioMenuItem dfsItem = new RadioMenuItem("DFS – möjlig väg");
        dfsItem.setToggleGroup(group);

        // DFS används för att hitta möjlig väg
        dfsItem.setOnAction(e -> manager.setPathFinder(new DFSPathFinder<>()));

        algorithmMenu.getItems().addAll(bfsItem, dfsItem);

        return new MenuBar(fileMenu, algorithmMenu);
    }

    // -----------------------------------------------------------------------
    // UML method: createToolbar()
    // -----------------------------------------------------------------------

    private HBox createToolbar() {
        Button addButton = new Button("Lägg till plats");
        addButton.setOnAction(e -> handleAddPlace());

        Button removeButton = new Button("Ta bort plats");
        removeButton.setOnAction(e -> handleRemovePlace());

        Button connectButton = new Button("Koppla platser");
        connectButton.setOnAction(e -> handleConnect());

        Button findPathButton = new Button("Hitta väg");
        findPathButton.setOnAction(e -> handleFindPath());

        Button clearButton = new Button("Rensa val");
        clearButton.setOnAction(e -> mapPane.clearSelection());

        // Grupperar viktiga åtgärder för snabb åtkomst
        HBox box = new HBox(10, addButton, removeButton, connectButton,
                findPathButton, clearButton);
        box.setPadding(new Insets(8));
        return box;
    }

    // -----------------------------------------------------------------------
    // UML method: handleUserActions() — split into individual handlers
    // -----------------------------------------------------------------------
    private void handleAddPlace() {
        handleAddPlaceAt(mapPane.getPrefWidth() / 2, mapPane.getPrefHeight() / 2);
    }

    private void handleAddPlaceAt(double x, double y) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Ny plats");
        dialog.setHeaderText("Lägg till plats");
        dialog.setContentText("Namn på platsen:");

        Optional<String> result = dialog.showAndWait();
        if (result.isEmpty()) return;

        String name = result.get().trim();

        // Validering förhindrar tomma namn (skapar trasiga noder)
        if (name.isBlank()) {
            showError("Ogiltigt namn", "Platsnamnet får inte vara tomt.");
            return;
        }

        // Förhindrar dubletter (skapar konflikt i grafen)
        if (manager.findPlaceByName(name) != null) {
            showError("Duplicerat namn", "En plats med det namnet finns redan.");
            return;
        }

        manager.addPlace(name, x, y);
        mapPane.drawMap();
    }

    private void handleRemovePlace() {
        Place start = mapPane.getSelectedStart();
        Place goal  = mapPane.getSelectedGoal();

        // Acceptera vilken enskild plats som väljs
        Place toRemove = null;
        if (start != null && goal == null) toRemove = start;
        else if (goal != null && start == null) toRemove = goal;

        // Exakt plats måste vara vald
        if (toRemove == null) {
            showError("Välj en plats", "Markera exakt en plats för att ta bort den.");
            return;
        }

        // Bekräftelse pga bortttagning påverkar plats och kopplingar
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                "Ta bort '" + toRemove.getName() + "' och alla dess kopplingar?",
                ButtonType.YES, ButtonType.NO);
        confirm.setTitle("Bekräfta borttagning");
        if (confirm.showAndWait().orElse(ButtonType.NO) == ButtonType.YES) {
            manager.removePlace(toRemove);
            mapPane.clearSelection();
            mapPane.drawMap();
        }
    }

    private void handleConnect() {
        Place a = mapPane.getSelectedStart();
        Place b = mapPane.getSelectedGoal();

        // Kräver två valda platser (kant binder alltid två noder)
        if (a == null || b == null) {
            showError("Välj två platser", "Markera en start- och en målplats för att koppla ihop dem.");
            return;
        }

        // Förhindrar dubbla kanter
        if (manager.getEdgeBetween(a, b) != null) {
            showError("Redan kopplad",
                    "Det finns redan en koppling mellan '" + a + "' och '" + b + "'.");
            return;
        }

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Koppla platser");
        dialog.setHeaderText("Koppla " + a + " ↔ " + b);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField nameField   = new TextField();
        nameField.setPromptText("Namn på sträckan");
        TextField timeField = new TextField();
        timeField.setPromptText("Tid (heltal ≥ 0)");

        grid.add(new Label("Namn:"),  0, 0);
        grid.add(nameField,           1, 0);
        grid.add(new Label("Tid:"),  0, 1);
        grid.add(timeField,         1, 1);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isEmpty() || result.get() != ButtonType.OK) return;

        String edgeName = nameField.getText().trim();
        if (edgeName.isBlank()) {
            showError("Ogiltigt namn", "Sträckans namn får inte vara tomt.");
            return;
        }


        // Tid valideras eftersom det används i kortaste väg beräkningen
        int time;
        try {
            time = Integer.parseInt(timeField.getText().trim());
            if (time < 0) throw new NumberFormatException();
        } catch (NumberFormatException ex) {
            showError("Ogiltig tid", "Tid måste vara ett heltal som är 0 eller större.");
            return;
        }

        manager.connectPlaces(a, b, edgeName, time);
        mapPane.clearSelection();
        mapPane.drawMap();
    }

    private void handleFindPath() {
        Place from = mapPane.getSelectedStart();
        Place to   = mapPane.getSelectedGoal();

        if (from == null || to == null) {
            showError("Välj två platser", "Markera en start- och en målplats för att söka väg.");
            return;
        }

        Path<Place> path = manager.findRoute(from, to);

        // Visar om graften inte har giltig sart och mål
        if (path == null) {
            showError("Ingen väg hittad",
                    "Det finns ingen väg mellan '" + from + "' och '" + to + "'.");
            return;
        }

        // Används för att visuellt visa vald väg på kartan
        mapPane.highlightPath(path);

        // Skapar steg för steg beskrivning för användaren (inte bara data)
        StringBuilder sb = new StringBuilder();
        sb.append("Väg från ").append(from).append(" till ").append(to).append(":\n\n");
        Place current = from;
        for (Edge<Place> edge : path) {
            sb.append("  ").append(current)
              .append("  →  [").append(edge.getName())
              .append(", tid ").append(edge.getTime())
              .append("]  →  ").append(edge.getDestination()).append("\n");
            current = edge.getDestination();
        }
        sb.append("\nTotal tid: ").append(path.getTotalTime());

        Alert info = new Alert(Alert.AlertType.INFORMATION);
        info.setTitle("Hittad väg");
        info.setHeaderText("Väg mellan " + from + " och " + to);
        info.setContentText(sb.toString());
        info.showAndWait();
    }

    // -----------------------------------------------------------------------
    // File operations
    // -----------------------------------------------------------------------
    private void handleNew() {

        // Varnar för att undvika att osparade ändringar försvinner
        if (!confirmIfUnsaved()) return;

        // Återställer hela grafen till tomt tillstånd
        manager.clear();
        mapPane.clearSelection();
        mapPane.drawMap();
    }

    private void handleOpen() {
        // Säkerställer att användaren råkar förlora osparade ändringar
        if (!confirmIfUnsaved()) return;

        FileChooser chooser = new FileChooser();
        chooser.setTitle("Öppna graf");

        // Begränsar filval till programmets sparformat
        chooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Textfil", "*.txt"));
        File file = chooser.showOpenDialog(primaryStage);
        if (file == null) return;

        try {
            // Läser in sparad graf från fil
            String imagePath = manager.loadFromFile(file);
            mapPane.clearSelection();

            // Laddar bakgrundsbild om det finns sparad tillsammans med grafen
            if (imagePath != null) {
                File imgFile = new File(imagePath);
                if (imgFile.exists()) mapPane.setBackgroundImage(imgFile);
            }

            // Ritar kartan så den visar inläst data
            mapPane.drawMap();
        } catch (IOException ex) {
            // Visar felmeddelanden om filen inte kunde läsas
            showError("Fel vid inläsning", "Kunde inte läsa filen: " + ex.getMessage());
        }
    }

    private void handleSave() {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Spara graf");

        // Kontrollerar att grafen sparas som textfil
        chooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Textfil", "*.txt"));
        File file = chooser.showSaveDialog(primaryStage);
        if (file == null) return;

        try {
            // Sparar aktuell fils graf + kopplingar
            manager.saveToFile(file);
        } catch (IOException ex) {
            // Hanterar fel vid sparande till fil
            showError("Fel vid sparning", "Kunde inte spara filen: " + ex.getMessage());
        }
    }

    private void handleLoadImage() {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Välj bakgrundsbild");

        // Tillåter endast vanliga bildformat
        chooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Bildfiler", "*.png", "*.jpg", "*.jpeg"));
        File file = chooser.showOpenDialog(primaryStage);
        if (file == null) return;

        // Sätter vald bild som bakgrund
        mapPane.setBackgroundImage(file);
        // Sparar så bilden kan återställas senare
        manager.setLoadedImagePath(file.getAbsolutePath());
    }

    // -----------------------------------------------------------------------
    // Helpers
    // -----------------------------------------------------------------------
    private boolean confirmIfUnsaved() {
        // Varning visas ej om inga ändringar har gjorts
        if (!manager.hasUnsavedChanges()) return true;

        Alert alert = new Alert(Alert.AlertType.WARNING,
                "Du har osparade ändringar. Vill du fortsätta utan att spara?",
                ButtonType.YES, ButtonType.NO);
        alert.setTitle("Osparade ändringar");

        // Returnerar true om användaren fortsätter 
        return alert.showAndWait().orElse(ButtonType.NO) == ButtonType.YES;
    }

    private void showError(String title, String message) {
        // Felhantering för alal fel visas på ett konsekvent sätt
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        // Startar JavaFX
        launch(args);
    }
}
