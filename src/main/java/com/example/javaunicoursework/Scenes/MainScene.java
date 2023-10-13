package com.example.javaunicoursework.Scenes;

import com.example.javaunicoursework.Database;
import com.example.javaunicoursework.EShop.BathroomFurniture;
import com.example.javaunicoursework.EShop.InternetShop;
import com.example.javaunicoursework.EShop.KitchenFurniture;
import com.example.javaunicoursework.EShop.LivingRoomFurniture;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
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
    private Label shopNameLabel, productNameLabel, sellAmountLabel, countryOfOriginLabel, saleDateLabel, customerNameLabel,
            paymentMethodLabel, furnitureTypeLabel, manufacturerLabel, lengthLabel, heightLabel, widthLabel, materialLabel;
    private TextField shopNameEntry, productNameEntry, sellAmountEntry, countryOfOriginEntry, saleDateEntry, customerNameEntry,
            furnitureTypeEntry, paymentMethodEntry, manufacturerEntry, lengthEntry, heightEntry, widthEntry, materialEntry;
    private final ObservableList<Document> data = FXCollections.observableArrayList();

    public Scene initScene(Database database) {
        GridPane grid = new GridPane();
        grid.setVgap(3);
        grid.setPadding(new Insets(20, 20, 20, 20));
        String css = Objects.requireNonNull(getClass().getResource("/style.css")).toExternalForm();
        grid.getStylesheets().add(css);

        Label storeChoiceLabel = new Label("Выберите магазин:");
        grid.add(storeChoiceLabel, 0, 0);
        ChoiceBox<String> storeChoice = createChoiceBox("Интернет-магазин", "Мебель для гостиных", "Мебель для кухни", "Мебель для ванн");
        grid.add(storeChoice, 1, 0);

        initLabelAndTextField(grid);

        storeChoice.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> updateFields(newValue));

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
        database.setCollection(database.getDatabase().getCollection("InternetShop"));
        loadDataFromCollection(database, tableView);
        tableView.setItems(data);
        tableView.setPrefWidth(800);
        tableView.setPrefHeight(400);
        tableView.getStylesheets().add(css);
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);


        addButton.setOnAction(e -> addButtonClicked(database, storeChoice, tableView));
        storeChoice.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> updateTable(newValue, database, tableView));

        tableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            Object selectedStore = storeChoice.getSelectionModel().getSelectedItem();

            if (newSelection != null) {
                shopNameEntry.setText(newSelection.getString("shopName"));
                productNameEntry.setText(String.valueOf(newSelection.getString("productName")));
                countryOfOriginEntry.setText(newSelection.getString("countryOfOrigin"));
                paymentMethodEntry.setText(newSelection.getString("paymentMethod"));
                sellAmountEntry.setText(String.valueOf(newSelection.getDouble("purchaseAmount")));
                saleDateEntry.setText(String.valueOf(newSelection.getDate("saleDate")));
                customerNameEntry.setText(newSelection.getString("customerName"));
                if (selectedStore.equals("Мебель для гостиных")) {
                    furnitureTypeEntry.setText(newSelection.getString("furnitureType"));
                    manufacturerEntry.setText(newSelection.getString("manufacturer"));
                }
                if (selectedStore.equals("Мебель для кухни")){
                    lengthEntry.setText(String.valueOf(newSelection.getDouble("length")));
                    heightEntry.setText(String.valueOf(newSelection.getDouble("height")));
                    widthEntry.setText(String.valueOf(newSelection.getDouble("width")));
                    materialEntry.setText(newSelection.getString("material"));
                }

            }
        });

        HBox hbox = new HBox();
        VBox vBox = new VBox();
        vBox.getChildren().add(tableView);
        hbox.getChildren().add(grid);
        hbox.getChildren().add(vBox);
        return new Scene(hbox, 1200, 450);
    }

    private void updateTable(String storeChoice, Database database, TableView<Document> tableView) {
        shopNameEntry.clear();
        productNameEntry.clear();
        sellAmountEntry.clear();
        countryOfOriginEntry.clear();
        saleDateEntry.clear();
        customerNameEntry.clear();
        furnitureTypeEntry.clear();
        paymentMethodEntry.clear();
        manufacturerEntry.clear();
        lengthEntry.clear();
        heightEntry.clear();
        widthEntry.clear();
        materialEntry.clear();
        switch (storeChoice) {
            case "Интернет-магазин" -> database.setCollection(database.getDatabase().getCollection("InternetShop"));
            case "Мебель для гостиных" ->
                    database.setCollection(database.getDatabase().getCollection("LivingRoomFurniture"));
            case "Мебель для кухни" ->
                    database.setCollection(database.getDatabase().getCollection("BathroomFurniture"));
            case "Мебель для ванн" -> database.setCollection(database.getDatabase().getCollection("KitchenFurniture"));
        }
        loadDataFromCollection(database, tableView);
    }

    private void loadDataFromCollection(Database database, TableView<Document> tableView) {
        data.clear();
        tableView.getColumns().clear();
        try {
            Objects.requireNonNull(database.getCollection().find().first()).forEach((key, value) -> {
                if (!key.equals("_id")) {
                    TableColumn<Document, Object> column = new TableColumn<>(key);
                    column.setCellValueFactory(param -> {
                        Document document = param.getValue();
                        return javafx.beans.binding.Bindings.createObjectBinding(() -> document.getOrDefault(key, null));
                    });
                    tableView.getColumns().add(column);
                }
            });
            database.getCollection().find().forEach(data::add);
        } catch (Exception e) {
            System.out.println(e);
        }

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

    private void updateFields(String storeChoice) {
        boolean isStore0Selected = Objects.equals(storeChoice, "Интернет-магазин");
        boolean isStore1Selected = Objects.equals(storeChoice, "Мебель для гостиных");
        boolean isStore2Selected = Objects.equals(storeChoice, "Мебель для кухни");
        boolean isStore3Selected = Objects.equals(storeChoice, "Мебель для ванн");
        setVisibilityAndManagedStatus(isStore0Selected || isStore1Selected || isStore2Selected || isStore3Selected,
                isStore0Selected || isStore1Selected || isStore2Selected || isStore3Selected,
                shopNameLabel, productNameLabel, countryOfOriginLabel, paymentMethodLabel, sellAmountLabel, saleDateLabel, customerNameLabel,
                shopNameEntry, productNameEntry, countryOfOriginEntry, paymentMethodEntry, sellAmountEntry, saleDateEntry, customerNameEntry);

        setVisibilityAndManagedStatus(isStore1Selected, isStore1Selected,
                furnitureTypeLabel, manufacturerLabel,
                furnitureTypeEntry, manufacturerEntry);

        setVisibilityAndManagedStatus(isStore2Selected, isStore2Selected,
                lengthLabel, heightLabel, widthLabel, materialLabel,
                lengthEntry, heightEntry, widthEntry, materialEntry);

    }

    private void setVisibilityAndManagedStatus(boolean isVisible, boolean isManaged, Node... nodes) {
        for (Node node : nodes) {
            node.setVisible(isVisible);
            node.setManaged(isManaged);
        }
    }

    private void initLabelAndTextField(GridPane grid) {
        shopNameLabel = new Label("Название магазина");
        productNameLabel = new Label("Название товара");
        countryOfOriginLabel = new Label("Страна производитель");
        paymentMethodLabel = new Label("Вид оплаты");
        sellAmountLabel = new Label("Цена:");
        saleDateLabel = new Label("Дата продажи");
        customerNameLabel = new Label("ФИО покупателя");
        furnitureTypeLabel = new Label("Тип мебели:");
        manufacturerLabel = new Label("Производитель:");
        lengthLabel = new Label("Длина:");
        heightLabel = new Label("Высота:");
        widthLabel = new Label("Ширина:");
        materialLabel = new Label("Материал:");

        shopNameEntry = new TextField();
        productNameEntry = new TextField();
        countryOfOriginEntry = new TextField();
        paymentMethodEntry = new TextField();
        sellAmountEntry = new TextField();
        saleDateEntry = new TextField();
        customerNameEntry = new TextField();
        furnitureTypeEntry = new TextField();
        manufacturerEntry = new TextField();
        lengthEntry = new TextField();
        heightEntry = new TextField();
        widthEntry = new TextField();
        materialEntry = new TextField();

        grid.add(shopNameLabel, 0, 1);
        grid.add(shopNameEntry, 1, 1);
        grid.add(productNameLabel, 0, 2);
        grid.add(productNameEntry, 1, 2);
        grid.add(countryOfOriginLabel, 0, 3);
        grid.add(countryOfOriginEntry, 1, 3);
        grid.add(paymentMethodLabel, 0, 4);
        grid.add(paymentMethodEntry, 1, 4);
        grid.add(saleDateLabel, 0, 5);
        grid.add(saleDateEntry, 1, 5);
        grid.add(customerNameLabel, 0, 6);
        grid.add(customerNameEntry, 1, 6);
        grid.add(sellAmountLabel, 0, 7);
        grid.add(sellAmountEntry, 1, 7);
        grid.add(furnitureTypeLabel, 0, 8);
        grid.add(furnitureTypeEntry, 1, 8);
        grid.add(manufacturerLabel, 0, 9);
        grid.add(manufacturerEntry, 1, 9);
        grid.add(lengthLabel, 0, 10);
        grid.add(lengthEntry, 1, 10);
        grid.add(heightLabel, 0, 11);
        grid.add(heightEntry, 1, 11);
        grid.add(widthLabel, 0, 12);
        grid.add(widthEntry, 1, 12);
        grid.add(materialLabel, 0, 13);
        grid.add(materialEntry, 1, 13);

        shopNameLabel.setId("grid");
        productNameLabel.setId("grid");
        countryOfOriginLabel.setId("grid");
        paymentMethodLabel.setId("grid");
        sellAmountLabel.setId("grid");
        saleDateLabel.setId("grid");
        customerNameLabel.setId("grid");
        furnitureTypeLabel.setId("grid");
        manufacturerLabel.setId("grid");
        lengthLabel.setId("grid");
        heightLabel.setId("grid");
        widthLabel.setId("grid");
        materialLabel.setId("grid");
        setVisibilityAndManagedStatus(false,
                false,
                furnitureTypeEntry, manufacturerEntry, lengthEntry, heightEntry, widthEntry, materialEntry,
                furnitureTypeLabel, manufacturerLabel, lengthLabel, heightLabel, widthLabel, materialLabel);
    }

    private void deleteButtonClicked(Database database, ChoiceBox<String> storeChoice, TableView<Document> tableView){
        if (shopNameEntry.getText().isEmpty() | productNameEntry.getText().isEmpty() ){
            alert();
            return;
        }
        String shopName = shopNameEntry.getText();
        String productName = productNameEntry.getText();
    }
    private void addButtonClicked(Database database, ChoiceBox<String> storeChoice, TableView<Document> tableView) {
        if (shopNameEntry.getText().isEmpty() | productNameEntry.getText().isEmpty() |
                countryOfOriginEntry.getText().isEmpty() | paymentMethodEntry.getText().isEmpty()
                | sellAmountEntry.getText().isEmpty() | saleDateEntry.getText().isEmpty()
                | countryOfOriginEntry.getText().isEmpty()) {
            alert();
            return;
        }

        String shopName = shopNameEntry.getText();
        String productName = productNameEntry.getText();
        String countryOfOrigin = countryOfOriginEntry.getText();
        String paymentMethod = paymentMethodEntry.getText();
        double purchaseAmount;
        try {
            purchaseAmount = Double.parseDouble(sellAmountEntry.getText());
        } catch (Exception e) {
            alert();
            return;
        }
        String saleDateString = saleDateEntry.getText();
        Date saleDate;
        try {
            saleDate = dateFormat.parse(saleDateString.replaceAll("[.,]", "/"));
        } catch (ParseException ex) {
            alert();
            return;
        }
        String customerName = countryOfOriginEntry.getText();
        String selectedStore = storeChoice.getValue();
        switch (selectedStore) {
            case "Интернет-магазин" -> {
                InternetShop internetShop = new InternetShop(shopName, productName, countryOfOrigin, paymentMethod, purchaseAmount,
                        saleDate, customerName);
                database.addToDatabase(internetShop);
            }
            case "Мебель для гостиных" -> {
                if (furnitureTypeEntry.getText().isEmpty() | manufacturerEntry.getText().isEmpty()) {
                    alert();
                    return;
                }
                String furnitureType = furnitureTypeEntry.getText();
                String manufacturer = manufacturerEntry.getText();
                LivingRoomFurniture furniture = new LivingRoomFurniture(shopName, productName, countryOfOrigin, paymentMethod,
                        purchaseAmount, saleDate, customerName, furnitureType, manufacturer);
                database.addToDatabase(furniture);
            }
            case "Мебель для кухни" -> {
                if (lengthEntry.getText().isEmpty() | heightEntry.getText().isEmpty()
                        | widthEntry.getText().isEmpty() | materialEntry.getText().isEmpty()) {
                    alert();
                    return;
                }
                double length = Double.parseDouble(lengthEntry.getText());
                double height = Double.parseDouble(heightEntry.getText());
                double width = Double.parseDouble(widthEntry.getText());
                String material = materialEntry.getText();
                KitchenFurniture kitchenFurniture = new KitchenFurniture(shopName, productName, countryOfOrigin, paymentMethod, purchaseAmount,
                        saleDate, customerName, length, height, width, material);
                database.addToDatabase(kitchenFurniture);
            }
            case "Мебель для ванн" -> {
                BathroomFurniture bathroomFurniture = new BathroomFurniture(shopName, productName, countryOfOrigin, paymentMethod, purchaseAmount,
                        saleDate, customerName);
                database.addToDatabase(bathroomFurniture);
            }
        }
        loadDataFromCollection(database, tableView);
    }

    private ChoiceBox<String> createChoiceBox(String... items) {
        ChoiceBox<String> choiceBox = new ChoiceBox<>();
        choiceBox.getItems().addAll(items);
        choiceBox.setValue("Интернет-магазин");
        return choiceBox;
    }

    private void alert() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning");
        alert.setHeaderText("Вы ввели не верные данные");
        alert.setContentText("Пожалуйста перепроверьте данные.");
        alert.showAndWait();
    }
}