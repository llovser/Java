package org.example;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/** Тести класу {@link Store}. */
class StoreTest {

    private Pants jeans(int quantity) {
        return new Pants("Levi's", Size.M, "синій", "денім", 1500, quantity, 100, Fit.SLIM);
    }

    @Test
    void shouldAddNewItemWithGivenQuantity() {
        Store store = new Store("Тест");

        assertTrue(store.addNewClothes(jeans(1), 5));

        assertEquals(1, store.size());
        assertEquals(5, store.getItems().get(0).getQuantity());
    }

    @Test
    void shouldIncreaseQuantityOfExistingItem() {
        Store store = new Store("Тест");
        store.addNewClothes(jeans(1), 5);

        assertFalse(store.addNewClothes(jeans(1), 3));

        assertEquals(1, store.size());
        assertEquals(8, store.getItems().get(0).getQuantity());
        assertEquals(8, store.getTotalQuantity());
    }

    @Test
    void shouldTreatItemsWithDifferentFieldsAsDifferent() {
        Store store = new Store("Тест");
        store.addNewClothes(jeans(1), 1);
        store.addNewClothes(new Pants("Levi's", Size.M, "синій", "денім", 1500, 1, 100, Fit.LOOSE), 2);
        store.addNewClothes(new Clothes("Levi's", "Штани", Size.M, "синій", "денім", 1500, 1), 1);

        assertEquals(3, store.size());
        assertEquals(4, store.getTotalQuantity());
    }

    @Test
    void shouldThrowExceptionOnInvalidAdd() {
        Store store = new Store("Тест");

        assertThrows(IllegalArgumentException.class, () -> store.addNewClothes(null, 1));
        assertThrows(IllegalArgumentException.class, () -> store.addNewClothes(jeans(1), 0));
        assertThrows(IllegalArgumentException.class, () -> store.addNewClothes(jeans(1), -2));
        assertThrows(IllegalArgumentException.class, () -> new Store("  "));
        assertEquals(0, store.size());
    }

    @Test
    void shouldNotAllowChangingStoreThroughReturnedList() {
        Store store = new Store("Тест");
        store.addNewClothes(jeans(1), 1);

        store.getItems().clear();

        assertEquals(1, store.size());
    }
}