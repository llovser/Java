package org.example;

import java.util.Objects;

/** Сорочка: одяг із типом рукава та розміром коміра. */
public class Shirts extends Clothes {

    public static final int MIN_NECK = 30;
    public static final int MAX_NECK = 50;

    private Sleeve sleeve;
    private int neckSize;

    /**
     * Створює сорочку.
     *
     * @throws InvalidFieldValueException якщо будь-яке значення некоректне
     */
    public Shirts(String brand, Size size, String color, String material,
                  double price, int quantity, Sleeve sleeve, int neckSize) {
        super(brand, "Сорочка", size, color, material, price, quantity);
        this.sleeve = checkSleeve(sleeve);
        this.neckSize = checkNeckSize(neckSize);
    }

    /** Повертає тип рукава. */
    public Sleeve getSleeve() {
        return sleeve;
    }

    /** Встановлює тип рукава (не null). */
    public void setSleeve(Sleeve sleeve) {
        this.sleeve = checkSleeve(sleeve);
    }

    /** Повертає розмір коміра в сантиметрах. */
    public int getNeckSize() {
        return neckSize;
    }

    /** Встановлює розмір коміра (від 30 до 50 см). */
    public void setNeckSize(int neckSize) {
        this.neckSize = checkNeckSize(neckSize);
    }

    /** Перевіряє, що тип рукава вказано. */
    public static Sleeve checkSleeve(Sleeve sleeve) {
        if (sleeve == null) {
            throw new InvalidFieldValueException("Тип рукава не може бути порожнім.");
        }
        return sleeve;
    }

    /** Перевіряє розмір коміра. */
    public static int checkNeckSize(int neckSize) {
        if (neckSize < MIN_NECK || neckSize > MAX_NECK) {
            throw new InvalidFieldValueException("Розмір коміра має бути від " + MIN_NECK
                    + " до " + MAX_NECK + " см.");
        }
        return neckSize;
    }

    @Override
    public Shirts copy() {
        Shirts result = new Shirts(getBrand(), getSize(), getColor(), getMaterial(),
                getPrice(), getQuantity(), sleeve, neckSize);
        copyIdentityTo(result);
        return result;
    }

    @Override
    public String getCategory() {
        return "Сорочка";
    }

    @Override
    public String toString() {
        String base = super.toString();
        return base.substring(0, base.length() - 1) + ", sleeve=" + sleeve
                + ", neckSize=" + neckSize + "}";
    }

    @Override
    public boolean equals(Object o) {
        if (!super.equals(o)) {
            return false;
        }
        Shirts other = (Shirts) o;
        return sleeve == other.sleeve && neckSize == other.neckSize;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), sleeve, neckSize);
    }
}