module org.nikita.armaplus2 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens org.nikita.armaplus2 to javafx.fxml;
    exports org.nikita.armaplus2;
    exports org.nikita.armaplus2.controllers;
    opens org.nikita.armaplus2.controllers to javafx.fxml;
}