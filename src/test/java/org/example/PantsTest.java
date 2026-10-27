package org.example;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/** Тести класу {@link Pants}. */
class PantsTest {

    /** Створює коректні штани. */
    private Pants createValid() {
        return new Pants("Levi's", Size.M, "синій", "денім", 1500, 4, 100, Fit.SLIM);
    }

    /** Штани успадковують поля базового класу й задають тип. */
    @Test
    void shouldInheritBaseFields() {
        Pants pants = createValid();

        assertTrue(pants instanceof Clothes);
        assertEquals("Levi's", pants.getBrand());
        assertEquals("Штани", pants.getType());
        assertEquals(100, pants.getLength());
        assertEquals(Fit.SLIM, pants.getFit());
    }

    /** Некоректні дані в конструкторі та сеттерах викликають виняток. */
    @Test
    void shouldThrowExceptionOnInvalidData() {
        Pants pants = createValid();

        assertThrows(IllegalArgumentException.class,
                () -> new Pants("Levi's", Size.M, "синій", "денім", 1500, 4, 10, Fit.SLIM));
        assertThrows(IllegalArgumentException.class,
                () -> new Pants("Levi's", Size.M, "синій", "денім", 1500, 4, 100, null));
        assertThrows(IllegalArgumentException.class,
                () -> new Pants("", Size.M, "синій", "денім", 1500, 4, 100, Fit.SLIM));
        assertThrows(IllegalArgumentException.class, () -> pants.setLength(200));
        assertThrows(IllegalArgumentException.class, () -> pants.setFit(null));
    }

    /** Через посилання на базовий тип викликаються перевизначені методи. */
    @Test
    void shouldBehavePolymorphically() {
        Clothes clothes = createValid();

        assertEquals("Штани", clothes.getCategory());
        assertTrue(clothes.toString().startsWith("Pants{"));
        assertTrue(clothes.toString().contains("length=100"));
    }

    /** equals враховує додаткові поля та клас об'єкта. */
    @Test
    void shouldCompareByAllFieldsAndClass() {
        Pants first = createValid();
        Pants same = createValid();
        Pants other = createValid();
        other.setFit(Fit.LOOSE);
        Clothes plain = new Clothes("Levi's", "Штани", Size.M, "синій", "денім", 1500, 4);

        assertEquals(first, same);
        assertEquals(first.hashCode(), same.hashCode());
        assertFalse(first.equals(other));
        assertFalse(first.equals(plain));
    }
}