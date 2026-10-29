package org.example;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/** Тести UUID, інтерфейсу Identifiable та пошуку за UUID. */
class UuidTest {

    private Pants pants(String brand) {
        return new Pants(brand, Size.M, "синій", "денім", 1500, 4, 100, Fit.SLIM);
    }

    @Test
    void shouldCreateUniqueStableUuidAutomatically() {
        Pants first = pants("Levi's");
        Pants second = pants("Levi's");

        assertNotNull(first.getUuid());
        assertNotEquals(first.getUuid(), second.getUuid());
        assertEquals(first.getUuid(), first.getUuid());
        assertEquals(first, second);
    }

    @Test
    void shouldImplementIdentifiableAndShowUuidInOutput() {
        Clothes item = pants("Levi's");

        assertTrue(item instanceof Identifiable);
        assertTrue(item.toString().contains(item.getUuid().toString()));
        assertTrue(item.toShortString().contains(item.getUuid().toString()));
        assertTrue(item.toString().startsWith("Pants{"));
    }

    @Test
    void shouldFindObjectByUuid() {
        Store store = new Store("Тест");
        Pants jeans = pants("Levi's");
        Jacket jacket = new Jacket("North Face", Size.L, "чорний", "поліестер", 6000, 2, Season.WINTER, true);
        store.addNewClothes(jeans, 4);
        store.addNewClothes(jacket, 2);

        assertSame(jacket, store.findByUuid(jacket.getUuid().toString()));
        assertSame(jeans, store.findByUuid("  " + jeans.getUuid() + " "));
    }

    @Test
    void shouldReturnNullWhenUuidNotFound() {
        Store store = new Store("Тест");
        store.addNewClothes(pants("Levi's"), 1);

        assertNull(store.findByUuid(UUID.randomUUID().toString()));
        assertNull(new Store("Порожній").findByUuid(UUID.randomUUID().toString()));
    }

    @Test
    void shouldRejectInvalidUuidFormat() {
        Store store = new Store("Тест");

        assertThrows(IllegalArgumentException.class, () -> store.findByUuid("abc"));
        assertThrows(IllegalArgumentException.class, () -> store.findByUuid(""));
        assertThrows(IllegalArgumentException.class, () -> store.findByUuid(null));
        assertThrows(IllegalArgumentException.class, () -> store.findByUuid("12345678-1234-1234-1234-12345678901z"));
    }

    @Test
    void shouldKeepUuidWhenSavedAndLoaded() throws IOException {
        File file = File.createTempFile("uuidtest", ".txt");
        file.deleteOnExit();
        Store store = new Store("Тест");
        Pants jeans = pants("Levi's");
        store.addNewClothes(jeans, 4);

        FileStorage.save(file.getPath(), store);
        Store loaded = new Store("Інший");
        FileStorage.load(file.getPath(), loaded);

        assertEquals(1, loaded.size());
        assertEquals(jeans.getUuid(), loaded.getItems().get(0).getUuid());
    }

    @Test
    void shouldGenerateUuidForLineWithoutIt() {
        Clothes item = FileStorage.parseLine("Pants;Levi's;M;синій;денім;1500.0;4;100;SLIM");

        assertNotNull(item.getUuid());
    }

    @Test
    void shouldRejectLineWithBrokenUuid() {
        assertThrows(IllegalArgumentException.class,
                () -> FileStorage.parseLine("Pants;Levi's;M;синій;денім;1500.0;4;100;SLIM;not-a-uuid"));
    }

    @Test
    void shouldRejectNullWhenRestoringUuid() {
        assertThrows(IllegalArgumentException.class, () -> pants("Levi's").restoreUuid(null));
    }
}