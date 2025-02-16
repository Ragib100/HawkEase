package me.hawkease;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;

public class NameStatus {
    private final StringProperty name;
    private final RadioButton acceptedRadio;
    private final RadioButton rejectedRadio;
    private final ToggleGroup toggleGroup;

    public NameStatus(String name) {
        this.name = new SimpleStringProperty(name);
        toggleGroup = new ToggleGroup();

        acceptedRadio = new RadioButton("Accepted");
        acceptedRadio.setToggleGroup(toggleGroup);

        rejectedRadio = new RadioButton("Rejected");
        rejectedRadio.setToggleGroup(toggleGroup);
    }

    public StringProperty getName() {
        return name;
    }

    public RadioButton getAcceptedRadio() {
        return acceptedRadio;
    }

    public RadioButton getRejectedRadio() {
        return rejectedRadio;
    }
}