package me.hawkease;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class main_class extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(main_class.class.getResource("adminhomepageController.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("HawkEase!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        //Md. Ragib Hossain Rifat
        launch();
    }
}
//import javafx.application.Application;
//import javafx.scene.Node;
//import javafx.scene.Scene;
//import javafx.scene.control.Button;
//import javafx.scene.control.TextField;
//import javafx.scene.layout.VBox;
//import javafx.scene.web.WebEngine;
//import javafx.scene.web.WebView;
//import javafx.stage.Stage;
//
//public class main_class extends Application {
//    @Override
//    public void start(Stage primaryStage) {
//        TextField latitudeField = new TextField();
//        latitudeField.setPromptText("Enter Latitude");
//
//        TextField longitudeField = new TextField();
//        longitudeField.setPromptText("Enter Longitude");
//
//        Button showMapButton = new Button("Show Map");
//        WebView webView = new WebView();
//        WebEngine webEngine = webView.getEngine();
//
//        showMapButton.setOnAction(e -> {
//            String lat = latitudeField.getText();
//            String lon = longitudeField.getText();
//            if (!lat.isEmpty() && !lon.isEmpty()) {
//                String mapUrl = "https://www.openstreetmap.org/?mlat=" + lat + "&mlon=" + lon + "&zoom=15";
//                webEngine.load(mapUrl);
//            }
//        });
//
//        VBox root = new VBox(10.0, (Node) latitudeField, (Node) longitudeField, (Node) showMapButton, (Node) webView);
//        Scene scene = new Scene(root, 800, 600);
//        primaryStage.setTitle("Map Viewer");
//        primaryStage.setScene(scene);
//        primaryStage.show();
//    }
//
//    public static void main(String[] args) {
//        launch(args);
//    }
//}
