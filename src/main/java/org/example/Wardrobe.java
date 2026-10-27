package org.example;

import java.util.ArrayList;
import java.util.List;

/** Гардероб: агрегує об'єкти {@link Clothes}, які існують і без нього. */
public class Wardrobe {

    private final String name;
    private final List<Clothes> items = new ArrayList<>();

    /**
     * Створює порожній гардероб.
     *
     * @param name назва гардероба
     * @throws IllegalArgumentException якщо назва порожня
     */
    public Wardrobe(String name) {
        this.name = Clothes.checkText(name, "Назва гардероба");
    }

    /** Повертає назву гардероба. */
    public String getName() {
        return name;
    }

    /**
     * Додає одяг до гардероба.
     *
     * @param clothes одяг
     * @throws IllegalArgumentException якщо clothes дорівнює null
     */
    public void add(Clothes clothes) {
        if (clothes == null) {
            throw new IllegalArgumentException("Не можна додати null.");
        }
        items.add(clothes);
    }

    /**
     * Повертає одяг за індексом (з нуля).
     *
     * @param index індекс
     * @throws IllegalArgumentException якщо індекс поза межами списку
     */
    public Clothes get(int index) {
        if (index < 0 || index >= items.size()) {
            throw new IllegalArgumentException("Немає об'єкта з індексом " + index + ".");
        }
        return items.get(index);
    }

    /** Повертає кількість одягу в гардеробі. */
    public int size() {
        return items.size();
    }

    /** Повертає true, якщо гардероб порожній. */
    public boolean isEmpty() {
        return items.isEmpty();
    }

    /** Повертає загальну вартість: сума ціна * кількість для всього одягу. */
    public double getTotalValue() {
        double total = 0;
        for (Clothes clothes : items) {
            total += clothes.getPrice() * clothes.getQuantity();
        }
        return total;
    }
}