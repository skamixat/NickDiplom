module org.nikita.armaplus {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.xerial.sqlitejdbc; // Если вы используете SQLite JDBC driver как модуль

    opens org.nikita.armaplus.controllers to javafx.fxml;
    opens org.nikita.armaplus.database.model to javafx.base; // <--- Эта строка открывает пакет для javafx.base
    exports org.nikita.armaplus;
    exports org.nikita.armaplus.controllers;
    exports org.nikita.armaplus.database.model; // Возможно, вам также нужно экспортировать этот пакет
}