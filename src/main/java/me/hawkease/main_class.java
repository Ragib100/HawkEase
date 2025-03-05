package me.hawkease;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class main_class extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
//            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/ragib", "root", "asdf1234");
            Connection con = DriverManager.getConnection(System.getenv("cloud_sql_host"), System.getenv("cloud_sql_user"), System.getenv("cloud_sql_password"));
            System.out.println("Connected to database.");
            database_connection.get_connection().set_connection(con);
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println(e.getMessage());
            System.out.println("Database connection failed.");
        }
        FXMLLoader fxmlLoader = new FXMLLoader(main_class.class.getResource("welcome_page.fxml"));
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
