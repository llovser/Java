package org.example;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/** Тести перевірки коректності даних у класі {@link Clothes}. */
class ClothesTest {

    /** Мінімальна конкретна реалізація абстрактного класу для перевірки спільної логіки. */
    private static class Item extends Clothes {
        Item(String brand, String type, Size size, String color, String material, double price, int quantity) {
            super(brand, type, size, color, material, price, quantity);
        }

        @Override
        public Item copy() {
            Item result = new Item(getBrand(), getType(), getSize(), getColor(), getMaterial(),
                    getPrice(), getQuantity());
            copyIdentityTo(result);
            return result;
        }

        @Override
        public String getCategory() {
            return "Тест";
        }
    }

    /** Створює коректний об'єкт для тестів. */
    private Item createValid() {
        return new Item("Nike", "Футболка", Size.M, "чорний", "бавовна", 799.99, 5);
    }

    /** Коректні дані приймаються. */
    @Test
    void shouldCreateObjectWithValidData() {
        Item clothes = createValid();

        assertEquals(Size.M, clothes.getSize());
        assertEquals(799.99, clothes.getPrice());
        assertEquals(5, clothes.getQuantity());
    }

    /** Некоректне значення в сеттері викликає виняток. */
    @Test
    void shouldThrowExceptionWhenInvalidValueInSetter() {
        Item clothes = createValid();

        assertThrows(IllegalArgumentException.class, () -> clothes.setPrice(-1));
        assertThrows(IllegalArgumentException.class, () -> clothes.setQuantity(-5));
        assertThrows(IllegalArgumentException.class, () -> clothes.setSize(null));
        assertThrows(IllegalArgumentException.class, () -> clothes.setBrand("   "));
    }

    /** Некоректні параметри конструктора викликають виняток. */
    @Test
    void shouldThrowExceptionWhenInvalidConstructorData() {
        assertThrows(IllegalArgumentException.class,
                () -> new Item("", "Футболка", Size.M, "чорний", "бавовна", 799.99, 5));
        assertThrows(IllegalArgumentException.class,
                () -> new Item("Nike", null, Size.M, "чорний", "бавовна", 799.99, 5));
        assertThrows(IllegalArgumentException.class,
                () -> new Item("Nike", "Футболка", Size.M, "чорний", "бавовна", 0, 5));
        assertThrows(IllegalArgumentException.class,
                () -> new Item("Nike", "Футболка", Size.M, "чорний", "бавовна", 799.99, -1));
        assertThrows(IllegalArgumentException.class,
                () -> new Item("Nike", "Футболка", null, "чорний", "бавовна", 799.99, 5));
    }

    /** Рядок розбирається у розмір без урахування регістру й пробілів. */
    @Test
    void shouldParseSizeIgnoringCaseAndSpaces() {
        assertEquals(Size.M, Size.fromString(" m "));
        assertEquals(Size.XXL, Size.fromString("xxl"));
        assertThrows(IllegalArgumentException.class, () -> Size.fromString("XXXL"));
        assertThrows(IllegalArgumentException.class, () -> Size.fromString("  "));
    }

    /** Після невдалого виклику сеттера старе значення зберігається. */
    @Test
    void shouldKeepOldValueWhenSetterFails() {
        Item clothes = createValid();

        assertThrows(IllegalArgumentException.class, () -> clothes.setPrice(-10));

        assertEquals(799.99, clothes.getPrice());
    }

    /** Повідомлення винятку вказує на помилкове поле. */
    @Test
    void shouldReportFieldInExceptionMessage() {
        Item clothes = createValid();

        IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
                () -> clothes.setMaterial(""));

        assertTrue(e.getMessage().contains("Матеріал"));
    }
}