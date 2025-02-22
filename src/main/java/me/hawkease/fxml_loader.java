package me.hawkease;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import java.io.IOException;

public class fxml_loader {
    MouseEvent event;
    public fxml_loader(MouseEvent event) {
        this.event = event;
    }

    public void load_fxml(String path) throws IOException {
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource(path));
            Parent root = loader.load();
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        }
        catch (IOException e){
            System.out.println("Error in loading fxml");
        }
    }
}
