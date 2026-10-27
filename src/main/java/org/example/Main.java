package org.example;

import java.util.Scanner;
import java.util.function.UnaryOperator;

/** Консольне меню для роботи зі списком одягу. */
public class Main {

    private static final int CREATE = 1;
    private static final int SHOW = 2;
    private static final int COPY = 3;
    private static final int STATS = 4;
    private static final int EXIT = 5;

    /**
     * Показує меню, доки користувач не обере завершення роботи.
     *
     * @param args аргументи командного рядка (не використовуються)
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Wardrobe wardrobe = new Wardrobe("Мій гардероб");
        boolean running = true;
        printHeader();

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
                case COPY:
                    copyClothes(scanner, wardrobe);
                    break;
                case STATS:
                    printStatistics(wardrobe);
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

    private static void printHeader() {
        System.out.println("==========================================");
        System.out.println("Практична робота №6 (варіант 1)");
        System.out.println("Статичні члени, агрегація, enum");
        System.out.println("Виконав: Басанець Єгор");
        System.out.println("==========================================");
    }

    /** Виводить пункти меню. */
    private static void printMenu() {
        System.out.println();
        System.out.println("=== Меню ===");
        System.out.println("1. Створити новий об'єкт");
        System.out.println("2. Вивести інформацію про всі об'єкти");
        System.out.println("3. Створити копію об'єкта");
        System.out.println("4. Показати кількість створених об'єктів");
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
    private static void printAll(Wardrobe wardrobe) {
        if (wardrobe.isEmpty()) {
            System.out.println("Список порожній.");
            return;
        }
        System.out.println("Гардероб: " + wardrobe.getName());
        for (int i = 0; i < wardrobe.size(); i++) {
            System.out.println((i + 1) + ". " + wardrobe.get(i));
        }
    }

    /** Копіює вибраний об'єкт конструктором копіювання й додає копію до гардероба. */
    private static void copyClothes(Scanner scanner, Wardrobe wardrobe) {
        if (wardrobe.isEmpty()) {
            System.out.println("Список порожній, копіювати нічого.");
            return;
        }
        int number = readNumber(scanner, wardrobe.size());
        Clothes original = wardrobe.get(number - 1);
        Clothes copy = new Clothes(original);
        wardrobe.add(copy);
        System.out.println("Копію створено: " + copy);
        System.out.println("equals(): " + original.equals(copy)
                + ", той самий об'єкт у пам'яті (==): " + (original == copy));
    }

    /** Зчитує номер об'єкта від 1 до max, повторюючи запит для некоректного значення. */
    private static int readNumber(Scanner scanner, int max) {
        while (true) {
            System.out.print("Номер об'єкта (від 1 до " + max + "): ");
            try {
                int number = Integer.parseInt(scanner.nextLine().trim());
                if (number >= 1 && number <= max) {
                    return number;
                }
                System.out.println("Помилка: число має бути від 1 до " + max + ".");
            } catch (NumberFormatException e) {
                System.out.println("Помилка: потрібно ввести ціле число.");
            }
        }
    }

    /** Виводить значення статичного лічильника та вартість гардероба. */
    private static void printStatistics(Wardrobe wardrobe) {
        System.out.println("Створено об'єктів Clothes (статичний лічильник): " + Clothes.getCount());
        System.out.println("Об'єктів у гардеробі: " + wardrobe.size());
        System.out.println("Загальна вартість: " + wardrobe.getTotalValue());
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