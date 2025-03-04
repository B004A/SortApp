module labo4.tn {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;

    opens labo4.tn to javafx.fxml;
    exports labo4.tn;
}
