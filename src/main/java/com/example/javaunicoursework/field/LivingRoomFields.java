package com.example.javaunicoursework.field;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import lombok.Getter;

import java.util.Arrays;

@Getter
public class LivingRoomFields extends MainFields {
    private final Label furnitureTypeLabel = new Label("Тип мебели:");
    private final Label manufacturerLabel = new Label("Производитель:");
    private final TextField furnitureTypeEntry = new TextField();
    private final TextField manufacturerEntry = new TextField();

    public LivingRoomFields() {
        getLabels().addAll(Arrays.asList(furnitureTypeLabel, manufacturerLabel));
        getFields().addAll(Arrays.asList(furnitureTypeEntry, manufacturerEntry));
        Tooltip furnitureTypeTT = new Tooltip("Тип мебели");
        furnitureTypeLabel.setTooltip(furnitureTypeTT);

        Tooltip manufacturerTT = new Tooltip("Производитель");
        manufacturerLabel.setTooltip(manufacturerTT);
    }
}
