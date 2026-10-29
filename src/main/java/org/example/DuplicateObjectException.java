package org.example;

/** Виняток для спроби створити в колекції дублікат наявного об'єкта. */
public class DuplicateObjectException extends IllegalArgumentException {

    private static final long serialVersionUID = 1L;

    /** Створює виняток із повідомленням про дублікат. */
    public DuplicateObjectException(String message) {
        super(message);
    }
}