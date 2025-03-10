package me.hawkease;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;

public class seller_homepage_controller {

    @FXML
    private StackPane page;

    @FXML
    void log_out(MouseEvent event) {
        try{
            fxml_loader f = new fxml_loader(event);
            f.load_fxml("log_in.fxml");
        }
        catch (Exception e){
            System.out.println("Error in loading fxml");
        }
    }

    @FXML
    void open_profile(MouseEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("profile.fxml"));
            Node newContent = fxmlLoader.load();
            page.getChildren().clear();
            page.getChildren().add(newContent);
        }
        catch (IOException e) {
            System.out.println("Error in loading fxml: " + e.getMessage());
        }
    }

    @FXML
    void pay_bill(MouseEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("paybill.fxml"));
            Node newContent = fxmlLoader.load();
            page.getChildren().clear();
            page.getChildren().add(newContent);
        }
        catch (IOException e) {
            System.out.println("Error in loading fxml: " + e.getMessage());
        }
    }

    @FXML
    void request_for_a_place(MouseEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("requestforaplace.fxml"));
            Node newContent = fxmlLoader.load();
            page.getChildren().clear();
            page.getChildren().add(newContent);
        }
        catch (IOException e) {
            System.out.println("Error in loading fxml: " + e.getMessage());
        }
    }

}
