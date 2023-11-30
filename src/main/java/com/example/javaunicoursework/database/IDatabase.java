package com.example.javaunicoursework.database;

import com.example.javaunicoursework.shop.BathroomFurniture;
import com.example.javaunicoursework.shop.InternetShop;
import com.example.javaunicoursework.shop.KitchenFurniture;
import com.example.javaunicoursework.shop.LivingRoomFurniture;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;
import org.bson.Document;

public interface IDatabase {
    void connect();
    void addToDatabase(InternetShop furniture);
    void update(InternetShop furniture);
   void closeConnection();

    void selectCollection(String collection);

    void updateTableView(TableView<Document> tableView, ObservableList<Document> data);
    void updateTableViewAfterSearch(TableView<Document> tableView, String searchText, String selectedField, ObservableList<Document> data);

    void deleteFromDatabaseLivingRoom(String shopName, String productName);
    void deleteFromDatabaseBathroom(String shopName, String productName);
    void deleteFromDatabaseKitchen(String shopName, String productName);
    void updateComboBox(ComboBox<String> comboBox);

}
