package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.function.UnaryOperator;

/** Консольне меню для роботи зі списком одягу. */
public class Main {

    private static final int CREATE_CLOTHES = 1;
    private static final int CREATE_PANTS = 2;
    private static final int CREATE_SHIRTS = 3;
    private static final int SHOW = 4;
    private static final int EXIT = 5;

    /**
     * Показує меню, доки користувач не обере завершення роботи.
     *
     * @param args аргументи командного рядка (не використовуються)
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        List<Clothes> wardrobe = new ArrayList<>();
        boolean running = true;

        while (running) {
            printMenu();
            switch (readMenuChoice(scanner)) {
                case CREATE_CLOTHES:
                    wardrobe.add(readClothes(scanner));
                    System.out.println("Об'єкт створено.");
                    break;
                case CREATE_PANTS:
                    wardrobe.add(readPants(scanner));
                    System.out.println("Штани створено.");
                    break;
                case CREATE_SHIRTS:
                    wardrobe.add(readShirts(scanner));
                    System.out.println("Сорочку створено.");
                    break;
                case SHOW:
                    printAll(wardrobe);
                    break;
                case EXIT:
                    running = false;
                    break;
                default:
                    System.out.println("Помилка: оберіть пункт від 1 до 5.");
                    break;
            }
        }

        scanner.close();
    }

    /** Виводить пункти меню. */
    private static void printMenu() {
        System.out.println();
        System.out.println("=== Меню ===");
        System.out.println("1. Створити одяг (базовий клас)");
        System.out.println("2. Створити штани (Pants)");
        System.out.println("3. Створити сорочку (Shirts)");
        System.out.println("4. Вивести інформацію про всі об'єкти");
        System.out.println("5. Завершити роботу");
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

    /** Виводить усі об'єкти через toString() або повідомлення, що список порожній. */
    private static void printAll(List<Clothes> wardrobe) {
        if (wardrobe.isEmpty()) {
            System.out.println("Список порожній.");
            return;
        }
        for (int i = 0; i < wardrobe.size(); i++) {
            Clothes item = wardrobe.get(i);
            System.out.println((i + 1) + ". [" + item.getCategory() + "] " + item);
        }
        System.out.println("Усього об'єктів: " + wardrobe.size());
    }

    /** Зчитує всі поля з клавіатури та створює об'єкт Clothes. */
    private static Clothes readClothes(Scanner scanner) {
        String brand = readField(scanner, "Бренд: ", v -> Clothes.checkText(v, "Бренд"));
        String type = readField(scanner, "Тип: ", v -> Clothes.checkText(v, "Тип"));
        Size size = readSize(scanner);
        String color = readField(scanner, "Колір: ", v -> Clothes.checkText(v, "Колір"));
        String material = readField(scanner, "Матеріал: ", v -> Clothes.checkText(v, "Матеріал"));
        double price = readPrice(scanner);
        int quantity = readQuantity(scanner);
        return new Clothes(brand, type, size, color, material, price, quantity);
    }

    /** Зчитує штани: спільні поля одягу, довжину та крій. */
    private static Pants readPants(Scanner scanner) {
        String brand = readField(scanner, "Бренд: ", v -> Clothes.checkText(v, "Бренд"));
        Size size = readSize(scanner);
        String color = readField(scanner, "Колір: ", v -> Clothes.checkText(v, "Колір"));
        String material = readField(scanner, "Матеріал: ", v -> Clothes.checkText(v, "Матеріал"));
        double price = readPrice(scanner);
        int quantity = readQuantity(scanner);
        int length = readInt(scanner, "Довжина, см", Pants.MIN_LENGTH, Pants.MAX_LENGTH);
        Fit fit = readEnum(scanner, "Крій", Fit.values());
        return new Pants(brand, size, color, material, price, quantity, length, fit);
    }

    /** Зчитує сорочку: спільні поля одягу, тип рукава та розмір коміра. */
    private static Shirts readShirts(Scanner scanner) {
        String brand = readField(scanner, "Бренд: ", v -> Clothes.checkText(v, "Бренд"));
        Size size = readSize(scanner);
        String color = readField(scanner, "Колір: ", v -> Clothes.checkText(v, "Колір"));
        String material = readField(scanner, "Матеріал: ", v -> Clothes.checkText(v, "Матеріал"));
        double price = readPrice(scanner);
        int quantity = readQuantity(scanner);
        Sleeve sleeve = readEnum(scanner, "Рукав", Sleeve.values());
        int neckSize = readInt(scanner, "Розмір коміра, см", Shirts.MIN_NECK, Shirts.MAX_NECK);
        return new Shirts(brand, size, color, material, price, quantity, sleeve, neckSize);
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

    /** Зчитує рядок і повторює запит, доки validator не прийме значення. */
    private static String readField(Scanner scanner, String prompt, UnaryOperator<String> validator) {
        while (true) {
            System.out.print(prompt);
            try {
                return validator.apply(scanner.nextLine());
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