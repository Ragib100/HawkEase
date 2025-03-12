package me.hawkease;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import java.io.IOException;

public class fxml_loader {
    private MouseEvent event;

    public fxml_loader(MouseEvent event) {
        this.event = event;
    }

    public void load_fxml(String path) throws IOException {
        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();

        double width = stage.getWidth();
        double height = stage.getHeight();
        double x = stage.getX();
        double y = stage.getY();

        FXMLLoader loader = new FXMLLoader(getClass().getResource(path));
        Parent root = loader.load();

        Scene scene = new Scene(root);

        stage.setScene(scene);

        stage.setWidth(width);
        stage.setHeight(height);
        stage.setX(x);
        stage.setY(y);

        stage.show();
    }

}