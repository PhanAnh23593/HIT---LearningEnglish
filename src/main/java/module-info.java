module org.example {

    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires static lombok;
    requires javafx.media;

    requires jbcrypt;
    requires mysql.connector.j;
    requires java.prefs;

    opens org.example.model to javafx.base;
    opens org.example to javafx.fxml;
    opens org.example.controller to javafx.fxml;
    opens org.example.controller.Review to javafx.fxml;

    exports org.example;
    exports org.example.controller;
}