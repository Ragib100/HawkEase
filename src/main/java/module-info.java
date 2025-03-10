module me.hawkease {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.base;
    requires java.desktop;
    requires javafx.swing;
    requires org.jxmapviewer.jxmapviewer2;
    requires java.sql;
    requires mysql.connector.j;
    requires jdk.httpserver;
    requires com.google.auth.oauth2;
    requires firebase.admin;
    requires com.google.auth;
    requires jakarta.mail;
    requires transitive javafx.graphics;

    opens me.hawkease to javafx.fxml, javafx.base;
    exports me.hawkease;
}
