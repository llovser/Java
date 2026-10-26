package org.example;

import java.util.Scanner;

public class Main {

    private static final String[] BRANDS = {"Nike", "Adidas", "Zara", "H&M", "Levi's"};
    private static final String[] TYPES = {"Футболка", "Джинси", "Куртка", "Светр", "Сукня"};
    private static final String[] SIZES = {"S", "M", "L", "XL", "M"};
    private static final String[] COLORS = {"чорний", "синій", "білий", "червоний", "сірий"};
    private static final double[] PRICES = {799.99, 1499.0, 3299.5, 1199.0, 1899.99};

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Введіть кількість елементів: ");
        int count = scanner.nextInt();

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

    private static Clothes createSample(int index) {
        int n = index % BRANDS.length;
        double price = PRICES[n] + 100.0 * (index / BRANDS.length);
        return new Clothes(BRANDS[n], TYPES[n], SIZES[n], COLORS[n], price);
    }
}