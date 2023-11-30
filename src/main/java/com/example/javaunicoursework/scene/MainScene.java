package com.example.javaunicoursework.scene;

import com.example.javaunicoursework.database.IDatabase;
import com.example.javaunicoursework.field.*;
import com.example.javaunicoursework.searchBar.AutoCompleteComboBoxListener;
import com.example.javaunicoursework.shop.BathroomFurniture;
import com.example.javaunicoursework.shop.InternetShop;
import com.example.javaunicoursework.shop.KitchenFurniture;
import com.example.javaunicoursework.shop.LivingRoomFurniture;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import org.bson.Document;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.function.BiConsumer;

public class MainScene {
    SimpleDateFormat inputDateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);
    ChoiceManager choiceManager = new ChoiceManager();
    private final ObservableList<Document> data = FXCollections.observableArrayList();
    private static final String ADD_BUTTON_URL = "https://icon-library.com/images/new-button-icon/new-button-icon-2.jpg";
    private static final String DELETE_BUTTON_URL = "https://cdn-icons-png.flaticon.com/512/1214/1214428.png";
    private static final String UPDATE_BUTTON_URL = "https://cdn-icons-png.flaticon.com/512/65/65368.png";


    public Scene initScene(IDatabase database) {
        GridPane grid = createGridPane();
        choiceManager.initChoiceManager(grid);

        Button addButton = createButton("Добавить", ADD_BUTTON_URL);
        Button deleteButton = createButton("Удалить", DELETE_BUTTON_URL);
        Button updateButton = createButton("Обновить", UPDATE_BUTTON_URL);
        GridPane.setConstraints(addButton, 0, 14);
        GridPane.setConstraints(deleteButton, 1, 14);
        GridPane.setConstraints(updateButton, 0, 15);

        grid.getChildren().addAll(addButton, deleteButton,updateButton);

        TableView<Document> tableView = createTableView(database);
        TextField searchField = createSearchField();
        ComboBox<String> comboBox = createComboBox(database, searchField);
        Button searchButton = createSearchButton(searchField, comboBox);

        HBox searchBox = createSearchBox(searchField, comboBox, searchButton);
        choiceManager.getStoreChoice().getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            updateTable(newValue, database, tableView);
            updateComboBox(newValue, database, comboBox);
        });
        addButton.setOnAction(e -> addButtonClicked(database, tableView));
        deleteButton.setOnAction(e -> deleteButtonClicked(database, tableView));
        updateButton.setOnAction(e -> updateButtonClicked(database,tableView));
        tableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> updateFields(newSelection));
        searchField.setOnAction(event -> performSearch(database, tableView, searchField.getText(), comboBox.getValue()));


        HBox hbox = new HBox(grid, new VBox(searchBox, tableView));
        return new Scene(hbox, 1200, 450);
    }

    private GridPane createGridPane() {
        GridPane grid = new GridPane();
        grid.setVgap(3);
        grid.setPadding(new Insets(20, 20, 20, 20));
        String css = Objects.requireNonNull(getClass().getResource("/style.css")).toExternalForm();
        grid.getStylesheets().add(css);
        grid.setStyle("-fx-padding: 10;" +
                "-fx-border-style: solid inside;" +
                "-fx-border-width: 2px;" +
                "-fx-border-insets: 5px;" +
                "-fx-border-radius: 5px;" +
                "-fx-border-color: #0BAAE4;");
        return grid;
    }

    private TableView<Document> createTableView(IDatabase database) {
        TableView<Document> tableView = new TableView<>();
        tableView.setEditable(false);
        tableView.setPadding(new Insets(5, 0, 0, 0));
        database.selectCollection("LivingRoomFurniture");
        loadDataFromCollection(database, tableView);
        tableView.setItems(data);
        tableView.setPrefWidth(800);
        tableView.setPrefHeight(400);
        String css = Objects.requireNonNull(getClass().getResource("/style.css")).toExternalForm();
        tableView.getStylesheets().add(css);
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        return tableView;
    }

    private TextField createSearchField() {
        TextField searchField = new TextField();
        String css = Objects.requireNonNull(getClass().getResource("/style.css")).toExternalForm();
        searchField.getStylesheets().add(css);
        searchField.setPromptText("Search...");
        searchField.getStyleClass().add("search-field");
        return searchField;
    }

    private ComboBox<String> createComboBox(IDatabase database, TextField searchField) {
        ComboBox<String> comboBox = new ComboBox<>();
        database.updateComboBox(comboBox);
        comboBox.setEditable(true);
        AutoCompleteComboBoxListener<String> listener = new AutoCompleteComboBoxListener<>(comboBox);
        comboBox.getEditor().setOnKeyReleased(listener);
        HBox.setHgrow(searchField, Priority.ALWAYS);
        return comboBox;
    }

    private Button createSearchButton(TextField searchField, ComboBox<String> comboBox) {
        Button searchButton = new Button("Search");
        String css = Objects.requireNonNull(getClass().getResource("/style.css")).toExternalForm();
        searchButton.getStylesheets().add(css);
        searchButton.getStyleClass().add("search-button");
        //searchButton.setOnAction(event -> performSearch(searchField.getText(), comboBox.getValue()));
        return searchButton;
    }

    private HBox createSearchBox(TextField searchField, ComboBox<String> comboBox, Button searchButton) {
        HBox searchBox = new HBox(searchField, comboBox, searchButton);
        String css = Objects.requireNonNull(getClass().getResource("/style.css")).toExternalForm();
        searchBox.getStylesheets().add(css);
        searchBox.getStyleClass().add("search-box");
        searchBox.setAlignment(Pos.CENTER_LEFT);
        return searchBox;
    }
    private void performSearch(IDatabase database,TableView<Document> tableView, String searchText, String filterText) {
        data.clear();
        database.updateTableViewAfterSearch(tableView,searchText,filterText,data);
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
    private void updateComboBox(String storeChoice, IDatabase database, ComboBox<String> comboBox){
        switch (storeChoice) {
            case "Мебель для гостиных" -> database.selectCollection("LivingRoomFurniture");
            case "Мебель для кухни" -> database.selectCollection("KitchenFurniture");
            case "Мебель для ванн" -> database.selectCollection("BathroomFurniture");
        }
        comboBox.getItems().clear();
        database.updateComboBox(comboBox);
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

    public Button createButton(String text, String iconUrl) {
        ImageView icon = createButtonIcon(iconUrl);
        Button button = new Button(text, icon);
        customizeButton(button);
        return button;
    }

    private ImageView createButtonIcon(String imageUrl) {
        Image buttonImage = new Image(imageUrl);
        ImageView buttonIcon = new ImageView(buttonImage);
        buttonIcon.setFitWidth(20);
        buttonIcon.setFitHeight(21);
        return buttonIcon;
    }

    private void customizeButton(Button button) {
        button.setVisible(true);
        button.setManaged(true);
        button.setId("myB");
        button.getStyleClass().add("button");
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

    private void handleButtonClick(IDatabase database, TableView<Document> tableView, BiConsumer<IDatabase, InternetShop> action) {
        if (choiceManager.isEmpty() || choiceManager.getSelectField().isEmpty()) {
            alert();
            return;
        }

        MainFields mainFields = (MainFields) choiceManager.getSelectField();

        InternetShop furniture;
        try {
            furniture = createFurniture(mainFields);
        } catch (ParseException e) {
            alert();
            return;
        }

        if (furniture != null) {
            action.accept(database, furniture);
            loadDataFromCollection(database, tableView);
        }
    }

    private InternetShop createFurniture(MainFields mainFields) throws ParseException {
        String shopName = mainFields.getShopNameEntry().getText();
        String productName = mainFields.getProductNameEntry().getText();
        String countryOfOrigin = mainFields.getCountryOfOriginEntry().getText();
        String paymentMethod = mainFields.getPaymentMethodEntry().getText();
        double purchaseAmount = parseDoubleValue(mainFields.getSellAmountEntry().getText());
        Date saleDate = inputDateFormat.parse(mainFields.getSaleDateEntry().getText());
        String customerName = mainFields.getCustomerNameEntry().getText();

        if (mainFields instanceof KitchenFields kitchenFields) {
            return new KitchenFurniture(shopName, productName, countryOfOrigin, paymentMethod,
                    purchaseAmount, saleDate, customerName,
                    parseDoubleValue(kitchenFields.getLengthEntry().getText()),
                    parseDoubleValue(kitchenFields.getHeightEntry().getText()),
                    parseDoubleValue(kitchenFields.getWidthEntry().getText()),
                    kitchenFields.getMaterialEntry().getText());
        } else if (mainFields instanceof LivingRoomFields livingRoomFields) {
            return new LivingRoomFurniture(shopName, productName, countryOfOrigin, paymentMethod,
                    purchaseAmount, saleDate, customerName,
                    livingRoomFields.getFurnitureTypeEntry().getText(),
                    livingRoomFields.getManufacturerEntry().getText());
        } else if (mainFields instanceof BathroomFields) {
            return new BathroomFurniture(shopName, productName, countryOfOrigin, paymentMethod,
                    purchaseAmount, saleDate, customerName);
        }

        return null;
    }

    private void updateButtonClicked(IDatabase database, TableView<Document> tableView) {
        handleButtonClick(database, tableView, IDatabase::update);
    }

    private void addButtonClicked(IDatabase database, TableView<Document> tableView) {
        handleButtonClick(database, tableView, IDatabase::addToDatabase);
    }

}