package org.example;

import java.util.Map;

/** Створює об'єкти ієрархії з рядкових значень (наприклад, із полів форми). */
public class ClothesFactory {

    public static final String PANTS = "Штани";
    public static final String SHIRTS = "Сорочка";
    public static final String JACKET = "Куртка";
    public static final String DRESS = "Сукня";

    /** Назви доступних категорій у порядку відображення. */
    public static String[] categories() {
        return new String[]{PANTS, SHIRTS, JACKET, DRESS};
    }

    /**
     * Створює об'єкт вказаної категорії. Ключі fields: brand, size, color, material, price,
     * quantity, number (довжина або розмір коміра), choice (крій, рукав, сезон або призначення),
     * hooded ("true" або "false" для куртки).
     *
     * @throws IllegalArgumentException якщо категорія невідома або значення некоректне
     */
    public static Clothes create(String category, Map<String, String> fields) {
        String brand = text(fields, "brand");
        Size size = Size.fromString(text(fields, "size"));
        String color = text(fields, "color");
        String material = text(fields, "material");
        double price = parsePrice(text(fields, "price"));
        int quantity = parseInt(text(fields, "quantity"), "Кількість");
        String choice = text(fields, "choice");
        if (PANTS.equals(category)) {
            return new Pants(brand, size, color, material, price, quantity,
                    parseInt(text(fields, "number"), "Довжина"), parseEnum(Fit.class, choice));
        }
        if (SHIRTS.equals(category)) {
            return new Shirts(brand, size, color, material, price, quantity,
                    parseEnum(Sleeve.class, choice), parseInt(text(fields, "number"), "Розмір коміра"));
        }
        if (JACKET.equals(category)) {
            return new Jacket(brand, size, color, material, price, quantity,
                    parseEnum(Season.class, choice), Boolean.parseBoolean(text(fields, "hooded")));
        }
        if (DRESS.equals(category)) {
            return new Dress(brand, size, color, material, price, quantity,
                    parseInt(text(fields, "number"), "Довжина"), parseEnum(Occasion.class, choice));
        }
        throw new IllegalArgumentException("Невідома категорія: " + category);
    }

    private static String text(Map<String, String> fields, String key) {
        String value = fields.get(key);
        return value == null ? "" : value;
    }

    private static double parsePrice(String value) {
        try {
            return Double.parseDouble(value.trim().replace(',', '.'));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Ціна має бути числом.");
        }
    }

    private static int parseInt(String value, String fieldName) {
        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(fieldName + " має бути цілим числом.");
        }
    }

    private static <T extends Enum<T>> T parseEnum(Class<T> type, String value) {
        try {
            return Enum.valueOf(type, value.trim());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Оберіть значення зі списку.");
        }
    }
}