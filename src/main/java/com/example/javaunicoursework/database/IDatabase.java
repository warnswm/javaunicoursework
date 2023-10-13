package com.example.javaunicoursework.database;

import com.example.javaunicoursework.shop.BathroomFurniture;
import com.example.javaunicoursework.shop.KitchenFurniture;
import com.example.javaunicoursework.shop.LivingRoomFurniture;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;
import org.bson.Document;

public interface IDatabase {
    void connect();

    void addToDatabaseLivingRoom(LivingRoomFurniture furniture);

    void addToDatabaseBathroom(BathroomFurniture furniture);

    void addToDatabaseKitchen(KitchenFurniture furniture);

    void closeConnection();

    void selectCollection(String collection);

    void updateTableView(TableView<Document> tableView, ObservableList<Document> data);
}
