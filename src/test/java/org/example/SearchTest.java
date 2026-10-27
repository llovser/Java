package org.example;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/** Тести пошуку в колекції. */
class SearchTest {

    private List<Clothes> sample() {
        List<Clothes> list = new ArrayList<>();
        list.add(new Clothes("Zara", "Топ", Size.S, "сірий", "бавовна", 500, 1));
        list.add(new Pants("Levi's", Size.M, "синій", "денім", 1500, 4, 100, Fit.SLIM));
        list.add(new Shirts("H&M", Size.S, "білий", "бавовна", 900, 7, Sleeve.LONG, 38));
        list.add(new Jacket("North Face", Size.L, "чорний", "поліестер", 6000, 2, Season.WINTER, true));
        list.add(new Dress("Mango", Size.M, "червоний", "віскоза", 2200, 3, 110, Occasion.EVENING));
        return list;
    }

    @Test
    void shouldFindByBrandPartIgnoringCase() {
        List<Clothes> found = Main.findByBrand(sample(), " nORTH ");

        assertEquals(1, found.size());
        assertEquals("Куртка", found.get(0).getCategory());
    }

    @Test
    void shouldFindByCategoryIgnoringCase() {
        List<Clothes> found = Main.findByCategory(sample(), "сукня");

        assertEquals(1, found.size());
        assertTrue(found.get(0) instanceof Dress);
    }

    @Test
    void shouldFindSeveralObjectsBySize() {
        List<Clothes> found = Main.findBySize(sample(), Size.S);

        assertEquals(2, found.size());
        assertEquals("Zara", found.get(0).getBrand());
        assertEquals("H&M", found.get(1).getBrand());
    }

    @Test
    void shouldFindByPriceRangeIncludingBounds() {
        List<Clothes> found = Main.findByPrice(sample(), 900, 2200);

        assertEquals(3, found.size());
    }

    @Test
    void shouldReturnEmptyListWhenNothingMatches() {
        assertTrue(Main.findByBrand(sample(), "Adidas").isEmpty());
        assertTrue(Main.findByCategory(sample(), "Шапка").isEmpty());
        assertTrue(Main.findBySize(sample(), Size.XXL).isEmpty());
        assertTrue(Main.findByPrice(sample(), 10000, 20000).isEmpty());
    }

    @Test
    void shouldNotChangeCollectionDuringSearch() {
        List<Clothes> list = sample();
        List<Clothes> copy = new ArrayList<>(list);

        Main.findByBrand(list, "a");
        Main.findByCategory(list, "Штани");
        Main.findBySize(list, Size.M);
        Main.findByPrice(list, 1, 100000);

        assertEquals(copy, list);
    }
}