module org.example {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens org.example.View to javafx.fxml;
    exports org.example;
}