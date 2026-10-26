package org.example;

import java.util.Scanner;

public class Main {

    private static final String[] BRANDS = {"Nike", "Adidas", "Zara", "H&M", "Levi's"};
    private static final String[] TYPES = {"Футболка", "Джинси", "Куртка", "Светр", "Сукня"};
    private static final String[] SIZES = {"S", "M", "L", "XL", "M"};
    private static final String[] COLORS = {"чорний", "синій", "білий", "червоний", "сірий"};
    private static final double[] PRICES = {799.99, 1499.0, 3299.5, 1199.0, 1899.99};
    private static final int MIN_COUNT = 1;
    private static final int MAX_COUNT = 100;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        int count = readCount(scanner);

        Clothes[] wardrobe = new Clothes[count];
        for (int i = 0; i < count; i++) {
            wardrobe[i] = createSample(i);
        }

        System.out.println();
        System.out.println("Створені об'єкти:");
        for (int i = 0; i < wardrobe.length; i++) {
            System.out.println((i + 1) + ". " + wardrobe[i]);
        }

        scanner.close();
    }

    private static int readCount(Scanner scanner) {
        while (true) {
            System.out.print("Введіть кількість елементів (від " + MIN_COUNT
                    + " до " + MAX_COUNT + "): ");

            if (!scanner.hasNextInt()) {
                System.out.println("Помилка: потрібно ввести ціле число.");
                scanner.next();
                continue;
            }

            int count = scanner.nextInt();
            if (count >= MIN_COUNT && count <= MAX_COUNT) {
                return count;
            }
            System.out.println("Помилка: число має бути від " + MIN_COUNT
                    + " до " + MAX_COUNT + ".");
        }
    }

    private static Clothes createSample(int index) {
        int n = index % BRANDS.length;
        double price = PRICES[n] + 100.0 * (index / BRANDS.length);
        return new Clothes(BRANDS[n], TYPES[n], SIZES[n], COLORS[n], price);
    }
}