package org.nikita.armaplus2;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("MainView.fxml"));
        Scene scene = new Scene(loader.load());
        primaryStage.setTitle("Учёт финансов арматурного завода");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        Database.connect();
        launch(args);
        Database.disconnect();
    }
}
