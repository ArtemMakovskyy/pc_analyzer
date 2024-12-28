package com.example.parser.utils;

public class ParseUtil {
    public static Double stringToDouble(String priceText) {
        if (priceText == null || priceText.trim().isEmpty()) {
            return 0.0;
        }
        try {
            if (priceText.matches("\\d+")) {
                return Double.parseDouble(priceText);
            }
        } catch (NumberFormatException e) {

        }
        return 0.0;
    }
}
