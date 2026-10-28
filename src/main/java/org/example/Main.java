package org.example;

import java.util.List;
import java.util.Scanner;

/** Консольне меню для роботи зі списком одягу. */
public class Main {

    private static final int SEARCH = 1;
    private static final int CREATE = 2;
    private static final int SHOW = 3;
    private static final int SORTED = 4;
    private static final int EXIT = 5;

    private static final int BY_BRAND = 1;
    private static final int BY_CATEGORY = 2;
    private static final int BY_SIZE = 3;
    private static final int BY_PRICE = 4;

    private static final int BACK = 0;
    private static final int TYPE_PANTS = 1;
    private static final int TYPE_SHIRTS = 2;
    private static final int TYPE_JACKET = 3;
    private static final int TYPE_DRESS = 4;

    /**
     * Показує меню, доки користувач не обере завершення роботи.
     *
     * @param args аргументи командного рядка (не використовуються)
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Store store = new Store("Магазин одягу");
        FileStorage.load(FileStorage.FILE_NAME, store);
        boolean running = true;

        while (running) {
            printMenu();
            switch (readMenuChoice(scanner)) {
                case SEARCH:
                    searchObjects(scanner, store);
                    break;
                case CREATE:
                    createObject(scanner, store);
                    break;
                case SHOW:
                    printAll(store);
                    break;
                case SORTED:
                    printSorted(store);
                    break;
                case EXIT:
                    running = false;
                    break;
                default:
                    System.out.println("Помилка: оберіть пункт від 1 до 5.");
                    break;
            }
        }

        if (FileStorage.save(FileStorage.FILE_NAME, store)) {
            System.out.println("Дані збережено у файл " + FileStorage.FILE_NAME + ".");
        }
        scanner.close();
    }

    /** Виводить пункти меню. */
    private static void printMenu() {
        System.out.println();
        System.out.println("=== Меню ===");
        System.out.println("1. Пошук об'єкта");
        System.out.println("2. Створити новий об'єкт");
        System.out.println("3. Вивести інформацію про всі об'єкти");
        System.out.println("4. Вивести відсортовану інформацію про всі об'єкти");
        System.out.println("5. Завершити роботу");
    }

    /** Дає обрати критерій пошуку, виконує його й виводить результат (0 - назад). */
    private static void searchObjects(Scanner scanner, Store store) {
        System.out.println();
        System.out.println("--- Пошук об'єкта ---");
        System.out.println("1. За брендом");
        System.out.println("2. За категорією");
        System.out.println("3. За розміром");
        System.out.println("4. За діапазоном ціни");
        System.out.println("0. Назад до головного меню");
        List<Clothes> found;
        switch (readInt(scanner, "Оберіть критерій", BACK, BY_PRICE)) {
            case BY_BRAND:
                found = store.findByBrand(readText(scanner, "Бренд (або його частина): ", "Бренд"));
                break;
            case BY_CATEGORY:
                found = store.findByCategory(readText(scanner,
                        "Категорія (Штани, Сорочка, Куртка, Сукня): ", "Категорія"));
                break;
            case BY_SIZE:
                found = store.findBySize(readSize(scanner));
                break;
            case BY_PRICE:
                found = readAndFindByPrice(scanner, store);
                break;
            default:
                System.out.println("Повернення до головного меню.");
                return;
        }
        printSearchResult(found);
    }

    /** Зчитує межі ціни (мінімальна не більша за максимальну) і виконує пошук. */
    private static List<Clothes> readAndFindByPrice(Scanner scanner, Store store) {
        while (true) {
            System.out.println("Мінімальна ціна:");
            double min = readPrice(scanner);
            System.out.println("Максимальна ціна:");
            double max = readPrice(scanner);
            if (min <= max) {
                return store.findByPrice(min, max);
            }
            System.out.println("Помилка: мінімальна ціна не може бути більшою за максимальну.");
        }
    }

    /** Виводить знайдені об'єкти або повідомлення, що нічого не знайдено. */
    private static void printSearchResult(List<Clothes> found) {
        if (found.isEmpty()) {
            System.out.println("За вказаним критерієм нічого не знайдено.");
            return;
        }
        System.out.println("Знайдено об'єктів: " + found.size());
        for (int i = 0; i < found.size(); i++) {
            Clothes item = found.get(i);
            System.out.println((i + 1) + ". [" + item.getCategory() + "] " + item);
        }
    }

