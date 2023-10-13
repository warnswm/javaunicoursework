package com.example.javaunicoursework.field;

import javafx.scene.layout.GridPane;

public interface IField {
    void show(boolean isShow);

    void init(GridPane gridPane);

    boolean isEmpty();
}
