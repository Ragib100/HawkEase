package me.hawkease;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class sign_up_controller implements Initializable {

    @FXML
    private TextFlow password_same_message;

    @FXML
    private TextFlow password_strength_message;

    @FXML
    private ComboBox<String> sign_up_type;

    @FXML
    private TextField take_email_address;

    @FXML
    private PasswordField take_password;

    @FXML
    private PasswordField take_password_again;

    @FXML
    private TextField take_user_name;

    @FXML
    private TextField take_otp;

    String email,password,username,user_type,otp;

    @FXML
    void sign_up(MouseEvent event) {
        if(take_otp.getText()!=null && otp.equals(take_otp.getText())) {
            try{
                users_sql user = new users_sql();
                user.insert_user(username,password,email,user_type);
                fxml_loader fxmlLoader = new fxml_loader(event);
                fxmlLoader.load_fxml("log_in.fxml");
            }
            catch(Exception e){
                System.out.println("Try again");
            }
        }
        else{
            System.out.println("Invalid credentials");
        }
    }

    @FXML
    void send_otp(MouseEvent event) {
        email = take_email_address.getText();
        password = take_password.getText();
        username = take_user_name.getText();
        user_type = sign_up_type.getValue();
        if(valid_password(take_password.getText()) && take_password.getText().equals(take_password_again.getText()) && !email.isEmpty() && !username.isEmpty() && !user_type.isEmpty()) {
            otp = secure_otp_generator.generateOTP();
            send_email send = new send_email();
            send.sendEmail(take_email_address.getText(),otp);
        }
        else {
            System.out.println("Fill up everything correctly");
        }
    }

    @FXML
    void log_in_from_sign_up_page(MouseEvent event) {
        try {
            fxml_loader f = new fxml_loader(event);
            f.load_fxml("log_in.fxml");
        }
        catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        sign_up_type.setItems(FXCollections.observableArrayList("Admin", "Buyer", "Seller"));
        sign_up_type.setStyle("-fx-font-family: 'Times New Roman'; -fx-font-size: 20px;");
        password_strength_message.setStyle("-fx-font-family: 'Times New Roman'; -fx-font-size: 16px");
        password_same_message.setStyle("-fx-font-family: 'Times New Roman'; -fx-font-size: 16px");
        take_password.setOnKeyReleased(this::validate_password);
        take_password.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                password_strength_message.getChildren().clear();
            }
        });
        take_password_again.setOnKeyReleased(this::validate_repeated_password);
        take_password_again.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                password_same_message.getChildren().clear();
            }
        });
    }

    @FXML
    void validate_password(KeyEvent event) {
        validate_password();
    }

    void validate_password(){
        password_strength_message.getChildren().clear();
        String password = take_password.getText();
        add_valid_message("Must be at least 8 characters ",password.length() >= 8);
        add_valid_message("Must contain at least one upper case letter ",password.matches(".*[A-Z].*"));
        add_valid_message("Must contain at least one lower case letter ",password.matches(".*[a-z].*"));
        add_valid_message("Must contain at least one special character ",password.matches(".*[!@#$%^&*].*"));
        add_valid_message("Must contain at least one number ",password.matches(".*[0-9].*"));
    }

    void add_valid_message(String text,boolean is_valid){
        Text t = new Text(text);
        Text symbol = new Text(is_valid? "✅":"❌");
        symbol.setFill(is_valid ? Color.rgb(43, 237, 17) : Color.RED);
        password_strength_message.getChildren().addAll(t,symbol,new Text("\n"));
    }

    @FXML
    void validate_repeated_password(KeyEvent event) {
        validate_repeated_password();
    }

    void validate_repeated_password() {
        password_same_message.getChildren().clear();
        String password = take_password_again.getText();
        Text symbol = new Text();
        if(!password.equals(take_password.getText()) || !valid_password(take_password.getText())){
            symbol.setText("❌");
            symbol.setFill(Color.RED);
        }
        else{
            symbol.setText("✅");
            symbol.setFill(Color.rgb(43, 237, 17));
        }
        password_same_message.getChildren().add(symbol);
    }

    boolean valid_password(String password){
        return password.length()>=8 && password.matches(".*[A-Z].*") && password.matches(".*[a-z].*") && password.matches(".*[!@#$%^&*].*") && password.matches(".*[0-9].*");
    }

}
