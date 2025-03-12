package me.hawkease;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class main_class extends Application {
    @Override
    public void start(Stage stage) {
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(main_class.class.getResource("welcome_page.fxml"));
            // current_user.get_user().set_email("mdragibhossain09@gmail.com");
            Scene scene = new Scene(fxmlLoader.load());
            stage.setTitle("HawkEase!");
            stage.setScene(scene);
            stage.show();
        }
        catch (IOException e) {
            make_alert.getInstance().make_alert("Error","Page loading failed");
        }
    }

    public static void main(String[] args) {
        launch();
    }
}