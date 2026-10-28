package org.example;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/** Тести класу {@link Dress}. */
class DressTest {

    private Dress createValid() {
        return new Dress("Mango", Size.M, "червоний", "віскоза", 2200, 3, 110, Occasion.EVENING);
    }

    @Test
    void shouldInheritBaseFields() {
        Dress dress = createValid();

        assertTrue(dress instanceof Clothes);
        assertEquals("Сукня", dress.getType());
        assertEquals(110, dress.getLength());
        assertEquals(Occasion.EVENING, dress.getOccasion());
    }

    @Test
    void shouldThrowExceptionOnInvalidData() {
        Dress dress = createValid();

        assertThrows(IllegalArgumentException.class,
                () -> new Dress("Mango", Size.M, "червоний", "віскоза", 2200, 3, 10, Occasion.EVENING));
        assertThrows(IllegalArgumentException.class,
                () -> new Dress("Mango", Size.M, "червоний", "віскоза", 2200, 3, 110, null));
        assertThrows(IllegalArgumentException.class, () -> dress.setLength(500));
        assertThrows(IllegalArgumentException.class, () -> dress.setOccasion(null));
    }
}