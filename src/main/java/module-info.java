module org.example {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires static lombok;

    // THÊM DÒNG NÀY ĐỂ SỬA LỖI
    requires jbcrypt;

    opens org.example to javafx.fxml;
    opens org.example.controller to javafx.fxml;

    exports org.example;
    exports org.example.controller;
}