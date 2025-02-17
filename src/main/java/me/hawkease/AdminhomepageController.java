package me.hawkease;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;

import java.io.IOException;

public class AdminhomepageController {

    @FXML
    private StackPane page;

    @FXML
    void allocate_stall_and_rent(MouseEvent event){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("allocatestallrent.fxml"));
            page.getChildren().clear();
            page.getChildren().add(fxmlLoader.load());
        }
        catch (IOException e){
            System.out.println("Error in loading fxml");
        }
    }

    @FXML
    void pending_req(MouseEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("pendingreq.fxml"));
            page.getChildren().clear();
            page.getChildren().add(fxmlLoader.load());
        }
        catch (IOException e){
            System.out.println("Error in loading fxml");
        }
    }

    @FXML
    void rent_prices(MouseEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("rent_prices.fxml"));
            page.getChildren().clear();
            page.getChildren().add(fxmlLoader.load());
        }
        catch (IOException e){
            System.out.println("Error in loading fxml");
        }
    }

    @FXML
    void reports_analytics(MouseEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("reports_analytics.fxml"));
            page.getChildren().clear();
            page.getChildren().add(fxmlLoader.load());
        }
        catch (IOException e){
            System.out.println("Error in loading fxml");
        }
    }

}
