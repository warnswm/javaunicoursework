package com.example.javaunicoursework.database.mongo;

import com.example.javaunicoursework.database.IDatabase;
import com.example.javaunicoursework.shop.BathroomFurniture;
import com.example.javaunicoursework.shop.InternetShop;
import com.example.javaunicoursework.shop.KitchenFurniture;
import com.example.javaunicoursework.shop.LivingRoomFurniture;
import com.mongodb.BasicDBObject;
import com.mongodb.MongoException;
import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import lombok.Getter;
import lombok.val;
import org.bson.BsonDocument;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;


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
    public void addToDatabase(InternetShop furniture) {
        Document document = new Document("shopName", furniture.getShopName())
                .append("productName", furniture.getProductName())
                .append("countryOfOrigin", furniture.getCountryOfOrigin())
                .append("paymentMethod", furniture.getPaymentMethod())
                .append("purchaseAmount", furniture.getPurchaseAmount())
                .append("saleDate", furniture.getSaleDate())
                .append("customerName", furniture.getCustomerName());

        if (furniture instanceof KitchenFurniture kitchenFurniture) {
            document.append("length", kitchenFurniture.getLength())
                    .append("height", kitchenFurniture.getHeight())
                    .append("width", kitchenFurniture.getWidth())
                    .append("material", kitchenFurniture.getMaterial());
        }
        if (furniture instanceof LivingRoomFurniture livingRoomFurniture){
            document.append("furnitureType", livingRoomFurniture.getFurnitureType())
                    .append("manufacturer", livingRoomFurniture.getManufacturer());
        }

        collection = database.getCollection(furniture.getName());
        collection.insertOne(document);
    }

    @Override
    public void update(InternetShop furniture) {
        Bson filter = Filters.eq("productName", furniture.getProductName());
        Bson update;

        Document document = new Document("shopName", furniture.getShopName())
                .append("productName", furniture.getProductName())
                .append("countryOfOrigin", furniture.getCountryOfOrigin())
                .append("paymentMethod", furniture.getPaymentMethod())
                .append("purchaseAmount", furniture.getPurchaseAmount())
                .append("saleDate", furniture.getSaleDate())
                .append("customerName", furniture.getCustomerName());

        if (furniture instanceof KitchenFurniture kitchenFurniture) {
            document.append("length", kitchenFurniture.getLength())
                    .append("height", kitchenFurniture.getHeight())
                    .append("width", kitchenFurniture.getWidth())
                    .append("material", kitchenFurniture.getMaterial());
        } else if (furniture instanceof LivingRoomFurniture livingRoomFurniture) {
            document.append("furnitureType", livingRoomFurniture.getFurnitureType())
                    .append("manufacturer", livingRoomFurniture.getManufacturer());
        }

        update = new Document("$set", document);

        collection = database.getCollection(furniture.getName());
        collection.updateOne(filter, update);
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
        data.clear();
        if (searchText.isEmpty()) {
            tableView.getColumns().clear();
            updateTableView(tableView, data);
        } else if ("All".equals(selectedField)) {
            tableView.getColumns().clear();
            val selectCollection = collection.find().first();
            if (selectCollection == null) return;
            BasicDBObject regexQuery = new BasicDBObject();
            Pattern pattern = Pattern.compile(searchText, Pattern.CASE_INSENSITIVE);

            List<BasicDBObject> orFilters = new ArrayList<>();
            for (String key : selectCollection.keySet()) {
                if (!key.equals("_id")) {
                    BasicDBObject fieldFilter = new BasicDBObject();
                    fieldFilter.put(key, pattern);
                    orFilters.add(fieldFilter);
                }
            }

            regexQuery.put("$or", orFilters);
            FindIterable<Document> results = collection.find(regexQuery);

            for (String key : selectCollection.keySet()) {
                if (!key.equals("_id")) {
                    TableColumn<Document, Object> column = new TableColumn<>(key);
                    column.setCellValueFactory(param -> {
                        Document document = param.getValue();
                        return javafx.beans.binding.Bindings.createObjectBinding(() -> document.getOrDefault(key, null));
                    });
                    tableView.getColumns().add(column);
                }
            }

            ObservableList<Document> searchResults = FXCollections.observableArrayList();
            for (Document document : results) {
                searchResults.add(document);
            }

            tableView.getItems().clear();
            tableView.getItems().addAll(searchResults);
        } else {
            Document query = new Document(selectedField, searchText);
            FindIterable<Document> results = collection.find(query);
            ObservableList<Document> searchResults = FXCollections.observableArrayList();
            for (Document document : results) {
                searchResults.add(document);
            }

            tableView.getItems().clear();
            tableView.getItems().addAll(searchResults);
        }
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
