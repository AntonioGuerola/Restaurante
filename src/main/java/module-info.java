module org.example {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.xml.bind;

    opens org.example.View to javafx.fxml;
    opens org.example.Model.Entity to java.xml.bind;
    exports org.example;
}