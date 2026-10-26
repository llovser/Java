package org.example;

import java.util.Objects;

public class Clothes {

    private String brand;
    private String type;
    private String size;
    private String color;
    private double price;

    public Clothes(String brand, String type, String size, String color, double price) {
        this.brand = brand;
        this.type = type;
        this.size = size;
        this.color = color;
        this.price = price;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Clothes{brand='" + brand + "', type='" + type + "', size='" + size
                + "', color='" + color + "', price=" + price + "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Clothes other = (Clothes) o;
        return Double.compare(price, other.price) == 0
                && Objects.equals(brand, other.brand)
                && Objects.equals(type, other.type)
                && Objects.equals(size, other.size)
                && Objects.equals(color, other.color);
    }

    @Override
    public int hashCode() {
        return Objects.hash(brand, type, size, color, price);
    }

}