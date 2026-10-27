package org.example;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/** Тести читання та запису колекції у файл. */
class FileStorageTest {

    private List<Clothes> sample() {
        List<Clothes> list = new ArrayList<>();
        list.add(new Clothes("Zara", "Топ", Size.S, "сірий", "бавовна", 500.5, 1));
        list.add(new Pants("Levi's", Size.M, "синій", "денім", 1500, 4, 100, Fit.SLIM));
        list.add(new Shirts("H&M", Size.S, "білий", "бавовна", 900, 7, Sleeve.LONG, 38));
        list.add(new Jacket("North Face", Size.L, "чорний", "поліестер", 6000, 2, Season.WINTER, true));
        list.add(new Dress("Mango", Size.M, "червоний", "віскоза", 2200, 3, 110, Occasion.EVENING));
        return list;
    }

    @Test
    void shouldRestoreEveryTypeFromItsLine() {
        for (Clothes item : sample()) {
            Clothes restored = FileStorage.parseLine(FileStorage.toLine(item));

            assertEquals(item, restored);
            assertEquals(item.getClass(), restored.getClass());
        }
    }

    @Test
    void shouldKeepSeparatorAndBackslashInText() {
        Clothes item = new Clothes("A;B\\C", "Топ", Size.M, "c", "m", 1, 1);

        assertEquals(item, FileStorage.parseLine(FileStorage.toLine(item)));
    }

    @Test
    void shouldRejectInvalidLines() {
        assertThrows(IllegalArgumentException.class, () -> FileStorage.parseLine("Hat;Nike;M"));
        assertThrows(IllegalArgumentException.class, () -> FileStorage.parseLine("Pants;Levi's;M;синій;денім;1500;4;100"));
        assertThrows(IllegalArgumentException.class,
                () -> FileStorage.parseLine("Pants;Levi's;M;синій;денім;abc;4;100;SLIM"));
        assertThrows(IllegalArgumentException.class,
                () -> FileStorage.parseLine("Pants;Levi's;M;синій;денім;1500;4;100;TIGHT"));
        assertThrows(IllegalArgumentException.class,
                () -> FileStorage.parseLine("Dress;Mango;M;червоний;віскоза;2200;3;5;EVENING"));
        assertThrows(IllegalArgumentException.class,
                () -> FileStorage.parseLine("Jacket;North Face;L;чорний;поліестер;6000;2;WINTER;maybe"));
    }

    @Test
    void shouldSaveAndLoadWholeCollection() throws IOException {
        File file = File.createTempFile("clothes", ".txt");
        file.deleteOnExit();
        List<Clothes> list = sample();

        assertTrue(FileStorage.save(file.getPath(), list));

        assertEquals(list, FileStorage.load(file.getPath()));
    }

    @Test
    void shouldSkipBadLinesAndLoadTheRest() throws IOException {
        File file = File.createTempFile("clothes", ".txt");
        file.deleteOnExit();
        String text = "Pants;Levi's;M;синій;денім;1500.0;4;100;SLIM\n"
                + "сміття\n"
                + "\n"
                + "Dress;Mango;M;червоний;віскоза;2200.0;3;110;EVENING\n";
        Files.write(file.toPath(), text.getBytes(StandardCharsets.UTF_8));

        List<Clothes> loaded = FileStorage.load(file.getPath());

        assertEquals(2, loaded.size());
        assertEquals("Штани", loaded.get(0).getCategory());
        assertEquals("Сукня", loaded.get(1).getCategory());
    }

    @Test
    void shouldReturnEmptyListWhenFileMissing() {
        assertTrue(FileStorage.load("no_such_file_12345.txt").isEmpty());
    }
}