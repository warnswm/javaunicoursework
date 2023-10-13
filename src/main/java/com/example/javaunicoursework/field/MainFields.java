package com.example.javaunicoursework.field;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.GridPane;
import lombok.Getter;
import lombok.val;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
public abstract class MainFields implements IField {
    private final Label shopNameLabel = new Label("Название магазина");
    private final Label productNameLabel = new Label("Название товара");
    private final Label sellAmountLabel = new Label("Цена:");
    private final Label countryOfOriginLabel = new Label("Страна производитель");
    private final Label saleDateLabel = new Label("Дата продажи");
    private final Label customerNameLabel = new Label("ФИО покупателя");
    private final Label paymentMethodLabel = new Label("Вид оплаты");

    private final TextField shopNameEntry = new TextField();
    private final TextField productNameEntry = new TextField();
    private final TextField sellAmountEntry = new TextField();
    private final TextField countryOfOriginEntry = new TextField();
    private final TextField saleDateEntry = new TextField();
    private final TextField customerNameEntry = new TextField();
    private final TextField paymentMethodEntry = new TextField();
    @Getter
    private final List<Label> labels = new ArrayList<>(Arrays.asList(shopNameLabel, productNameLabel, sellAmountLabel, countryOfOriginLabel, saleDateLabel, customerNameLabel, paymentMethodLabel));
    @Getter
    private final List<TextField> fields = new ArrayList<>(Arrays.asList(shopNameEntry, productNameEntry, sellAmountEntry, countryOfOriginEntry, saleDateEntry, customerNameEntry, paymentMethodEntry));

    public MainFields() {
        Tooltip shopNameTT = new Tooltip("Название магазина");
        shopNameLabel.setTooltip(shopNameTT);

        Tooltip productNameTT = new Tooltip("Название товара");
        productNameLabel.setTooltip(productNameTT);

        Tooltip countryOfOriginTT = new Tooltip("Страна производитель");
        countryOfOriginLabel.setTooltip(countryOfOriginTT);

        Tooltip paymentMethodTT = new Tooltip("Вид оплаты");
        paymentMethodLabel.setTooltip(paymentMethodTT);

        Tooltip sellAmountTT = new Tooltip("Цена");
        sellAmountLabel.setTooltip(sellAmountTT);

        Tooltip saleDateTT = new Tooltip("Дата продажи");
        saleDateLabel.setTooltip(saleDateTT);

        Tooltip customerNameTT = new Tooltip("ФИО покупателя");
        customerNameLabel.setTooltip(customerNameTT);
    }

    @Override
    public void init(GridPane grid) {
        for (int i = 0; i < labels.size(); i++) {
            Label label = labels.get(i);
            grid.add(label, 0, i + 1);
            labels.get(i).setId("grid");
        }
        for (int i = 0; i < fields.size(); i++) {
            TextField field = fields.get(i);
            grid.add(field, 1, i + 1);
        }
        show(true);
    }

    @Override
    public void show(boolean isShow) {
        for (Label mainLabel : labels) {
            mainLabel.setVisible(isShow);
            mainLabel.setManaged(isShow);
        }
        for (TextField mainFields : fields) {
            mainFields.setVisible(isShow);
            mainFields.setManaged(isShow);
            mainFields.clear();
        }
    }

    @Override
    public boolean isEmpty() {
        for (final TextField field : fields) {
            if (field.getText().isEmpty()) return true;
        }
        return false;
    }
}
