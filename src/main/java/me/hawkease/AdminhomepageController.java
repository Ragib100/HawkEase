package me.hawkease;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;

public class AdminhomepageController {

    @FXML
    private StackPane stack_pane;

    @FXML
    void allocate_stall_and_rent(MouseEvent event) {
        try{
            stack_pane.getChildren().clear();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("allocatestallrent.fxml"));
            stack_pane.getChildren().add(fxmlLoader.load());
        }
        catch(Exception e){
            System.out.println("Error loading fxml");
        }
    }

    @FXML
    void pending_req(MouseEvent event) {
        try{
            stack_pane.getChildren().clear();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("pendingreq.fxml"));
            stack_pane.getChildren().add(fxmlLoader.load());
        }
        catch(Exception e){
            System.out.println("Error loading fxml");
        }
    }

    @FXML
    void rent_prices(MouseEvent event) {
        try{
//            fxml_loader fxml = new fxml_loader(event);
//            fxml.load_fxml("allocatestallrent.fxml");
                System.out.println("Rent Prices");
    
        }
        catch(Exception e){
            System.out.println("Error loading fxml");
        }
    }

    @FXML
    void reports_analytics(MouseEvent event) {
        try{
//            fxml_loader fxml = new fxml_loader(event);
//            fxml.load_fxml("allocatestallrent.fxml");
            System.out.println("Report Analytics");
        }
        catch(Exception e){
            System.out.println("Error loading fxml");
        }
    }

}
