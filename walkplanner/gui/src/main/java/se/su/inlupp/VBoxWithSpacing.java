// PROG2 VT2026
// Grupp 043
// Noah Ander 
// Filippa Colbin
// Natalija Karlsson

package se.su.inlupp;

import javafx.scene.Node;
import javafx.scene.layout.VBox;

/**
 * VBox som automatiskt använder ett fast avstånd
 * mellan skickade komponenter för jämn layout i gränssnittet
 */
public class VBoxWithSpacing extends VBox {
    public VBoxWithSpacing(Node... children) {
        // Konsekvent mellanrum mellan komponenter
        super(5);
        // Lägger till alla komponenter i layouten
        getChildren().addAll(children);
    }
}