    /** Дає обрати тип об'єкта, зчитує його дані й додає товар у магазин (0 - назад). */
    private static void createObject(Scanner scanner, Store store) {
        System.out.println();
        System.out.println("--- Тип об'єкта ---");
        System.out.println("1. Штани (Pants)");
        System.out.println("2. Сорочка (Shirts)");
        System.out.println("3. Куртка (Jacket)");
        System.out.println("4. Сукня (Dress)");
        System.out.println("0. Назад до головного меню");
        Clothes created;
        switch (readInt(scanner, "Оберіть тип", BACK, TYPE_DRESS)) {
            case TYPE_PANTS:
                created = readPants(scanner);
                break;
            case TYPE_SHIRTS:
                created = readShirts(scanner);
                break;
            case TYPE_JACKET:
                created = readJacket(scanner);
                break;
            case TYPE_DRESS:
                created = readDress(scanner);
                break;
            default:
                System.out.println("Повернення до головного меню.");
                return;
        }
        addToStore(store, created);
    }

    /** Додає товар у магазин і повідомляє, чи створено нову позицію, чи збільшено кількість. */
    private static void addToStore(Store store, Clothes created) {
        try {
            if (store.addNewClothes(created, created.getQuantity())) {
                System.out.println("Новий товар додано до магазину.");
            } else {
                System.out.println("Такий товар уже є в магазині: кількість збільшено.");
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Помилка: " + e.getMessage());
        }
    }

    /** Зчитує пункт меню; для нечислового вводу повертає -1. */
    private static int readMenuChoice(Scanner scanner) {
        System.out.print("Ваш вибір: ");
        try {
            return Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    /** Виводить усі товари магазину через базовий тип (поліморфізм) або повідомлення, що він порожній. */
    private static void printAll(Store store) {
        printItems(store, store.getItems());
    }

    /** Виводить товари магазину, відсортовані за природним порядком (Comparable), не змінюючи магазин. */
    private static void printSorted(Store store) {
        List<Clothes> items = store.getItems();
        items.sort(null);
        printItems(store, items);
    }

    /** Виводить список товарів із підсумком або повідомлення, що магазин порожній. */
    private static void printItems(Store store, List<Clothes> items) {
        if (items.isEmpty()) {
            System.out.println("Магазин порожній.");
            return;
        }
        System.out.println("Магазин: " + store.getName());
        for (int i = 0; i < items.size(); i++) {
            Clothes item = items.get(i);
            System.out.println((i + 1) + ". [" + item.getCategory() + "] " + item);
        }
        System.out.println("Позицій: " + store.size() + ", одиниць товару: " + store.getTotalQuantity());
    }

    /** Зчитує штани: спільні поля одягу, довжину та крій. */
    private static Pants readPants(Scanner scanner) {
        String brand = readText(scanner, "Бренд: ", "Бренд");
        Size size = readSize(scanner);
        String color = readText(scanner, "Колір: ", "Колір");
        String material = readText(scanner, "Матеріал: ", "Матеріал");
        double price = readPrice(scanner);
        int quantity = readQuantity(scanner);
        int length = readInt(scanner, "Довжина, см", Pants.MIN_LENGTH, Pants.MAX_LENGTH);
        Fit fit = readEnum(scanner, "Крій", Fit.values());
        return new Pants(brand, size, color, material, price, quantity, length, fit);
    }

    /** Зчитує сорочку: спільні поля одягу, тип рукава та розмір коміра. */
    private static Shirts readShirts(Scanner scanner) {
        String brand = readText(scanner, "Бренд: ", "Бренд");
        Size size = readSize(scanner);
        String color = readText(scanner, "Колір: ", "Колір");
        String material = readText(scanner, "Матеріал: ", "Матеріал");
        double price = readPrice(scanner);
        int quantity = readQuantity(scanner);
        Sleeve sleeve = readEnum(scanner, "Рукав", Sleeve.values());
        int neckSize = readInt(scanner, "Розмір коміра, см", Shirts.MIN_NECK, Shirts.MAX_NECK);
        return new Shirts(brand, size, color, material, price, quantity, sleeve, neckSize);
    }

    /** Зчитує куртку: спільні поля одягу, сезон і наявність капюшона. */
    private static Jacket readJacket(Scanner scanner) {
        String brand = readText(scanner, "Бренд: ", "Бренд");
        Size size = readSize(scanner);
        String color = readText(scanner, "Колір: ", "Колір");
        String material = readText(scanner, "Матеріал: ", "Матеріал");
        double price = readPrice(scanner);
        int quantity = readQuantity(scanner);
        Season season = readEnum(scanner, "Сезон", Season.values());
        boolean hooded = readYesNo(scanner, "З капюшоном?");
        return new Jacket(brand, size, color, material, price, quantity, season, hooded);
    }

    /** Зчитує сукню: спільні поля одягу, довжину та призначення. */
    private static Dress readDress(Scanner scanner) {
        String brand = readText(scanner, "Бренд: ", "Бренд");
        Size size = readSize(scanner);
        String color = readText(scanner, "Колір: ", "Колір");
        String material = readText(scanner, "Матеріал: ", "Матеріал");
        double price = readPrice(scanner);
        int quantity = readQuantity(scanner);
        int length = readInt(scanner, "Довжина, см", Dress.MIN_LENGTH, Dress.MAX_LENGTH);
        Occasion occasion = readEnum(scanner, "Призначення", Occasion.values());
        return new Dress(brand, size, color, material, price, quantity, length, occasion);
    }

    /** Зчитує відповідь так/ні, повторюючи запит для іншого вводу. */
    private static boolean readYesNo(Scanner scanner, String question) {
        while (true) {
            System.out.print(question + " (так/ні): ");
            String answer = scanner.nextLine().trim().toLowerCase();
            if (answer.equals("так") || answer.equals("т") || answer.equals("y")) {
                return true;
            }
            if (answer.equals("ні") || answer.equals("н") || answer.equals("n")) {
                return false;
            }
            System.out.println("Помилка: введіть \"так\" або \"ні\".");
        }
    }

    /** Зчитує ціле число від min до max, повторюючи запит для некоректного значення. */
    private static int readInt(Scanner scanner, String name, int min, int max) {
        while (true) {
            System.out.print(name + " (від " + min + " до " + max + "): ");
            try {
                int value = Integer.parseInt(scanner.nextLine().trim());
                if (value >= min && value <= max) {
                    return value;
                }
                System.out.println("Помилка: число має бути від " + min + " до " + max + ".");
            } catch (NumberFormatException e) {
                System.out.println("Помилка: потрібно ввести ціле число.");
            }
        }
    }

    /** Показує значення перерахування та зчитує вибір за номером. */
    private static <T extends Enum<T>> T readEnum(Scanner scanner, String name, T[] values) {
        System.out.println(name + ":");
        for (int i = 0; i < values.length; i++) {
            System.out.println("  " + (i + 1) + ". " + values[i]);
        }
        return values[readInt(scanner, "Номер", 1, values.length) - 1];
    }

    /** Зчитує непорожній рядок, повторюючи запит для порожнього значення. */
    private static String readText(Scanner scanner, String prompt, String fieldName) {
        while (true) {
            System.out.print(prompt);
            try {
                return Clothes.checkText(scanner.nextLine(), fieldName);
            } catch (IllegalArgumentException e) {
                System.out.println("Помилка: " + e.getMessage());
            }
        }
    }

    /** Зчитує розмір, повторюючи запит для порожнього або невідомого значення. */
    private static Size readSize(Scanner scanner) {
        while (true) {
            System.out.print("Розмір (XS, S, M, L, XL, XXL): ");
            try {
                return Size.fromString(scanner.nextLine());
            } catch (IllegalArgumentException e) {
                System.out.println("Помилка: " + e.getMessage());
            }
        }
    }

    /** Зчитує ціну, повторюючи запит для нечислового або некоректного значення. */
    private static double readPrice(Scanner scanner) {
        while (true) {
            System.out.print("Ціна: ");
            try {
                String line = scanner.nextLine().trim().replace(',', '.');
                return Clothes.checkPrice(Double.parseDouble(line));
            } catch (NumberFormatException e) {
                System.out.println("Помилка: потрібно ввести число.");
            } catch (IllegalArgumentException e) {
                System.out.println("Помилка: " + e.getMessage());
            }
        }
    }

    /** Зчитує кількість, повторюючи запит для нечислового або некоректного значення. */
    private static int readQuantity(Scanner scanner) {
        while (true) {
            System.out.print("Кількість: ");
            try {
                return Clothes.checkQuantity(Integer.parseInt(scanner.nextLine().trim()));
            } catch (NumberFormatException e) {
                System.out.println("Помилка: потрібно ввести ціле число.");
            } catch (IllegalArgumentException e) {
                System.out.println("Помилка: " + e.getMessage());
            }
        }
    }
}