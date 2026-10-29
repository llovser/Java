package org.example;

import java.util.Objects;

/** Куртка: одяг із сезоном і наявністю капюшона. */
public class Jacket extends Clothes {

    private Season season;
    private boolean hooded;

    /**
     * Створює куртку.
     *
     * @throws IllegalArgumentException якщо будь-яке значення некоректне
     */
    public Jacket(String brand, Size size, String color, String material,
                  double price, int quantity, Season season, boolean hooded) {
        super(brand, "Куртка", size, color, material, price, quantity);
        this.season = checkSeason(season);
        this.hooded = hooded;
    }

    /** Повертає сезон. */
    public Season getSeason() {
        return season;
    }

    /** Встановлює сезон (не null). */
    public void setSeason(Season season) {
        this.season = checkSeason(season);
    }

    /** Повертає true, якщо куртка з капюшоном. */
    public boolean isHooded() {
        return hooded;
    }

    /** Встановлює наявність капюшона. */
    public void setHooded(boolean hooded) {
        this.hooded = hooded;
    }

    /** Перевіряє, що сезон вказано. */
    public static Season checkSeason(Season season) {
        if (season == null) {
            throw new IllegalArgumentException("Сезон не може бути порожнім.");
        }
        return season;
    }

    @Override
    public Jacket copy() {
        Jacket result = new Jacket(getBrand(), getSize(), getColor(), getMaterial(),
                getPrice(), getQuantity(), season, hooded);
        copyIdentityTo(result);
        return result;
    }

    @Override
    public String getCategory() {
        return "Куртка";
    }

    @Override
    public String toString() {
        String base = super.toString();
        return base.substring(0, base.length() - 1) + ", season=" + season
                + ", hooded=" + hooded + "}";
    }

    @Override
    public boolean equals(Object o) {
        if (!super.equals(o)) {
            return false;
        }
        Jacket other = (Jacket) o;
        return season == other.season && hooded == other.hooded;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), season, hooded);
    }
}