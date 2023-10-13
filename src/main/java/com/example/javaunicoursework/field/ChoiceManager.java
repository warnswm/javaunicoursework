package com.example.javaunicoursework.field;

import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import lombok.Getter;

public class ChoiceManager {
    private final Label choiceLabel = new Label("Выберите магазин:");
    @Getter
    private final ChoiceBox<String> storeChoice = createChoiceBox("Мебель для гостиных", "Мебель для кухни", "Мебель для ванн");
    private final IField livingRoomField = new LivingRoomFields();
    private final IField kitchenField = new KitchenFields();
    private final IField bathRoomField = new BathroomFields();
    @Getter
    private IField selectField;

    public void initChoiceManager(GridPane grid) {
        grid.add(choiceLabel, 0, 0);
        grid.add(storeChoice, 1, 0);

        livingRoomField.init(grid);
        kitchenField.init(grid);
        bathRoomField.init(grid);

        livingRoomField.show(true);
        kitchenField.show(false);
        bathRoomField.show(false);
        selectField = livingRoomField;

        storeChoice.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> updateFields(newValue));
    }

    private ChoiceBox<String> createChoiceBox(String... items) {
        ChoiceBox<String> choiceBox = new ChoiceBox<>();
        choiceBox.getItems().addAll(items);
        choiceBox.setValue("Мебель для гостиных");
        return choiceBox;
    }

    private void updateFields(String storeChoice) {
        switch (storeChoice.toLowerCase()) {
            case "мебель для кухни" -> {
                kitchenField.show(true);
                bathRoomField.show(false);
                livingRoomField.show(false);
                selectField = kitchenField;
            }
            case "мебель для ванн" -> {
                kitchenField.show(false);
                bathRoomField.show(true);
                livingRoomField.show(false);
                selectField = bathRoomField;
            }
            default -> {
                kitchenField.show(false);
                bathRoomField.show(false);
                livingRoomField.show(true);
                selectField = livingRoomField;
            }
        }
    }

    public boolean isEmpty() {

        return selectField.isEmpty();
    }
}
