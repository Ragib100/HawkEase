package me.hawkease;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
//hi
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class Log_in_controller implements Initializable {

    @FXML
    private PasswordField take_password;

    @FXML
    private TextField take_email;

    @FXML
    private ComboBox<String> user_type;

    @FXML
    void forgot_password(MouseEvent event) {
        try{
            fxml_loader fxmlLoader = new fxml_loader(event);
            fxmlLoader.load_fxml("forgot_password_controller.fxml");
        }
        catch(IOException e){
            System.out.println("Try again");
        }
    }

    @FXML
    void log_in(MouseEvent event) throws SQLException {
        String email = take_email.getText();
        String password = take_password.getText();
        String type = user_type.getValue();
        users_sql st = new users_sql();
        boolean flag = st.check_user(email,password,type);
        if(flag) {
            if(type.equals("Admin")) {
                try {
                    fxml_loader fxmlLoader = new fxml_loader(event);
                    fxmlLoader.load_fxml("adminhomepageController.fxml");
                }
                catch (IOException e){
                    System.out.println("Error in loading fxml");
                }
            }
        }
    }

    @FXML
    void not_a_user(MouseEvent event) {
        try {
            fxml_loader f = new fxml_loader(event);
            f.load_fxml("sign_up.fxml");
        }
        catch (IOException e){
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        user_type.setItems(FXCollections.observableArrayList("Admin", "Buyer", "Seller"));
        user_type.setStyle("-fx-font-family: 'Times New Roman'; -fx-font-size: 20px;");
    }
}
