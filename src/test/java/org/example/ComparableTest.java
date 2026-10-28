package org.example;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/** Тести абстрактності базового класу та сортування за Comparable. */
class ComparableTest {

    private Pants pants(String brand) {
        return new Pants(brand, Size.M, "синій", "денім", 1500, 1, 100, Fit.SLIM);
    }

    private List<Clothes> sample() {
        List<Clothes> list = new ArrayList<>();
        list.add(pants("Zara"));
        list.add(new Jacket("North Face", Size.L, "чорний", "поліестер", 6000, 2, Season.WINTER, true));
        list.add(new Dress("Mango", Size.M, "червоний", "віскоза", 2200, 3, 110, Occasion.EVENING));
        list.add(new Shirts("H&M", Size.S, "білий", "бавовна", 900, 7, Sleeve.LONG, 38));
        return list;
    }

    @Test
    void shouldHaveAbstractComparableBaseClass() {
        assertTrue(java.lang.reflect.Modifier.isAbstract(Clothes.class.getModifiers()));
        assertTrue(Comparable.class.isAssignableFrom(Clothes.class));
    }

    @Test
    void shouldSortSeveralObjectsByBrand() {
        List<Clothes> list = sample();

        list.sort(null);

        assertEquals("H&M", list.get(0).getBrand());
        assertEquals("Mango", list.get(1).getBrand());
        assertEquals("North Face", list.get(2).getBrand());
        assertEquals("Zara", list.get(3).getBrand());
    }

    @Test
    void shouldSortEmptyAndSingleElementLists() {
        List<Clothes> empty = new ArrayList<>();
        List<Clothes> single = new ArrayList<>();
        single.add(pants("Zara"));

        empty.sort(null);
        single.sort(null);

        assertTrue(empty.isEmpty());
        assertEquals(1, single.size());
    }

    @Test
    void shouldIgnoreCaseOfBrandWhenSorting() {
        List<Clothes> list = new ArrayList<>();
        list.add(pants("zara"));
        list.add(pants("Adidas"));
        list.add(pants("Nike"));

        list.sort(null);

        assertEquals("Adidas", list.get(0).getBrand());
        assertEquals("Nike", list.get(1).getBrand());
        assertEquals("zara", list.get(2).getBrand());
    }

    @Test
    void shouldGiveConsistentResultForEqualBrands() {
        Pants cheap = new Pants("Nike", Size.M, "синій", "денім", 500, 1, 100, Fit.SLIM);
        Pants expensive = new Pants("Nike", Size.M, "синій", "денім", 900, 1, 100, Fit.SLIM);

        assertTrue(cheap.compareTo(expensive) < 0);
        assertTrue(expensive.compareTo(cheap) > 0);
        assertEquals(0, cheap.compareTo(new Pants("Nike", Size.M, "синій", "денім", 500, 5, 100, Fit.SLIM)));
    }

    @Test
    void shouldKeepOrderOfEqualElementsAndNotChangeSource() {
        Pants first = pants("Nike");
        Pants second = pants("Nike");
        second.setQuantity(9);
        List<Clothes> list = new ArrayList<>();
        list.add(second);
        list.add(first);
        list.add(pants("Adidas"));
        Store store = new Store("Тест");
        store.addNewClothes(pants("Zara"), 1);
        store.addNewClothes(pants("Adidas"), 1);

        list.sort(null);
        List<Clothes> copy = store.getItems();
        copy.sort(null);

        assertSame(second, list.get(1));
        assertSame(first, list.get(2));
        assertEquals("Zara", store.getItems().get(0).getBrand());
        assertEquals("Adidas", copy.get(0).getBrand());
    }
}