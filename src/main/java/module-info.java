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
    exports com.example.movieapp.models;
    opens com.example.movieapp.models to com.google.gson, javafx.fxml;
    exports com.example.movieapp.controllers;
    opens com.example.movieapp.controllers to com.google.gson, javafx.fxml;
    exports com.example.movieapp.api;
    opens com.example.movieapp.api to com.google.gson, javafx.fxml;
    exports com.example.movieapp.services;
    opens com.example.movieapp.services to com.google.gson, javafx.fxml;

}