package org.example;

import java.util.ArrayList;
import java.util.List;

/** Магазин одягу: володіє колекцією товарів, кількість кожного зберігається в самому товарі. */
public class Store {

    private final String name;
    private final List<Clothes> items = new ArrayList<>();

    /**
     * Створює порожній магазин.
     *
     * @throws IllegalArgumentException якщо назва порожня
     */
    public Store(String name) {
        this.name = Clothes.checkText(name, "Назва магазину");
    }

    /** Повертає назву магазину. */
    public String getName() {
        return name;
    }

    /** Повертає копію списку товарів (змінити колекцію магазину ззовні не можна). */
    public List<Clothes> getItems() {
        return new ArrayList<>(items);
    }

    /** Повертає кількість різних позицій. */
    public int size() {
        return items.size();
    }

    /** Повертає загальну кількість одиниць товару. */
    public int getTotalQuantity() {
        int total = 0;
        for (Clothes item : items) {
            total += item.getQuantity();
        }
        return total;
    }

    /**
     * Додає товар у кількості quantity: якщо такий уже є, збільшує його кількість.
     *
     * @return true, якщо додано нову позицію; false, якщо збільшено кількість існуючої
     * @throws IllegalArgumentException якщо товар null або кількість не додатна
     */
    public boolean addNewClothes(Clothes cl, int quantity) {
        if (cl == null) {
            throw new IllegalArgumentException("Товар не може бути порожнім.");
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("Кількість для додавання має бути додатною.");
        }
        for (Clothes item : items) {
            if (item.equals(cl)) {
                item.setQuantity(item.getQuantity() + quantity);
                return false;
            }
        }
        cl.setQuantity(quantity);
        items.add(cl);
        return true;
    }

    /** Повертає товари, бренд яких містить заданий текст (без урахування регістру). */
    public List<Clothes> findByBrand(String text) {
        List<Clothes> result = new ArrayList<>();
        String part = text.trim().toLowerCase();
        for (Clothes item : items) {
            if (item.getBrand().toLowerCase().contains(part)) {
                result.add(item);
            }
        }
        return result;
    }

    /** Повертає товари заданої категорії (без урахування регістру). */
    public List<Clothes> findByCategory(String category) {
        List<Clothes> result = new ArrayList<>();
        for (Clothes item : items) {
            if (item.getCategory().equalsIgnoreCase(category.trim())) {
                result.add(item);
            }
        }
        return result;
    }

    /** Повертає товари заданого розміру. */
    public List<Clothes> findBySize(Size size) {
        List<Clothes> result = new ArrayList<>();
        for (Clothes item : items) {
            if (item.getSize() == size) {
                result.add(item);
            }
        }
        return result;
    }

    /** Повертає товари, ціна яких лежить у межах від min до max включно. */
    public List<Clothes> findByPrice(double min, double max) {
        List<Clothes> result = new ArrayList<>();
        for (Clothes item : items) {
            if (item.getPrice() >= min && item.getPrice() <= max) {
                result.add(item);
            }
        }
        return result;
    }
}