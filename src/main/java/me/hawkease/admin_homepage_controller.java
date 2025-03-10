package me.hawkease;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;

import java.io.IOException;

public class admin_homepage_controller {

    @FXML
    private StackPane page;

    @FXML
    void stalls(MouseEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("allocatestallrent.fxml"));
            Node newContent = fxmlLoader.load();
            page.getChildren().clear();
            page.getChildren().add(newContent);
        }
        catch (IOException e) {
            System.out.println("Error in loading fxml: ");
        }
    }

    @FXML
    void home(MouseEvent event) {
        try {
            fxml_loader fxmlLoader = new fxml_loader(event);
            fxmlLoader.load_fxml("adminhomepageController.fxml");
        }
        catch (IOException e){
            System.out.println("Error in loading fxml: " + e.getMessage());
        }
    }

    @FXML
    void pending_req(MouseEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("pendingreq.fxml"));
            Node newContent = fxmlLoader.load();
            page.getChildren().clear();
            page.getChildren().add(newContent);
        }
        catch (IOException e){
            System.out.println("Error in loading fxml: ");
        }
    }

    @FXML
    void reports_analytics(MouseEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("reports_analytics.fxml"));
            Node newContent = fxmlLoader.load();
            page.getChildren().clear();
            page.getChildren().add(newContent);
        }
        catch (IOException e) {
            System.out.println("Error in loading fxml: ");
        }
    }

    @FXML
    void log_out(MouseEvent event) {
        try{
            fxml_loader fxmlLoader = new fxml_loader(event);
            fxmlLoader.load_fxml("log_in.fxml");
        }
        catch (IOException e){
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
            System.out.println("Error in loading fxml: ");
        }
    }

    @FXML
    void check_bills(MouseEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("bills_from_admin.fxml"));
            Node newContent = fxmlLoader.load();
            page.getChildren().clear();
            page.getChildren().add(newContent);
        }
        catch (IOException e) {
            System.out.println("Error in loading fxml: ");
        }
    }
}
