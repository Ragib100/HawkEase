package me.hawkease;

import javafx.scene.layout.BorderPane;

public interface map_controller {
    void setLocation(double lat, double lon);
    BorderPane getPage();
}