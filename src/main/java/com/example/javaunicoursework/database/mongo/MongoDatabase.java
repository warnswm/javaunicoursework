package com.example.javaunicoursework.database.mongo;

import com.example.javaunicoursework.database.IDatabase;
import com.example.javaunicoursework.shop.BathroomFurniture;
import com.example.javaunicoursework.shop.KitchenFurniture;
import com.example.javaunicoursework.shop.LivingRoomFurniture;
import com.mongodb.MongoException;
import com.mongodb.client.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import lombok.Getter;
import lombok.val;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class MongoDatabase implements IDatabase {
    private MongoCollection<Document> collection;
    private final Logger logger = LoggerFactory.getLogger(IDatabase.class.getName());
    private MongoClient mongoClient;
    @Getter
    private com.mongodb.client.MongoDatabase database;

    @Override
    public void connect() {
        try {
            mongoClient = MongoClients.create("mongodb://localhost:27017");
            database = mongoClient.getDatabase("java_curs");
            logger.info("Database connected");
        } catch (MongoException e) {
            logger.info("Error connection to database");
        }
    }

    @Override
    public void addToDatabaseLivingRoom(LivingRoomFurniture furniture) {
        Document document = new Document("shopName", furniture.getShopName())
                .append("productName", furniture.getProductName())
                .append("countryOfOrigin", furniture.getCountryOfOrigin())
                .append("paymentMethod", furniture.getPaymentMethod())
                .append("purchaseAmount", furniture.getPurchaseAmount())
                .append("saleDate", furniture.getSaleDate())
                .append("customerName", furniture.getCustomerName())
                .append("furnitureType", furniture.getFurnitureType())
                .append("manufacturer", furniture.getManufacturer());
        collection = database.getCollection("LivingRoomFurniture");
        collection.insertOne(document);
    }

    @Override
    public void addToDatabaseBathroom(BathroomFurniture furniture) {
        Document document = new Document("shopName", furniture.getShopName())
                .append("productName", furniture.getProductName())
                .append("countryOfOrigin", furniture.getCountryOfOrigin())
                .append("paymentMethod", furniture.getPaymentMethod())
                .append("purchaseAmount", furniture.getPurchaseAmount())
                .append("saleDate", furniture.getSaleDate())
                .append("customerName", furniture.getCustomerName());
        collection = database.getCollection("BathroomFurniture");
        collection.insertOne(document);
    }

    @Override
    public void addToDatabaseKitchen(KitchenFurniture furniture) {
        Document document = new Document("shopName", furniture.getShopName())
                .append("productName", furniture.getProductName())
                .append("countryOfOrigin", furniture.getCountryOfOrigin())
                .append("paymentMethod", furniture.getPaymentMethod())
                .append("purchaseAmount", furniture.getPurchaseAmount())
                .append("saleDate", furniture.getSaleDate())
                .append("customerName", furniture.getCustomerName())
                .append("length", furniture.getLength())
                .append("height", furniture.getHeight())
                .append("width", furniture.getWidth())
                .append("material", furniture.getMaterial());
        collection = database.getCollection("KitchenFurniture");
        collection.insertOne(document);
    }

    @Override
    public void closeConnection() {
        mongoClient.close();
        logger.info("Database connection closed");
    }

    @Override
    public void selectCollection(String collection) {
        this.collection = database.getCollection(collection);
    }

    @Override
    public void updateTableView(TableView<Document> tableView, ObservableList<Document> data) {
        val selectCollection = collection.find().first();
        if (selectCollection == null) return;
        selectCollection.forEach((key, value) -> {
            if (!key.equals("_id")) {
                TableColumn<Document, Object> column = new TableColumn<>(key);
                column.setCellValueFactory(param -> {
                    Document document = param.getValue();
                    return javafx.beans.binding.Bindings.createObjectBinding(() -> document.getOrDefault(key, null));
                });
                tableView.getColumns().add(column);
            }
        });
        collection.find().forEach(data::add);
    }

    @Override
    public void updateTableViewAfterSearch(TableView<Document> tableView, String searchText, String selectedField, ObservableList<Document> data) {
        Document query = new Document(selectedField, searchText);
        FindIterable<Document> results = collection.find(query);
        ObservableList<Document> searchResults = FXCollections.observableArrayList();
        for (Document document : results) {
            searchResults.add(document);
        }

        tableView.getItems().clear();
        // Добавляем новые результаты поиска в таблицу
        tableView.getItems().addAll(searchResults);
    }

    @Override
    public void deleteFromDatabaseLivingRoom(String shopName, String productName){
        Document criteria = new Document("shopName", shopName)
                .append("productName", productName);

        if (database.getCollection("LivingRoomFurniture").find(criteria).first() != null) {
            database.getCollection("LivingRoomFurniture").deleteOne(criteria);
            System.out.println("Document deleted.");
        } else {
            System.out.println("Document not find.");
        }
    }
    @Override
    public void deleteFromDatabaseBathroom(String shopName, String productName){
        Document criteria = new Document("shopName", shopName)
                .append("productName", productName);

        if (database.getCollection("BathroomFurniture").find(criteria).first() != null) {
            database.getCollection("BathroomFurniture").deleteOne(criteria);
            System.out.println("Document deleted.");
        } else {
            System.out.println("Document not find.");
        }
    }

    @Override
    public void deleteFromDatabaseKitchen(String shopName, String productName) {
        Document criteria = new Document("shopName", shopName)
                .append("productName", productName);

        if (database.getCollection("KitchenFurniture").find(criteria).first() != null) {
            database.getCollection("KitchenFurniture").deleteOne(criteria);
            System.out.println("Document deleted.");
        } else {
            System.out.println("Document not find.");
        }
    }

    @Override
    public void updateComboBox(ComboBox<String> comboBox) {
        val selectCollection = collection.find().first();
        if (selectCollection == null) return;
        comboBox.getItems().add("All");
        selectCollection.forEach((key, value) -> {
            if (!key.equals("_id")) {
                comboBox.getItems().add(key);
            }
        });
        comboBox.setValue("All");
    }



}
