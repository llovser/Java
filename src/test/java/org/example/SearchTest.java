package org.example;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/** Тести пошуку в магазині. */
class SearchTest {

    private Store createStore() {
        Store store = new Store("Тест");
        store.addNewClothes(new Clothes("Zara", "Топ", Size.S, "сірий", "бавовна", 500, 1), 1);
        store.addNewClothes(new Pants("Levi's", Size.M, "синій", "денім", 1500, 4, 100, Fit.SLIM), 4);
        store.addNewClothes(new Shirts("H&M", Size.S, "білий", "бавовна", 900, 7, Sleeve.LONG, 38), 7);
        store.addNewClothes(new Jacket("North Face", Size.L, "чорний", "поліестер", 6000, 2, Season.WINTER, true), 2);
        store.addNewClothes(new Dress("Mango", Size.M, "червоний", "віскоза", 2200, 3, 110, Occasion.EVENING), 3);
        return store;
    }

    @Test
    void shouldFindByBrandPartIgnoringCase() {
        List<Clothes> found = createStore().findByBrand(" nORTH ");

        assertEquals(1, found.size());
        assertEquals("Куртка", found.get(0).getCategory());
    }

    @Test
    void shouldFindByCategoryIgnoringCase() {
        List<Clothes> found = createStore().findByCategory("сукня");

        assertEquals(1, found.size());
        assertTrue(found.get(0) instanceof Dress);
    }

    @Test
    void shouldFindSeveralObjectsBySize() {
        List<Clothes> found = createStore().findBySize(Size.S);

        assertEquals(2, found.size());
        assertEquals("Zara", found.get(0).getBrand());
        assertEquals("H&M", found.get(1).getBrand());
    }

    @Test
    void shouldFindByPriceRangeIncludingBounds() {
        assertEquals(3, createStore().findByPrice(900, 2200).size());
    }

    @Test
    void shouldReturnEmptyListWhenNothingMatches() {
        Store store = createStore();

        assertTrue(store.findByBrand("Adidas").isEmpty());
        assertTrue(store.findByCategory("Шапка").isEmpty());
        assertTrue(store.findBySize(Size.XXL).isEmpty());
        assertTrue(store.findByPrice(10000, 20000).isEmpty());
    }

    @Test
    void shouldNotChangeStoreDuringSearch() {
        Store store = createStore();
        List<Clothes> before = store.getItems();
        int total = store.getTotalQuantity();

        store.findByBrand("a");
        store.findByCategory("Штани");
        store.findBySize(Size.M);
        store.findByPrice(1, 100000);

        assertEquals(before, store.getItems());
        assertEquals(total, store.getTotalQuantity());
    }
}