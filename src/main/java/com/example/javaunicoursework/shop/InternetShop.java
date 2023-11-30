package com.example.javaunicoursework.shop;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Date;

@Getter
@AllArgsConstructor
public class InternetShop {
    String shopName;
    String productName;
    String countryOfOrigin;
    String paymentMethod;
    double purchaseAmount;
    Date saleDate;
    String customerName;

    public String getName(){
        return null;
    }
}

