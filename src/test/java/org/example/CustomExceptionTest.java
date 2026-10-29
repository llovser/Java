package org.example;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class CustomExceptionTest {

    private Pants createPants() {
        return new Pants("Levi's", Size.M, "синій", "джинс", 1500, 3, 100, Fit.SLIM);
    }

    @Test
    void shouldThrowObjectNotFoundExceptionWhenDeletingNonExistingObject() {
        Store store = new Store("Тест");
        Pants pants = createPants();
        assertThrows(ObjectNotFoundException.class, () -> store.delete(pants));
    }

    @Test
    void shouldThrowObjectNotFoundExceptionWhenUpdatingNonExistingObject() {
        Store store = new Store("Тест");
        Pants pants = createPants();
        assertThrows(ObjectNotFoundException.class, () -> store.update(pants, pants.copy()));
    }

    @Test
    void shouldThrowInvalidFieldValueExceptionForNegativePrice() {
        Pants pants = createPants();
        assertThrows(InvalidFieldValueException.class, () -> pants.setPrice(-5));
    }

    @Test
    void shouldThrowInvalidFieldValueExceptionForEmptyBrand() {
        Pants pants = createPants();
        assertThrows(InvalidFieldValueException.class, () -> pants.setBrand("  "));
    }

    @Test
    void shouldThrowInvalidFieldValueExceptionForWrongPantsLength() {
        assertThrows(InvalidFieldValueException.class, () -> Pants.checkLength(10));
    }

    @Test
    void shouldThrowInvalidFieldValueExceptionForNotPositiveQuantityInStore() {
        Store store = new Store("Тест");
        Pants pants = createPants();
        assertThrows(InvalidFieldValueException.class, () -> store.addNewClothes(pants, 0));
    }

    @Test
    void shouldThrowDuplicateObjectExceptionWhenUpdateCreatesDuplicate() {
        Store store = new Store("Тест");
        Pants first = createPants();
        Pants second = createPants();
        second.setColor("чорний");
        store.addNewClothes(first, 1);
        store.addNewClothes(second, 1);
        Pants changed = second.copy();
        changed.setColor("синій");
        assertThrows(DuplicateObjectException.class, () -> store.update(second, changed));
    }

    @Test
    void shouldKeepMessageAndStoreUnchangedAfterException() {
        Store store = new Store("Тест");
        Pants pants = createPants();
        store.addNewClothes(pants, 2);
        ObjectNotFoundException e = assertThrows(ObjectNotFoundException.class,
                () -> store.delete(new Pants("X", Size.S, "білий", "льон", 100, 1, 90, Fit.SLIM)));
        assertTrue(e.getMessage().contains("не знайдено"));
        assertEquals(1, store.size());
    }
}