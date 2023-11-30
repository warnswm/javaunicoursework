package com.example.javaunicoursework.shop;


import lombok.Getter;

import java.util.Date;

@Getter
public class KitchenFurniture extends InternetShop {
    double length;
    double height;
    double width;
    String material;

    public KitchenFurniture(String shopName, String productName, String countryOfOrigin, String paymentMethod, double purchaseAmount, Date saleDate, String customerName, double length, double height, double width, String material) {
        super(shopName, productName, countryOfOrigin, paymentMethod, purchaseAmount, saleDate, customerName);
        this.length = length;
        this.height = height;
        this.width = width;
        this.material = material;
    }

    @Override
    public String getName(){
        return "KitchenFurniture";
    }
}
