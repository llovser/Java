package org.example;

import java.util.Objects;

/** Штани: одяг із довжиною та кроєм. */
public class Pants extends Clothes {

    public static final int MIN_LENGTH = 50;
    public static final int MAX_LENGTH = 130;

    private int length;
    private Fit fit;

    /**
     * Створює штани.
     *
     * @throws InvalidFieldValueException якщо будь-яке значення некоректне
     */
    public Pants(String brand, Size size, String color, String material,
                 double price, int quantity, int length, Fit fit) {
        super(brand, "Штани", size, color, material, price, quantity);
        this.length = checkLength(length);
        this.fit = checkFit(fit);
    }

    /** Повертає довжину штанів у сантиметрах. */
    public int getLength() {
        return length;
    }

    /** Встановлює довжину (від 50 до 130 см). */
    public void setLength(int length) {
        this.length = checkLength(length);
    }

    /** Повертає крій. */
    public Fit getFit() {
        return fit;
    }

    /** Встановлює крій (не null). */
    public void setFit(Fit fit) {
        this.fit = checkFit(fit);
    }

    /** Перевіряє довжину штанів. */
    public static int checkLength(int length) {
        if (length < MIN_LENGTH || length > MAX_LENGTH) {
            throw new InvalidFieldValueException("Довжина має бути від " + MIN_LENGTH
                    + " до " + MAX_LENGTH + " см.");
        }
        return length;
    }

    /** Перевіряє, що крій вказано. */
    public static Fit checkFit(Fit fit) {
        if (fit == null) {
            throw new InvalidFieldValueException("Крій не може бути порожнім.");
        }
        return fit;
    }

    @Override
    public Pants copy() {
        Pants result = new Pants(getBrand(), getSize(), getColor(), getMaterial(),
                getPrice(), getQuantity(), length, fit);
        copyIdentityTo(result);
        return result;
    }

    @Override
    public String getCategory() {
        return "Штани";
    }

    @Override
    public String toString() {
        String base = super.toString();
        return base.substring(0, base.length() - 1) + ", length=" + length + ", fit=" + fit + "}";
    }

    @Override
    public boolean equals(Object o) {
        if (!super.equals(o)) {
            return false;
        }
        Pants other = (Pants) o;
        return length == other.length && fit == other.fit;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), length, fit);
    }
}