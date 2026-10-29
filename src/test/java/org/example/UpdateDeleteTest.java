package org.example;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.UUID;
import org.junit.jupiter.api.Test;

class UpdateDeleteTest {

    private final Store store = new Store("Тест");
    private final Pants pants = new Pants("Levi's", Size.M, "синій", "джинс", 1500, 3, 100, Fit.SLIM);
    private final Shirts shirt = new Shirts("Zara", Size.L, "білий", "бавовна", 900, 2, Sleeve.LONG, 40);

    UpdateDeleteTest() {
        store.addNewClothes(pants, 3);
        store.addNewClothes(shirt, 2);
    }

    @Test
    void updateReplacesObjectAndKeepsUuid() {
        Pants changed = pants.copy();
        changed.setColor("чорний");
        UUID uuid = pants.getUuid();
        assertTrue(store.update(pants, changed));
        assertEquals(2, store.size());
        assertEquals("чорний", ((Pants) store.getItems().get(0)).getColor());
        assertEquals(uuid, store.getItems().get(0).getUuid());
    }

    @Test
    void updateOfQuantityOnly() {
        Pants changed = pants.copy();
        changed.setQuantity(10);
        assertTrue(store.update(pants, changed));
        assertEquals(12, store.getTotalQuantity());
    }

    @Test
    void updateMissingReturnsFalse() {
        Pants other = new Pants("Other", Size.S, "червоний", "льон", 500, 1, 90, Fit.SLIM);
        assertFalse(store.update(other, other.copy()));
        assertEquals(2, store.size());
    }

    @Test
    void updateToDuplicateIsRejected() {
        Shirts changed = shirt.copy();
        changed.setBrand("Zara2");
        store.addNewClothes(changed.copy(), 1);
        Shirts duplicate = shirt.copy();
        duplicate.setBrand("Zara2");
        assertThrows(IllegalArgumentException.class, () -> store.update(shirt, duplicate));
    }

    @Test
    void updateWithNullThrows() {
        assertThrows(IllegalArgumentException.class, () -> store.update(null, pants));
        assertThrows(IllegalArgumentException.class, () -> store.update(pants, null));
    }

    @Test
    void deleteRemovesObject() {
        assertTrue(store.delete(pants));
        assertEquals(1, store.size());
        assertFalse(store.delete(pants));
    }

    @Test
    void deleteFromEmptyStoreReturnsFalse() {
        assertFalse(new Store("Порожній").delete(pants));
        assertFalse(store.delete(null));
    }

    @Test
    void copyIsIndependentAndEqual() {
        Pants copy = pants.copy();
        assertNotSame(pants, copy);
        assertEquals(pants, copy);
        assertEquals(pants.getUuid(), copy.getUuid());
        copy.setColor("зелений");
        assertEquals("синій", pants.getColor());
    }
}