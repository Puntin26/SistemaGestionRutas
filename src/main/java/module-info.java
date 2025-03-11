module com.example.demoproyfinal {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires smartgraph;

    opens com.example.demoproyfinal to javafx.fxml;
    exports com.example.demoproyfinal;
}