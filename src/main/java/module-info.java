module com.example.Client {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;
    requires mysql.connector.j;

    opens com.example.Client to javafx.fxml;
    exports com.example.Client;
    exports com.example.Client.controller;
    opens com.example.Client.controller to javafx.fxml;
}