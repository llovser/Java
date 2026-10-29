package org.example;

import java.util.Objects;

/** Сукня: одяг із довжиною та призначенням. */
public class Dress extends Clothes {

    public static final int MIN_LENGTH = 60;
    public static final int MAX_LENGTH = 160;

    private int length;
    private Occasion occasion;

    /**
     * Створює сукню.
     *
     * @throws IllegalArgumentException якщо будь-яке значення некоректне
     */
    public Dress(String brand, Size size, String color, String material,
                 double price, int quantity, int length, Occasion occasion) {
        super(brand, "Сукня", size, color, material, price, quantity);
        this.length = checkLength(length);
        this.occasion = checkOccasion(occasion);
    }

    /** Повертає довжину сукні в сантиметрах. */
    public int getLength() {
        return length;
    }

    /** Встановлює довжину (від 60 до 160 см). */
    public void setLength(int length) {
        this.length = checkLength(length);
    }

    /** Повертає призначення. */
    public Occasion getOccasion() {
        return occasion;
    }

    /** Встановлює призначення (не null). */
    public void setOccasion(Occasion occasion) {
        this.occasion = checkOccasion(occasion);
    }

    /** Перевіряє довжину сукні. */
    public static int checkLength(int length) {
        if (length < MIN_LENGTH || length > MAX_LENGTH) {
            throw new IllegalArgumentException("Довжина має бути від " + MIN_LENGTH
                    + " до " + MAX_LENGTH + " см.");
        }
        return length;
    }

    /** Перевіряє, що призначення вказано. */
    public static Occasion checkOccasion(Occasion occasion) {
        if (occasion == null) {
            throw new IllegalArgumentException("Призначення не може бути порожнім.");
        }
        return occasion;
    }

    @Override
    public Dress copy() {
        Dress result = new Dress(getBrand(), getSize(), getColor(), getMaterial(),
                getPrice(), getQuantity(), length, occasion);
        copyIdentityTo(result);
        return result;
    }

    @Override
    public String getCategory() {
        return "Сукня";
    }

    @Override
    public String toString() {
        String base = super.toString();
        return base.substring(0, base.length() - 1) + ", length=" + length
                + ", occasion=" + occasion + "}";
    }

    @Override
    public boolean equals(Object o) {
        if (!super.equals(o)) {
            return false;
        }
        Dress other = (Dress) o;
        return length == other.length && occasion == other.occasion;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), length, occasion);
    }
}