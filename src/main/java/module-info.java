module com.example.movieapp {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires java.net.http;
    requires com.google.gson;
    requires java.desktop;
    requires java.sql;

    opens com.example.movieapp to javafx.fxml, com.google.gson;
    exports com.example.movieapp;

}