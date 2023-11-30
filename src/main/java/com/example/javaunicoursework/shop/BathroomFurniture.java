package com.example.javaunicoursework.shop;

import lombok.Getter;

import java.util.Date;

@Getter
public class BathroomFurniture extends InternetShop {
    public BathroomFurniture(String shopName, String productName, String countryOfOrigin, String paymentMethod, double purchaseAmount, Date saleDate, String customerName) {
        super(shopName, productName, countryOfOrigin, paymentMethod, purchaseAmount, saleDate, customerName);
    }

    @Override
    public String getName(){
        return "BathroomFurniture";
    }
}
