package com.example.javaunicoursework.EShop;

import lombok.Getter;

import java.util.Date;
@Getter
public class InternetShop {
    String shopName;
    String productName;
    String countryOfOrigin;
    String paymentMethod;
    double purchaseAmount;
    Date saleDate;
    String customerName;

    public InternetShop(String shopName, String productName, String countryOfOrigin, String paymentMethod, double purchaseAmount, Date saleDate, String customerName) {
        this.shopName = shopName;
        this.productName = productName;
        this.countryOfOrigin = countryOfOrigin;
        this.paymentMethod = paymentMethod;
        this.purchaseAmount = purchaseAmount;
        this.saleDate = saleDate;
        this.customerName = customerName;
    }
}

