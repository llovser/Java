package org.example;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/** Тести компараторів для різних критеріїв сортування. */
class ComparatorTest {

    private List<Clothes> sample() {
        List<Clothes> list = new ArrayList<>();
        list.add(new Pants("Levi's", Size.M, "синій", "денім", 1500, 6, 100, Fit.SLIM));
        list.add(new Jacket("North Face", Size.XL, "чорний", "поліестер", 6000, 2, Season.WINTER, true));
        list.add(new Dress("Mango", Size.XS, "червоний", "віскоза", 2200, 3, 110, Occasion.EVENING));
        list.add(new Shirts("H&M", Size.S, "білий", "бавовна", 900, 7, Sleeve.LONG, 38));
        return list;
    }

    private String brands(List<Clothes> list) {
        StringBuilder result = new StringBuilder();
        for (Clothes item : list) {
            result.append(item.getBrand()).append(';');
        }
        return result.toString();
    }

    @Test
    void shouldSortByPriceAscending() {
        List<Clothes> list = sample();

        list.sort(Main.priceComparator());

        assertEquals("H&M;Levi's;Mango;North Face;", brands(list));
    }

    @Test
    void shouldSortByQuantityDescending() {
        List<Clothes> list = sample();

        list.sort(Main.quantityComparator());

        assertEquals("H&M;Levi's;Mango;North Face;", brands(list));
    }

    @Test
    void shouldSortBySizeInEnumOrder() {
        List<Clothes> list = sample();

        list.sort(Main.sizeComparator());

        assertEquals("Mango;H&M;Levi's;North Face;", brands(list));
    }

    @Test
    void shouldSortByBrand() {
        List<Clothes> list = sample();

        list.sort(Main.brandComparator());

        assertEquals("H&M;Levi's;Mango;North Face;", brands(list));
    }

    @Test
    void shouldHandleEmptyAndSingleElementLists() {
        Comparator<Clothes> comparator = Main.priceComparator();
        List<Clothes> empty = new ArrayList<>();
        List<Clothes> single = new ArrayList<>();
        single.add(sample().get(0));

        empty.sort(comparator);
        single.sort(comparator);

        assertTrue(empty.isEmpty());
        assertEquals(1, single.size());
    }

    @Test
    void shouldKeepOrderOfEqualElements() {
        Pants first = new Pants("A", Size.M, "c", "m", 100, 1, 100, Fit.SLIM);
        Pants second = new Pants("B", Size.M, "c", "m", 100, 2, 100, Fit.SLIM);
        List<Clothes> list = new ArrayList<>();
        list.add(second);
        list.add(first);

        list.sort(Main.priceComparator());

        assertSame(second, list.get(0));
        assertSame(first, list.get(1));
    }

    @Test
    void shouldNotChangeStoreWhenSortingItsCopy() {
        Store store = new Store("Тест");
        for (Clothes item : sample()) {
            store.addNewClothes(item, item.getQuantity());
        }

        List<Clothes> copy = store.getItems();
        copy.sort(Main.priceComparator());

        assertEquals("Levi's", store.getItems().get(0).getBrand());
        assertEquals("H&M", copy.get(0).getBrand());
    }

    @Test
    void shouldKeepOrderForEqualQuantitiesAndSizes() {
        Pants first = new Pants("A", Size.M, "c", "m", 100, 5, 100, Fit.SLIM);
        Pants second = new Pants("B", Size.M, "c", "m", 200, 5, 100, Fit.SLIM);
        List<Clothes> list = new ArrayList<>();
        list.add(second);
        list.add(first);

        list.sort(Main.quantityComparator());
        assertSame(second, list.get(0));

        list.sort(Main.sizeComparator());
        assertSame(second, list.get(0));
    }
}