package org.example;

/** Виняток для операції над об'єктом, якого немає в колекції. */
public class ObjectNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /** Створює виняток із повідомленням про відсутній об'єкт. */
    public ObjectNotFoundException(String message) {
        super(message);
    }
}