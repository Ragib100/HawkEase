package me.hawkease;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
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

    }

    @FXML
    void pay_bill(MouseEvent event) {
        try{
            page.getChildren().clear();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("paybill.fxml"));
            page.getChildren().add(fxmlLoader.load());
        }
        catch (Exception e){
            System.out.println("Error in loading fxml");
        }
    }

    @FXML
    void request_for_a_place(MouseEvent event) {
        try{
            page.getChildren().clear();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("requestforaplace.fxml"));
            page.getChildren().add(fxmlLoader.load());
        }
        catch (Exception e){
            System.out.println("Error in loading fxml");
        }
    }

    @FXML
    void add_products(MouseEvent event) {
        try{
            page.getChildren().clear();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("addproducts.fxml"));
            page.getChildren().add(fxmlLoader.load());
        }
        catch (Exception e){
            System.out.println("Error in loading fxml");
        }
    }

}
