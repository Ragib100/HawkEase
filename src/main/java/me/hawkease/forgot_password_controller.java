package me.hawkease;

import javafx.fxml.FXML;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;

public class forgot_password_controller {

    @FXML
    private ToggleGroup Type;

    @FXML
    private TextField email;

    @FXML
    private TextField pass;

    @FXML
    private TextField enter_otp;

    String otp;

    @FXML
    void change_password(MouseEvent event) {
        if(enter_otp.getText().equals(otp)) {
            RadioButton radioButton = (RadioButton) Type.getSelectedToggle();
            users_sql users = new users_sql();
            users.change_password(email.getText(), pass.getText(), radioButton.getText());
        }
    }

    @FXML
    void send_email(MouseEvent event){
        try{
            send_email sendemail = new send_email();
            otp = secure_otp_generator.generateOTP();
            sendemail.sendEmail(email.getText(),otp);
        }
        catch(Exception e){
            System.out.println("OTP generation failed");
        }
    }

}
