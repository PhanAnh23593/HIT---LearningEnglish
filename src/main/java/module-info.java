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
    opens org.example.controller.Review to javafx.fxml;

    exports org.example;
    exports org.example.controller.Learning;
    opens org.example.controller.Learning to javafx.fxml;
    exports org.example.controller.Dashboard;
    opens org.example.controller.Dashboard to javafx.fxml;
    exports org.example.controller.Authentication;
    opens org.example.controller.Authentication to javafx.fxml;
    exports org.example.controller.Test;
    opens org.example.controller.Test to javafx.fxml;
    opens org.example.controller.Profile to javafx.fxml;
    opens org.example.controller.Speaking to javafx.fxml;
}