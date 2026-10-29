package org.example;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/** Читання та запис колекції одягу у текстовий файл: один об'єкт у рядку, поля через ';'. */
public class FileStorage {

    public static final String FILE_NAME = "input.txt";

    private static final char SEPARATOR = ';';
    private static final char ESCAPE = '\\';

    /** Перетворює об'єкт на рядок файлу: тип, спільні поля, потім поля класу. */
    public static String toLine(Clothes item) {
        StringBuilder line = new StringBuilder(item.getClass().getSimpleName());
        append(line, item.getBrand());
        append(line, item.getSize().name());
        append(line, item.getColor());
        append(line, item.getMaterial());
        append(line, String.valueOf(item.getPrice()));
        append(line, String.valueOf(item.getQuantity()));
        if (item instanceof Pants) {
            Pants pants = (Pants) item;
            append(line, String.valueOf(pants.getLength()));
            append(line, pants.getFit().name());
        } else if (item instanceof Shirts) {
            Shirts shirts = (Shirts) item;
            append(line, shirts.getSleeve().name());
            append(line, String.valueOf(shirts.getNeckSize()));
        } else if (item instanceof Jacket) {
            Jacket jacket = (Jacket) item;
            append(line, jacket.getSeason().name());
            append(line, String.valueOf(jacket.isHooded()));
        } else if (item instanceof Dress) {
            Dress dress = (Dress) item;
            append(line, String.valueOf(dress.getLength()));
            append(line, dress.getOccasion().name());
        }
        append(line, item.getUuid().toString());
        return line.toString();
    }

    /**
     * Створює об'єкт за рядком файлу.
     *
     * @throws IllegalArgumentException якщо тип невідомий, полів не вистачає або значення некоректне
     */
    public static Clothes parseLine(String line) {
        List<String> f = split(line);
        UUID uuid = null;
        int count = fieldCount(f.get(0));
        if (count > 0 && f.size() == count + 1) {
            uuid = Store.parseUuid(f.remove(count));
        }
        Clothes item = create(f);
        if (uuid != null) {
            item.restoreUuid(uuid);
        }
        return item;
    }

    /** Повертає кількість полів рядка без UUID для відомого типу або 0 для невідомого. */
    private static int fieldCount(String type) {
        switch (type) {
            case "Pants":
            case "Shirts":
            case "Jacket":
            case "Dress":
                return 9;
            default:
                return 0;
        }
    }

    /** Створює об'єкт за полями рядка (без UUID). */
    private static Clothes create(List<String> f) {
        String type = f.get(0);
        switch (type) {
            case "Pants":
                expect(f, 9);
                return new Pants(f.get(1), size(f.get(2)), f.get(3), f.get(4), number(f.get(5)),
                        integer(f.get(6)), integer(f.get(7)), parseEnum(Fit.class, f.get(8)));
            case "Shirts":
                expect(f, 9);
                return new Shirts(f.get(1), size(f.get(2)), f.get(3), f.get(4), number(f.get(5)),
                        integer(f.get(6)), parseEnum(Sleeve.class, f.get(7)), integer(f.get(8)));
            case "Jacket":
                expect(f, 9);
                return new Jacket(f.get(1), size(f.get(2)), f.get(3), f.get(4), number(f.get(5)),
                        integer(f.get(6)), parseEnum(Season.class, f.get(7)), bool(f.get(8)));
            case "Dress":
                expect(f, 9);
                return new Dress(f.get(1), size(f.get(2)), f.get(3), f.get(4), number(f.get(5)),
                        integer(f.get(6)), integer(f.get(7)), parseEnum(Occasion.class, f.get(8)));
            default:
                throw new IllegalArgumentException("Невідомий тип об'єкта: " + type);
        }
    }

    /**
     * Зчитує товари з файлу й додає їх у магазин через addNewClothes; некоректні рядки пропускає з повідомленням.
     * Якщо файлу немає або його не вдалося прочитати, магазин залишається без змін.
     */
    public static void load(String fileName, Store store) {
        if (!Files.exists(Paths.get(fileName))) {
            System.out.println("Файл " + fileName + " не знайдено, починаємо з порожнього магазину.");
            return;
        }
        List<String> lines;
        try {
            lines = Files.readAllLines(Paths.get(fileName), StandardCharsets.UTF_8);
        } catch (IOException e) {
            System.out.println("Помилка читання файлу: " + e.getMessage());
            return;
        }
        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            if (line.trim().isEmpty()) {
                continue;
            }
            try {
                Clothes item = parseLine(line);
                store.addNewClothes(item, item.getQuantity());
            } catch (IllegalArgumentException e) {
                System.out.println("Рядок " + (i + 1) + " пропущено: " + e.getMessage());
            }
        }
    }

    /** Записує всі товари магазину у файл (по одному в рядку); повертає false, якщо запис не вдався. */
    public static boolean save(String fileName, Store store) {
        List<String> lines = new ArrayList<>();
        for (Clothes item : store.getItems()) {
            lines.add(toLine(item));
        }
        try {
            Files.write(Paths.get(fileName), lines, StandardCharsets.UTF_8);
            return true;
        } catch (IOException e) {
            System.out.println("Помилка запису файлу: " + e.getMessage());
            return false;
        }
    }

    private static void append(StringBuilder line, String value) {
        line.append(SEPARATOR);
        for (int i = 0; i < value.length(); i++) {
            char c = value.charAt(i);
            if (c == SEPARATOR || c == ESCAPE) {
                line.append(ESCAPE);
            }
            line.append(c);
        }
    }

    /** Ділить рядок за ';' з урахуванням екранування символом '\'. */
    private static List<String> split(String line) {
        List<String> fields = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (c == ESCAPE && i + 1 < line.length()) {
                current.append(line.charAt(++i));
            } else if (c == SEPARATOR) {
                fields.add(current.toString());
                current.setLength(0);
            } else {
                current.append(c);
            }
        }
        fields.add(current.toString());
        return fields;
    }

    private static void expect(List<String> fields, int count) {
        if (fields.size() != count) {
            throw new IllegalArgumentException("очікувалось полів: " + count
                    + ", знайдено: " + fields.size());
        }
    }

    private static Size size(String value) {
        return Size.fromString(value);
    }

    private static double number(String value) {
        try {
            return Double.parseDouble(value.trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("некоректне число: " + value);
        }
    }

    private static int integer(String value) {
        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("некоректне ціле число: " + value);
        }
    }

    private static boolean bool(String value) {
        String text = value.trim();
        if (text.equals("true")) {
            return true;
        }
        if (text.equals("false")) {
            return false;
        }
        throw new IllegalArgumentException("очікувалось true або false: " + value);
    }

    private static <T extends Enum<T>> T parseEnum(Class<T> type, String value) {
        try {
            return Enum.valueOf(type, value.trim());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("некоректне значення " + type.getSimpleName() + ": " + value);
        }
    }
}