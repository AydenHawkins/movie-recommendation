module com.example.movierecommendation {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;

    opens com.example.movierecommendation to javafx.fxml;
    exports com.example.movierecommendation;
}