package org.example;

/** Виняток для некоректного значення поля об'єкта або некоректних вхідних даних. */
public class InvalidFieldValueException extends IllegalArgumentException {

    private static final long serialVersionUID = 1L;

    /** Створює виняток із повідомленням про некоректне значення. */
    public InvalidFieldValueException(String message) {
        super(message);
    }
}