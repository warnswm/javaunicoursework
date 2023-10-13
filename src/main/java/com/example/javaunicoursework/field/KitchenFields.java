package com.example.javaunicoursework.field;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import lombok.Getter;

import java.util.Arrays;

@Getter
public class KitchenFields extends MainFields {
    private final Label lengthLabel = new Label("Длина:");
    private final Label heightLabel = new Label("Высота:");
    private final Label widthLabel = new Label("Ширина:");
    private final Label materialLabel = new Label("Материал:");
    private final TextField lengthEntry = new TextField();
    private final TextField heightEntry = new TextField();
    private final TextField widthEntry = new TextField();
    private final TextField materialEntry = new TextField();

    public KitchenFields() {
        getLabels().addAll(Arrays.asList(lengthLabel, heightLabel, widthLabel, materialLabel));
        getFields().addAll(Arrays.asList(lengthEntry, heightEntry, widthEntry, materialEntry));
        Tooltip lengthTT = new Tooltip("Длина товара");
        lengthLabel.setTooltip(lengthTT);

        Tooltip heightTT = new Tooltip("Высота товара");
        heightLabel.setTooltip(heightTT);

        Tooltip widthTT = new Tooltip("Ширина товара");
        widthLabel.setTooltip(widthTT);

        Tooltip materialTT = new Tooltip("Материал товара");
        materialLabel.setTooltip(materialTT);
    }

}
