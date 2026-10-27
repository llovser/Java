package org.example;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/** Тести класу {@link Wardrobe}. */
class WardrobeTest {

    /** Створює коректний одяг із заданою ціною та кількістю. */
    private Clothes clothes(double price, int quantity) {
        return new Clothes("Nike", "Футболка", Size.M, "чорний", "бавовна", price, quantity);
    }

    /** Новий гардероб порожній і має назву. */
    @Test
    void shouldStartEmpty() {
        Wardrobe wardrobe = new Wardrobe("Мій гардероб");

        assertTrue(wardrobe.isEmpty());
        assertEquals(0, wardrobe.size());
        assertEquals("Мій гардероб", wardrobe.getName());
    }

    /** Додані об'єкти зберігаються і повертаються за індексом. */
    @Test
    void shouldStoreAggregatedObjects() {
        Wardrobe wardrobe = new Wardrobe("Мій гардероб");
        Clothes first = clothes(100, 1);

        wardrobe.add(first);

        assertEquals(1, wardrobe.size());
        assertSame(first, wardrobe.get(0));
    }

    /** Загальна вартість дорівнює сумі ціна * кількість. */
    @Test
    void shouldCalculateTotalValue() {
        Wardrobe wardrobe = new Wardrobe("Мій гардероб");
        wardrobe.add(clothes(100, 2));
        wardrobe.add(clothes(50, 3));

        assertEquals(350.0, wardrobe.getTotalValue());
    }

    /** Некоректні дані викликають виняток. */
    @Test
    void shouldThrowExceptionOnInvalidData() {
        Wardrobe wardrobe = new Wardrobe("Мій гардероб");

        assertThrows(IllegalArgumentException.class, () -> new Wardrobe("  "));
        assertThrows(IllegalArgumentException.class, () -> wardrobe.add(null));
        assertThrows(IllegalArgumentException.class, () -> wardrobe.get(0));
        assertThrows(IllegalArgumentException.class, () -> wardrobe.get(-1));
    }
}