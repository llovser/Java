package org.example;

import java.util.Objects;

/** Одяг: бренд, тип, розмір, колір, матеріал, ціна та кількість на складі. */
public class Clothes {

    private String brand;
    private String type;
    private String size;
    private String color;
    private String material;
    private double price;
    private int quantity;

    /**
     * Створює об'єкт одягу.
     *
     * @throws IllegalArgumentException якщо будь-яке значення некоректне
     */
    public Clothes(String brand, String type, String size, String color,
                   String material, double price, int quantity) {
        this.brand = brand;
        this.type = type;
        this.size = size;
        this.color = color;
        this.material = material;
        this.price = price;
        this.quantity = quantity;
    }

    /** Повертає бренд. */
    public String getBrand() {
        return brand;
    }

    /** Встановлює бренд (кидає IllegalArgumentException, якщо значення некоректне). */
    public void setBrand(String brand) {
        this.brand = brand;
    }

    /** Повертає тип одягу. */
    public String getType() {
        return type;
    }

    /** Встановлює тип (кидає IllegalArgumentException, якщо значення некоректне). */
    public void setType(String type) {
        this.type = type;
    }

    /** Повертає розмір. */
    public String getSize() {
        return size;
    }

    /** Встановлює розмір (кидає IllegalArgumentException, якщо значення некоректне). */
    public void setSize(String size) {
        this.size = size;
    }

    /** Повертає колір. */
    public String getColor() {
        return color;
    }

    /** Встановлює колір (кидає IllegalArgumentException, якщо значення некоректне). */
    public void setColor(String color) {
        this.color = color;
    }

    /** Повертає матеріал. */
    public String getMaterial() {
        return material;
    }

    /** Встановлює матеріал (кидає IllegalArgumentException, якщо значення некоректне). */
    public void setMaterial(String material) {
        this.material = material;
    }

    /** Повертає ціну. */
    public double getPrice() {
        return price;
    }

    /** Встановлює ціну (кидає IllegalArgumentException, якщо значення некоректне). */
    public void setPrice(double price) {
        this.price = price;
    }

    /** Повертає кількість на складі. */
    public int getQuantity() {
        return quantity;
    }

    /** Встановлює кількість (кидає IllegalArgumentException, якщо значення некоректне). */
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    /** Повертає текстове подання об'єкта. */
    @Override
    public String toString() {
        return "Clothes{brand='" + brand + "', type='" + type + "', size='" + size
                + "', color='" + color + "', material='" + material
                + "', price=" + price + ", quantity=" + quantity + "}";
    }

    /** Порівнює об'єкти за всіма полями. */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Clothes other = (Clothes) o;
        return Double.compare(price, other.price) == 0
                && quantity == other.quantity
                && Objects.equals(brand, other.brand)
                && Objects.equals(type, other.type)
                && Objects.equals(size, other.size)
                && Objects.equals(color, other.color)
                && Objects.equals(material, other.material);
    }

    /** Повертає хеш-код, узгоджений з equals. */
    @Override
    public int hashCode() {
        return Objects.hash(brand, type, size, color, material, price, quantity);
    }
}