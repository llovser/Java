package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.function.UnaryOperator;

/** Консольне меню для роботи зі списком одягу. */
public class Main {

    private static final int CREATE = 1;
    private static final int SHOW = 2;
    private static final int EXIT = 3;

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
                case CREATE:
                    wardrobe.add(readClothes(scanner));
                    System.out.println("Об'єкт створено.");
                    break;
                case SHOW:
                    printAll(wardrobe);
                    break;
                case EXIT:
                    running = false;
                    break;
                default:
                    System.out.println("Помилка: оберіть пункт від 1 до 3.");
                    break;
            }
        }

        scanner.close();
    }

    /** Виводить пункти меню. */
    private static void printMenu() {
        System.out.println();
        System.out.println("=== Меню ===");
        System.out.println("1. Створити новий об'єкт");
        System.out.println("2. Вивести інформацію про всі об'єкти");
        System.out.println("3. Завершити роботу");
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
            System.out.println((i + 1) + ". " + wardrobe.get(i));
        }
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