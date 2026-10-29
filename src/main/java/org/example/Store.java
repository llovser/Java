package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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

    /**
     * Замінює існуючий товар новим (UUID зберігається); товар шукається за equals.
     *
     * @return true, якщо оновлено; false, якщо існуючого товару в колекції немає
     * @throws IllegalArgumentException якщо дані null або такий товар уже є в колекції
     */
    public boolean update(Clothes existingObject, Clothes newObject) {
        if (existingObject == null || newObject == null) {
            throw new IllegalArgumentException("Товар не може бути порожнім.");
        }
        int index = indexOf(existingObject);
        if (index < 0) {
            return false;
        }
        for (int i = 0; i < items.size(); i++) {
            if (i != index && items.get(i).equals(newObject)) {
                throw new IllegalArgumentException("Товар із такими даними вже є в магазині.");
            }
        }
        newObject.restoreUuid(items.get(index).getUuid());
        items.set(index, newObject);
        return true;
    }

    /**
     * Видаляє товар із колекції; товар шукається за equals.
     *
     * @return true, якщо видалено; false, якщо товару в колекції немає
     */
    public boolean delete(Clothes existingObject) {
        if (existingObject == null) {
            return false;
        }
        int index = indexOf(existingObject);
        if (index < 0) {
            return false;
        }
        items.remove(index);
        return true;
    }

    /** Повертає індекс товару в колекції за equals або -1. */
    private int indexOf(Clothes object) {
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).equals(object)) {
                return i;
            }
        }
        return -1;
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

    /**
     * Знаходить товар за UUID, записаним рядком.
     */
    public Clothes findByUuid(String text) {
        UUID uuid = parseUuid(text);
        for (Clothes item : items) {
            if (item.getUuid().equals(uuid)) {
                return item;
            }
        }
        return null;
    }

    /**
     * Перетворює рядок на UUID.
     */
    public static UUID parseUuid(String text) {
        if (text == null || text.trim().isEmpty()) {
            throw new IllegalArgumentException("UUID не може бути порожнім.");
        }
        try {
            return UUID.fromString(text.trim());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Некоректний формат UUID: " + text.trim());
        }
    }
}