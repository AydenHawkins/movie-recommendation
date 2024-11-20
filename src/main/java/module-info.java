module com.example.movierecommendation {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires java.net.http;
    requires com.google.gson;

    opens com.example.movierecommendation to javafx.fxml, com.google.gson;
    exports com.example.movierecommendation;

}