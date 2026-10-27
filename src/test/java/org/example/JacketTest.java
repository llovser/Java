package org.example;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/** Тести класу {@link Jacket}. */
class JacketTest {

    private Jacket createValid() {
        return new Jacket("North Face", Size.L, "чорний", "поліестер", 6000, 2, Season.WINTER, true);
    }

    @Test
    void shouldInheritBaseFields() {
        Jacket jacket = createValid();

        assertTrue(jacket instanceof Clothes);
        assertEquals("Куртка", jacket.getType());
        assertEquals(Season.WINTER, jacket.getSeason());
        assertTrue(jacket.isHooded());
    }

    @Test
    void shouldThrowExceptionOnInvalidData() {
        Jacket jacket = createValid();

        assertThrows(IllegalArgumentException.class,
                () -> new Jacket("North Face", Size.L, "чорний", "поліестер", 6000, 2, null, true));
        assertThrows(IllegalArgumentException.class,
                () -> new Jacket("North Face", Size.L, "чорний", "поліестер", 0, 2, Season.WINTER, true));
        assertThrows(IllegalArgumentException.class, () -> jacket.setSeason(null));
    }

    @Test
    void shouldCompareByAllFieldsAndPrintOwnType() {
        Jacket first = createValid();
        Jacket other = createValid();
        other.setHooded(false);

        assertEquals(first, createValid());
        assertFalse(first.equals(other));
        assertTrue(first.toString().startsWith("Jacket{"));
        assertTrue(first.toString().contains("hooded=true"));
    }
}