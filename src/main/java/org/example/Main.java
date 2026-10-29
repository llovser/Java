package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Comparator;

/** Консольне меню для роботи зі списком одягу. */
public class Main {

    private static final int SEARCH = 1;
    private static final int CREATE = 2;
    private static final int UPDATE = 3;
    private static final int DELETE = 4;
    private static final int SHOW = 5;
    private static final int SORTED = 6;
    private static final int EXIT = 7;

    private static final int BY_BRAND = 1;
    private static final int BY_CATEGORY = 2;
    private static final int BY_SIZE = 3;
    private static final int BY_PRICE = 4;
    private static final int BY_UUID = 5;

    private static final int BACK = 0;
    private static final int TYPE_PANTS = 1;
    private static final int TYPE_SHIRTS = 2;
    private static final int TYPE_JACKET = 3;
    private static final int TYPE_DRESS = 4;

    private static final int SORT_BY_PRICE = 1;
    private static final int SORT_BY_QUANTITY = 2;
    private static final int SORT_BY_SIZE = 3;
    private static final int SORT_BY_BRAND = 4;

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
                case UPDATE:
                    modifyObject(scanner, store);
                    break;
                case DELETE:
                    deleteObject(scanner, store);
                    break;
                case SHOW:
                    printAll(store);
                    break;
                case SORTED:
                    sortObjects(scanner, store);
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
        System.out.println("3. Модифікувати об'єкт");
        System.out.println("4. Видалити об'єкт");
        System.out.println("5. Вивести інформацію про всі об'єкти");
        System.out.println("6. Вивести відсортовану інформацію про всі об'єкти");
        System.out.println("7. Завершити роботу");
    }

    /** Дає обрати критерій пошуку, виконує його й виводить результат (0 - назад). */
    private static void searchObjects(Scanner scanner, Store store) {
        System.out.println();
        System.out.println("--- Пошук об'єкта ---");
        System.out.println("1. За брендом");
        System.out.println("2. За категорією");
        System.out.println("3. За розміром");
        System.out.println("4. За діапазоном ціни");
        System.out.println("5. За UUID");
        System.out.println("0. Назад до головного меню");
        List<Clothes> found;
        switch (readInt(scanner, "Оберіть критерій", BACK, BY_UUID)) {
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
            case BY_UUID:
                found = readAndFindByUuid(scanner, store);
                break;
            default:
                System.out.println("Повернення до головного меню.");
                return;
        }
        if (found != null) {
            printSearchResult(found);
        }
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

    /** Зчитує UUID і шукає за ним товар; для некоректного формату повідомляє про помилку й повертає null. */
    private static List<Clothes> readAndFindByUuid(Scanner scanner, Store store) {
        List<Clothes> result = new ArrayList<>();
        System.out.print("UUID: ");
        try {
            Clothes item = store.findByUuid(scanner.nextLine());
            if (item != null) {
                result.add(item);
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Помилка: " + e.getMessage());
            return null;
        }
        return result;
    }

    /** Виводить знайдені об'єкти або повідомлення, що нічого не знайдено. */
    private static void printSearchResult(List<Clothes> found) {
        if (found.isEmpty()) {
            System.out.println("Не знайдено: за вказаним критерієм об'єктів немає.");
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

    /** Дає обрати товар зі списку, змінити один його атрибут і замінити товар у магазині (0 - назад). */
    private static void modifyObject(Scanner scanner, Store store) {
        Clothes existing = chooseObject(scanner, store, "модифікації");
        if (existing == null) {
            return;
        }
        Clothes modified = existing.copy();
        try {
            if (!editAttribute(scanner, modified)) {
                return;
            }
            store.update(existing, modified);
            System.out.println("Об'єкт змінено: " + modified);
        } catch (ObjectNotFoundException | IllegalArgumentException e) {
            System.out.println("Помилка: " + e.getMessage());
        }
    }

    /** Дає обрати товар зі списку й видаляє його після підтвердження (0 - назад). */
    private static void deleteObject(Scanner scanner, Store store) {
        Clothes existing = chooseObject(scanner, store, "видалення");
        if (existing == null) {
            return;
        }
        if (!readYesNo(scanner, "Видалити \"" + existing.getBrand() + "\"?")) {
            System.out.println("Видалення скасовано.");
            return;
        }
        try {
            store.delete(existing);
            System.out.println("Об'єкт видалено.");
        } catch (ObjectNotFoundException | IllegalArgumentException e) {
            System.out.println("Помилка: " + e.getMessage());
        }
    }

    /** Виводить нумерований список і повертає обраний товар або null (порожній магазин, 0 - назад). */
    private static Clothes chooseObject(Scanner scanner, Store store, String action) {
        List<Clothes> items = store.getItems();
        if (items.isEmpty()) {
            System.out.println("Магазин порожній.");
            return null;
        }
        printItems(store, items);
        int number = readInt(scanner, "Номер об'єкта для " + action + " (0 - назад)", BACK, items.size());
        return number == BACK ? null : items.get(number - 1);
    }

    /** Зчитує атрибут і нове значення та застосовує їх до копії; false, якщо обрано "назад". */
    private static boolean editAttribute(Scanner scanner, Clothes item) {
        System.out.println("Оберіть атрибут:");
        System.out.println("1. Бренд\n2. Розмір\n3. Колір\n4. Матеріал\n5. Ціна\n6. Кількість");
        int max = 6;
        if (item instanceof Pants) {
            System.out.println("7. Довжина\n8. Крій");
            max = 8;
        } else if (item instanceof Shirts) {
            System.out.println("7. Рукав\n8. Розмір коміра");
            max = 8;
        } else if (item instanceof Jacket) {
            System.out.println("7. Сезон\n8. Капюшон");
            max = 8;
        } else if (item instanceof Dress) {
            System.out.println("7. Довжина\n8. Призначення");
            max = 8;
        }
        int choice = readInt(scanner, "Атрибут (0 - назад)", BACK, max);
        switch (choice) {
            case BACK:
                return false;
            case 1:
                item.setBrand(readText(scanner, "Новий бренд: ", "Бренд"));
                break;
            case 2:
                item.setSize(readSize(scanner));
                break;
            case 3:
                item.setColor(readText(scanner, "Новий колір: ", "Колір"));
                break;
            case 4:
                item.setMaterial(readText(scanner, "Новий матеріал: ", "Матеріал"));
                break;
            case 5:
                item.setPrice(readPrice(scanner));
                break;
            case 6:
                item.setQuantity(readQuantity(scanner));
                break;
            default:
                editSpecificAttribute(scanner, item, choice);
                break;
        }
        return true;
    }

    /** Змінює атрибут, специфічний для конкретного типу одягу (пункти 7 і 8). */
    private static void editSpecificAttribute(Scanner scanner, Clothes item, int choice) {
        if (item instanceof Pants) {
            Pants pants = (Pants) item;
            if (choice == 7) {
                pants.setLength(readInt(scanner, "Довжина, см", Pants.MIN_LENGTH, Pants.MAX_LENGTH));
            } else {
                pants.setFit(readEnum(scanner, "Крій", Fit.values()));
            }
        } else if (item instanceof Shirts) {
            Shirts shirt = (Shirts) item;
            if (choice == 7) {
                shirt.setSleeve(readEnum(scanner, "Рукав", Sleeve.values()));
            } else {
                shirt.setNeckSize(readInt(scanner, "Розмір коміра, см", Shirts.MIN_NECK, Shirts.MAX_NECK));
            }
        } else if (item instanceof Jacket) {
            Jacket jacket = (Jacket) item;
            if (choice == 7) {
                jacket.setSeason(readEnum(scanner, "Сезон", Season.values()));
            } else {
                jacket.setHooded(readYesNo(scanner, "З капюшоном?"));
            }
        } else if (item instanceof Dress) {
            Dress dress = (Dress) item;
            if (choice == 7) {
                dress.setLength(readInt(scanner, "Довжина, см", Dress.MIN_LENGTH, Dress.MAX_LENGTH));
            } else {
                dress.setOccasion(readEnum(scanner, "Призначення", Occasion.values()));
            }
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

    /** Дає обрати критерій сортування, сортує копію списку магазину й виводить результат (0 - назад). */
    private static void sortObjects(Scanner scanner, Store store) {
        System.out.println();
        System.out.println("--- Оберіть критерій сортування ---");
        System.out.println("1. За ціною (від меншої до більшої)");
        System.out.println("2. За кількістю (від більшої до меншої)");
        System.out.println("3. За розміром (від XS до XXL)");
        System.out.println("4. За брендом (за алфавітом)");
        System.out.println("0. Повернутися в головне меню");
        Comparator<Clothes> comparator;
        String title;
        switch (readInt(scanner, "Оберіть критерій", BACK, SORT_BY_BRAND)) {
            case SORT_BY_PRICE:
                comparator = priceComparator();
                title = "за ціною";
                break;
            case SORT_BY_QUANTITY:
                comparator = quantityComparator();
                title = "за кількістю";
                break;
            case SORT_BY_SIZE:
                comparator = sizeComparator();
                title = "за розміром";
                break;
            case SORT_BY_BRAND:
                comparator = brandComparator();
                title = "за брендом";
                break;
            default:
                System.out.println("Повернення до головного меню.");
                return;
        }
        List<Clothes> items = store.getItems();
        items.sort(comparator);
        if (!items.isEmpty()) {
            System.out.println("Відсортовано " + title + ":");
        }
        printItems(store, items);
    }

    /** Повертає компаратор за ціною (від меншої до більшої). */
    static Comparator<Clothes> priceComparator() {
        return (o1, o2) -> Double.compare(o1.getPrice(), o2.getPrice());
    }

    /** Повертає компаратор за кількістю (від більшої до меншої). */
    static Comparator<Clothes> quantityComparator() {
        return (o1, o2) -> Integer.compare(o2.getQuantity(), o1.getQuantity());
    }

    /** Повертає компаратор за розміром (у порядку XS, S, M, L, XL, XXL). */
    static Comparator<Clothes> sizeComparator() {
        return (o1, o2) -> o1.getSize().compareTo(o2.getSize());
    }

    /** Повертає компаратор за брендом (природний порядок Comparable). */
    static Comparator<Clothes> brandComparator() {
        return (o1, o2) -> o1.compareTo(o2);
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