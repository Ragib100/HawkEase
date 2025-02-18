module me.hawkease {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.base;
    requires java.desktop;
    requires java.sql;
    requires mysql.connector.j;
    requires jdk.httpserver;

    opens me.hawkease to javafx.fxml, javafx.base;
    exports me.hawkease;
}
