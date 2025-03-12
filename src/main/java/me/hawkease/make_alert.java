package me.hawkease;

import javafx.scene.control.Alert;

public class make_alert {
    private static make_alert instance;
    private Alert alert;
    public static make_alert getInstance(){
        if(instance==null){
            instance=new make_alert();
        }
        return instance;
    }
    public void make_alert(String title, String content){
        alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
