package org.example;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/** Тести спільної колекції об'єктів усієї ієрархії. */
class HierarchyTest {

    @Test
    void shouldStoreAllFiveTypesInOneList() {
        List<Clothes> list = new ArrayList<>();
        list.add(new Pants("Levi's", Size.M, "синій", "денім", 1500, 4, 100, Fit.SLIM));
        list.add(new Shirts("H&M", Size.S, "білий", "бавовна", 900, 7, Sleeve.LONG, 38));
        list.add(new Jacket("North Face", Size.L, "чорний", "поліестер", 6000, 2, Season.WINTER, true));
        list.add(new Dress("Mango", Size.M, "червоний", "віскоза", 2200, 3, 110, Occasion.EVENING));

        String[] expected = {"Штани", "Сорочка", "Куртка", "Сукня"};
        String[] names = {"Pants{", "Shirts{", "Jacket{", "Dress{"};

        assertEquals(4, list.size());
        for (int i = 0; i < list.size(); i++) {
            assertEquals(expected[i], list.get(i).getCategory());
            assertTrue(list.get(i).toString().startsWith(names[i]));
        }
    }

    @Test
    void shouldNotEqualObjectsOfDifferentClasses() {
        Clothes base = new Pants("A", Size.M, "c", "m", 1, 1, 100, Fit.SLIM);
        Jacket jacket = new Jacket("A", Size.M, "c", "m", 1, 1, Season.SUMMER, false);

        assertFalse(base.equals(jacket));
        assertFalse(jacket.equals(base));
    }
}