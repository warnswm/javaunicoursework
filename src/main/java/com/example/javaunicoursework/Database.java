package com.example.javaunicoursework;

import com.example.javaunicoursework.EShop.BathroomFurniture;
import com.example.javaunicoursework.EShop.InternetShop;
import com.example.javaunicoursework.EShop.KitchenFurniture;
import com.example.javaunicoursework.EShop.LivingRoomFurniture;
import com.mongodb.MongoException;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import lombok.Getter;
import lombok.Setter;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Database {
    @Getter
    @Setter
    private MongoCollection<Document> collection;
    private final Logger logger = LoggerFactory.getLogger(Database.class.getName());
    private MongoClient mongoClient;
    @Getter
    private MongoDatabase database;

    public Database() {
        try {
            mongoClient = MongoClients.create("mongodb://localhost:27017");
            database = mongoClient.getDatabase("java_curs");
            logger.info("Database connected");
        } catch (MongoException e) {
            logger.info("Error connection to database");
        }
    }

    public void addToDatabase(InternetShop furniture) {
        Document document = new Document("shopName", furniture.getShopName())
                .append("productName", furniture.getProductName())
                .append("countryOfOrigin", furniture.getCountryOfOrigin())
                .append("paymentMethod", furniture.getPaymentMethod())
                .append("purchaseAmount", furniture.getPurchaseAmount())
                .append("saleDate", furniture.getSaleDate())
                .append("customerName", furniture.getCustomerName());
        collection = database.getCollection("InternetShop");
        collection.insertOne(document);
    }

    public void addToDatabase(LivingRoomFurniture furniture) {
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

    public void addToDatabase(BathroomFurniture furniture) {
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

    public void addToDatabase(KitchenFurniture furniture) {
        Document document = new Document("shopName", furniture.getShopName())
                .append("productName", furniture.getProductName())
                .append("countryOfOrigin", furniture.getCountryOfOrigin())
                .append("paymentMethod", furniture.getPaymentMethod())
                .append("purchaseAmount", furniture.getPurchaseAmount())
                .append("saleDate", furniture.getSaleDate())
                .append("customerName", furniture.getCustomerName())
                .append("length", furniture.getLength())
                .append("height",furniture.getHeight())
                .append("width", furniture.getWidth())
                .append("material",furniture.getMaterial());
        collection = database.getCollection("KitchenFurniture");
        collection.insertOne(document);
    }

    public void closeConnection() {
        mongoClient.close();
        logger.info("Database connection closed");
    }

}
