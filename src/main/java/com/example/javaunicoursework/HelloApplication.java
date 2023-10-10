package com.example.javaunicoursework;

import com.example.javaunicoursework.Scenes.MainScene;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class HelloApplication extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        primaryStage.setTitle("Интернет-магазин");

        MainScene mainscene = new MainScene();
        Database database = new Database();
        Scene scene = mainscene.initScene(database);
        primaryStage.setScene(scene);
        primaryStage.setOnCloseRequest(e -> database.closeConnection());

        primaryStage.show();
    }
}