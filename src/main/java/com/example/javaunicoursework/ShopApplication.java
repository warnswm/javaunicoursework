package com.example.javaunicoursework;

import com.example.javaunicoursework.database.IDatabase;
import com.example.javaunicoursework.database.mongo.MongoDatabase;
import com.example.javaunicoursework.scene.MainScene;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ShopApplication extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        primaryStage.setTitle("Интернет-магазин");

        MainScene mainscene = new MainScene();
        IDatabase database = new MongoDatabase();
        database.connect();
        Scene scene = mainscene.initScene(database);
        primaryStage.setScene(scene);
        primaryStage.setOnCloseRequest(e -> database.closeConnection());

        primaryStage.show();
    }
}