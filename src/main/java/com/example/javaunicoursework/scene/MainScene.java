package com.example.javaunicoursework.scene;

import com.example.javaunicoursework.database.IDatabase;
import com.example.javaunicoursework.field.*;
import com.example.javaunicoursework.shop.BathroomFurniture;
import com.example.javaunicoursework.shop.KitchenFurniture;
import com.example.javaunicoursework.shop.LivingRoomFurniture;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.bson.Document;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class MainScene {
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    ChoiceManager choiceManager = new ChoiceManager();
    private final ObservableList<Document> data = FXCollections.observableArrayList();

    public Scene initScene(IDatabase database) {
        GridPane grid = new GridPane();
        grid.setVgap(3);
        grid.setPadding(new Insets(20, 20, 20, 20));
        String css = Objects.requireNonNull(getClass().getResource("/style.css")).toExternalForm();
        grid.getStylesheets().add(css);
        grid.setStyle("fx-padding: 10;" +
                "-fx-border-style: solid inside;" +
                "-fx-border-width: 2;" +
                "-fx-border-insets: 5;" +
                "-fx-border-radius: 5;" +
                "-fx-border-color: #0BAAE4;");


        choiceManager.initChoiceManager(grid);

        String addbuttonurl = "https://icon-library.com/images/new-button-icon/new-button-icon-2.jpg";
        Image addbuttonimage = new Image(addbuttonurl);
        ImageView addbuttonicon = new ImageView(addbuttonimage);
        Button addButton = createButton("Добавить", addbuttonicon);
        String deletebuttonurl = "https://cdn-icons-png.flaticon.com/512/1214/1214428.png";
        Image deletebuttonimage = new Image(deletebuttonurl);
        ImageView deletebuttonicon = new ImageView(deletebuttonimage);
        Button deleteButton = createButton("Удалить", deletebuttonicon);
        grid.add(addButton, 0, 14);
        grid.add(deleteButton, 1, 14);

        TableView<Document> tableView = new TableView<>();
        tableView.setEditable(false);
        tableView.setPadding(new Insets(5, 0, 0, 0));
        database.selectCollection("LivingRoomFurniture");
        loadDataFromCollection(database, tableView);
        tableView.setItems(data);
        tableView.setPrefWidth(800);
        tableView.setPrefHeight(400);
        tableView.getStylesheets().add(css);
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);


        addButton.setOnAction(e -> addButtonClicked(database, tableView));
        deleteButton.setOnAction(e -> deleteButtonClicked(database,tableView));
        choiceManager.getStoreChoice().getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> updateTable(newValue, database, tableView));
        tableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> updateFields(newSelection));

        HBox hbox = new HBox();
        VBox vBox = new VBox();
        vBox.getChildren().add(tableView);
        hbox.getChildren().add(grid);
        hbox.getChildren().add(vBox);
        return new Scene(hbox, 1200, 450);
    }

    private void deleteButtonClicked(IDatabase database, TableView<Document> tableView){
        IField selectedField = choiceManager.getSelectField();
        if (selectedField instanceof MainFields mainFields) {
            if (mainFields.getShopNameEntry().getText().isEmpty() | mainFields.getProductNameEntry().getText().isEmpty()) {
                alert();
                return;
            }
            if (selectedField instanceof LivingRoomFields) {
                database.deleteFromDatabaseLivingRoom(mainFields.getShopNameEntry().getText(),mainFields.getProductNameEntry().getText());

            } else if (selectedField instanceof KitchenFields) {
                database.deleteFromDatabaseKitchen(mainFields.getShopNameEntry().getText(),mainFields.getProductNameEntry().getText());
            } else if (selectedField instanceof BathroomFields) {
                database.deleteFromDatabaseBathroom(mainFields.getShopNameEntry().getText(),mainFields.getProductNameEntry().getText());
            }
            loadDataFromCollection(database, tableView);
        }
    }
    private void updateFields(Document newSelection){
        IField selectedField = choiceManager.getSelectField();
        if (newSelection == null) return;
        if (selectedField instanceof MainFields mainFields) {
            mainFields.getShopNameEntry().setText(newSelection.getString("shopName"));
            mainFields.getProductNameEntry().setText(String.valueOf(newSelection.getString("productName")));
            mainFields.getCountryOfOriginEntry().setText(newSelection.getString("countryOfOrigin"));
            mainFields.getPaymentMethodEntry().setText(newSelection.getString("paymentMethod"));
            mainFields.getSellAmountEntry().setText(String.valueOf(newSelection.getDouble("purchaseAmount")));
            mainFields.getSaleDateEntry().setText(String.valueOf(newSelection.getDate("saleDate")));
            mainFields.getCustomerNameEntry().setText(newSelection.getString("customerName"));

            if (selectedField instanceof LivingRoomFields livingRoomFields) {
                livingRoomFields.getFurnitureTypeEntry().setText(newSelection.getString("furnitureType"));
                livingRoomFields.getManufacturerEntry().setText(newSelection.getString("manufacturer"));
            }
            if (selectedField instanceof KitchenFields kitchenFields) {
                kitchenFields.getLengthEntry().setText(String.valueOf(newSelection.getDouble("length")));
                kitchenFields.getHeightEntry().setText(String.valueOf(newSelection.getDouble("height")));
                kitchenFields.getWidthEntry().setText(String.valueOf(newSelection.getDouble("width")));
                kitchenFields.getMaterialEntry().setText(newSelection.getString("material"));
            }
        }
    }
    private void updateTable(String storeChoice, IDatabase database, TableView<Document> tableView) {
        switch (storeChoice) {
            case "Мебель для гостиных" -> database.selectCollection("LivingRoomFurniture");
            case "Мебель для кухни" -> database.selectCollection("KitchenFurniture");
            case "Мебель для ванн" -> database.selectCollection("BathroomFurniture");
        }
        loadDataFromCollection(database, tableView);
    }

    private void loadDataFromCollection(IDatabase database, TableView<Document> tableView) {
        data.clear();
        tableView.getColumns().clear();
        database.updateTableView(tableView, data);
    }

    private Button createButton(String text, ImageView icon) {
        icon.setFitWidth(20);
        icon.setFitHeight(21);
        Button addButton = new Button(text, icon);
        addButton.setVisible(true);
        addButton.setManaged(true);
        addButton.setId("myB");
        addButton.getStyleClass().add("button");

        return addButton;
    }
    private void addButtonClicked(IDatabase database, TableView<Document> tableView) {
        if (choiceManager.isEmpty()) {
            alert();
            return;
        }
        IField selectedField = choiceManager.getSelectField();

        if (selectedField.isEmpty()) {
            alert();
            return;
        }

        if (selectedField instanceof MainFields mainFields) {
            String shopName = mainFields.getShopNameEntry().getText();
            String productName = mainFields.getProductNameEntry().getText();
            String countryOfOrigin = mainFields.getCountryOfOriginEntry().getText();
            String paymentMethod = mainFields.getPaymentMethodEntry().getText();
            double purchaseAmount = parseDoubleValue(mainFields.getSellAmountEntry().getText());
            Date saleDate = null;
            try {
                saleDate = dateFormat.parse(mainFields.getSaleDateEntry().getText().replaceAll("[.,]", "/"));
            } catch (ParseException e) {
                alert();
            }
            if (saleDate == null) return;
            String customerName = mainFields.getCustomerNameEntry().getText();
            if (selectedField instanceof KitchenFields) {
                KitchenFields kitchenFields = (KitchenFields) mainFields;
                double length = parseDoubleValue(kitchenFields.getLengthEntry().getText());
                double height = parseDoubleValue(kitchenFields.getHeightEntry().getText());
                double width = parseDoubleValue(kitchenFields.getWidthEntry().getText());
                String material = kitchenFields.getMaterialEntry().getText();
                KitchenFurniture kitchenFurniture = new KitchenFurniture(shopName, productName, countryOfOrigin, paymentMethod,
                        purchaseAmount, saleDate, customerName, length, height, width, material);
                database.addToDatabaseKitchen(kitchenFurniture);
            } else if (selectedField instanceof LivingRoomFields) {
                LivingRoomFields livingRoomFields = (LivingRoomFields) mainFields;
                String furnitureType = livingRoomFields.getFurnitureTypeEntry().getText();
                String manufacturer = livingRoomFields.getManufacturerEntry().getText();
                LivingRoomFurniture livingRoomFurniture = new LivingRoomFurniture(shopName, productName, countryOfOrigin, paymentMethod,
                        purchaseAmount, saleDate, customerName, furnitureType, manufacturer);
                database.addToDatabaseLivingRoom(livingRoomFurniture);
            } else if (selectedField instanceof BathroomFields) {
                BathroomFurniture bathroomFurniture = new BathroomFurniture(shopName, productName, countryOfOrigin, paymentMethod,
                        purchaseAmount, saleDate, customerName);
                database.addToDatabaseBathroom(bathroomFurniture);
            }
        }
        loadDataFromCollection(database, tableView);
    }

    private void alert() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning");
        alert.setHeaderText("Вы ввели не верные данные");
        alert.setContentText("Пожалуйста перепроверьте данные.");
        alert.showAndWait();
    }

    public double parseDoubleValue(String text) {
        try {
            return Double.parseDouble(text);
        } catch (Exception e) {
            alert();
            return 0.0;
        }
    }
}