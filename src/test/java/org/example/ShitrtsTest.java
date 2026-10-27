package org.example;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/** Тести класу {@link Shirts} та спільної колекції об'єктів різних типів. */
class ShirtsTest {

    /** Створює коректну сорочку. */
    private Shirts createValid() {
        return new Shirts("H&M", Size.S, "білий", "бавовна", 900, 7, Sleeve.LONG, 38);
    }

    /** Сорочка успадковує поля базового класу й задає тип. */
    @Test
    void shouldInheritBaseFields() {
        Shirts shirt = createValid();

        assertTrue(shirt instanceof Clothes);
        assertEquals("Сорочка", shirt.getType());
        assertEquals(Sleeve.LONG, shirt.getSleeve());
        assertEquals(38, shirt.getNeckSize());
    }

    /** Некоректні дані в конструкторі та сеттерах викликають виняток. */
    @Test
    void shouldThrowExceptionOnInvalidData() {
        Shirts shirt = createValid();

        assertThrows(IllegalArgumentException.class,
                () -> new Shirts("H&M", Size.S, "білий", "бавовна", 900, 7, null, 38));
        assertThrows(IllegalArgumentException.class,
                () -> new Shirts("H&M", Size.S, "білий", "бавовна", 900, 7, Sleeve.LONG, 10));
        assertThrows(IllegalArgumentException.class,
                () -> new Shirts("H&M", Size.S, "білий", "бавовна", -5, 7, Sleeve.LONG, 38));
        assertThrows(IllegalArgumentException.class, () -> shirt.setNeckSize(60));
        assertThrows(IllegalArgumentException.class, () -> shirt.setSleeve(null));
    }

    /** Об'єкти різних типів зберігаються в одному списку й обробляються як Clothes. */
    @Test
    void shouldStoreDifferentTypesInOneList() {
        List<Clothes> list = new ArrayList<>();
        list.add(new Clothes("Zara", "Куртка", Size.L, "сірий", "вовна", 3000, 1));
        list.add(new Pants("Levi's", Size.M, "синій", "денім", 1500, 4, 100, Fit.SLIM));
        list.add(createValid());

        assertEquals(3, list.size());
        assertEquals("Одяг", list.get(0).getCategory());
        assertEquals("Штани", list.get(1).getCategory());
        assertEquals("Сорочка", list.get(2).getCategory());
        assertTrue(list.get(2).toString().startsWith("Shirts{"));
    }
}