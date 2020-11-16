module com.ps.ui {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.ps.ui to javafx.fxml;
    opens com.ps.models to javafx.fxml;
    opens com.ps.executor to javafx.fxml;
    exports com.ps.ui;
    exports com.ps.models;
    exports com.ps.executor;
    requires javafx.graphicsEmpty;
}
