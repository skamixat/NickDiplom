module org.nikita.armaplus2 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.xerial.sqlitejdbc; // Если вы используете SQLite JDBC driver как модуль

    opens org.nikita.armaplus2.controllers to javafx.fxml;
    opens org.nikita.armaplus2.database.model to javafx.base; // <--- Эта строка открывает пакет для javafx.base
    exports org.nikita.armaplus2;
    exports org.nikita.armaplus2.controllers;
    exports org.nikita.armaplus2.database.model; // Возможно, вам также нужно экспортировать этот пакет
}