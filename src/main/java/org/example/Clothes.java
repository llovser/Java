package org.example;

import java.util.Objects;

/** Одяг: бренд, тип, розмір, колір, матеріал, ціна та кількість на складі. */
public class Clothes {

    private static int count;
    private String brand;
    private String type;
    private Size size;
    private String color;
    private String material;
    private double price;
    private int quantity;

    /**
     * Створює об'єкт одягу.
     *
     * @throws IllegalArgumentException якщо будь-яке значення некоректне
     */
    public Clothes(String brand, String type, Size size, String color,
                   String material, double price, int quantity) {
        this.brand = checkText(brand, "Бренд");
        this.type = checkText(type, "Тип");
        this.size = checkSize(size);
        this.color = checkText(color, "Колір");
        this.material = checkText(material, "Матеріал");
        this.price = checkPrice(price);
        this.quantity = checkQuantity(quantity);
        count++;
    }

    public static int getCount() {
        return count;
    }

    /** Повертає бренд. */
    public String getBrand() {
        return brand;
    }

    /** Встановлює бренд (кидає IllegalArgumentException, якщо значення некоректне). */
    public void setBrand(String brand) {
        this.brand = checkText(brand, "Бренд");
    }

    /** Повертає тип одягу. */
    public String getType() {
        return type;
    }

    /** Встановлює тип (кидає IllegalArgumentException, якщо значення некоректне). */
    public void setType(String type) {
        this.type = checkText(type, "Тип");
    }

    /** Повертає розмір. */
    public Size getSize() {
        return size;
    }

    /** Встановлює розмір (кидає IllegalArgumentException, якщо значення некоректне). */
    public void setSize(Size size) {
        this.size = checkSize(size);
    }

    /** Повертає колір. */
    public String getColor() {
        return color;
    }

    /** Встановлює колір (кидає IllegalArgumentException, якщо значення некоректне). */
    public void setColor(String color) {
        this.color = checkText(color, "Колір");
    }

    /** Повертає матеріал. */
    public String getMaterial() {
        return material;
    }

    /** Встановлює матеріал (кидає IllegalArgumentException, якщо значення некоректне). */
    public void setMaterial(String material) {
        this.material = checkText(material, "Матеріал");
    }

    /** Повертає ціну. */
    public double getPrice() {
        return price;
    }

    /** Встановлює ціну (кидає IllegalArgumentException, якщо значення некоректне). */
    public void setPrice(double price) {
        this.price = checkPrice(price);
    }

    /** Повертає кількість на складі. */
    public int getQuantity() {
        return quantity;
    }

    /** Встановлює кількість (кидає IllegalArgumentException, якщо значення некоректне). */
    public void setQuantity(int quantity) {
        this.quantity = checkQuantity(quantity);
    }

    /**
     * Перевіряє, що рядок не порожній, і повертає його без зайвих пробілів.
     */
    public static String checkText(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(fieldName + " не може бути порожнім.");
        }
        return value.trim();
    }

    /**
     * Перевіряє розмір: допустимі XS, S, M, L, XL, XXL (регістр не важливий).
     */
    public static Size checkSize(Size size) {
        if (size == null) {
            throw new IllegalArgumentException("Розмір не може бути порожнім.");
        }
        return size;
    }

    /**
     * Перевіряє, що ціна є скінченним додатним числом.
     */
    public static double checkPrice(double price) {
        if (Double.isNaN(price) || Double.isInfinite(price) || price <= 0) {
            throw new IllegalArgumentException("Ціна має бути додатним числом.");
        }
        return price;
    }

    /**
     * Перевіряє, що кількість не від'ємна.
     */
    public static int checkQuantity(int quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException("Кількість не може бути від'ємною.");
        }
        return quantity;
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