package com.example.javaunicoursework.shop;

import lombok.Getter;

import java.util.Date;

@Getter
public class LivingRoomFurniture extends InternetShop {
    String furnitureType;
    String manufacturer;

    public LivingRoomFurniture(String shopName, String productName, String countryOfOrigin, String paymentMethod, double purchaseAmount, Date saleDate, String customerName, String furnitureType, String manufacturer) {
        super(shopName, productName, countryOfOrigin, paymentMethod, purchaseAmount, saleDate, customerName);
        this.furnitureType = furnitureType;
        this.manufacturer = manufacturer;
    }
}
