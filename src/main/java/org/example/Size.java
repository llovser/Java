package org.example;

import java.util.Locale;

/** Допустимі розміри одягу. */
public enum Size {
    XS, S, M, L, XL, XXL;

    /**
     * Перетворює рядок на розмір (регістр і зайві пробіли не важливі).
     */
    public static Size fromString(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("Розмір не може бути порожнім.");
        }
        String text = value.trim().toUpperCase(Locale.ROOT);
        for (Size size : values()) {
            if (size.name().equals(text)) {
                return size;
            }
        }
        throw new IllegalArgumentException("Невідомий розмір: " + value
                + " (допустимо: XS, S, M, L, XL, XXL).");
    }
}