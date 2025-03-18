module SortApp.tn {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;

    opens SortApp.tn to javafx.fxml;

    exports SortApp.tn;
}
