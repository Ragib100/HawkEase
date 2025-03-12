package me.hawkease;

import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;

import java.io.IOException;

public class welcome_page {

    @FXML
    void get_started(MouseEvent event) {
        try{
            fxml_loader fxml_loader = new fxml_loader(event);
            fxml_loader.load_fxml("log_in.fxml");
        }
        catch (IOException e) {
            make_alert.getInstance().make_alert("Error","Page loading failed");
        }
    }

}
