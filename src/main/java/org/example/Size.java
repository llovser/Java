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
            throw new InvalidFieldValueException("Розмір не може бути порожнім.");
        }
        String text = value.trim().toUpperCase(Locale.ROOT);
        for (Size size : values()) {
            if (size.name().equals(text)) {
                return size;
            }
        }
        throw new InvalidFieldValueException("Невідомий розмір: " + value
                + " (допустимо: XS, S, M, L, XL, XXL).");
    }
}