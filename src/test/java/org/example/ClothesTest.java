package org.example;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/** Тести перевірки коректності даних у класі {@link Clothes}. */
class ClothesTest {

    /** Створює коректний об'єкт для тестів. */
    private Clothes createValid() {
        return new Clothes("Nike", "Футболка", Size.M, "чорний", "бавовна", 799.99, 5);
    }

    /** Коректні дані приймаються. */
    @Test
    void shouldCreateObjectWithValidData() {
        Clothes clothes = createValid();

        assertEquals(Size.M, clothes.getSize());
        assertEquals(799.99, clothes.getPrice());
        assertEquals(5, clothes.getQuantity());
    }

    /** Некоректне значення в сеттері викликає виняток. */
    @Test
    void shouldThrowExceptionWhenInvalidValueInSetter() {
        Clothes clothes = createValid();

        assertThrows(IllegalArgumentException.class, () -> clothes.setPrice(-1));
        assertThrows(IllegalArgumentException.class, () -> clothes.setQuantity(-5));
        assertThrows(IllegalArgumentException.class, () -> clothes.setSize(null));
        assertThrows(IllegalArgumentException.class, () -> clothes.setBrand("   "));
    }

    /** Некоректні параметри конструктора викликають виняток. */
    @Test
    void shouldThrowExceptionWhenInvalidConstructorData() {
        assertThrows(IllegalArgumentException.class,
                () -> new Clothes("", "Футболка", Size.M, "чорний", "бавовна", 799.99, 5));
        assertThrows(IllegalArgumentException.class,
                () -> new Clothes("Nike", null, Size.M, "чорний", "бавовна", 799.99, 5));
        assertThrows(IllegalArgumentException.class,
                () -> new Clothes("Nike", "Футболка", Size.M, "чорний", "бавовна", 0, 5));
        assertThrows(IllegalArgumentException.class,
                () -> new Clothes("Nike", "Футболка", Size.M, "чорний", "бавовна", 799.99, -1));
        assertThrows(IllegalArgumentException.class,
                () -> new Clothes("Nike", "Футболка", null, "чорний", "бавовна", 799.99, 5));
    }

    /** Рядок розбирається у розмір без урахування регістру й пробілів. */
    @Test
    void shouldParseSizeIgnoringCaseAndSpaces() {
        assertEquals(Size.M, Size.fromString(" m "));
        assertEquals(Size.XXL, Size.fromString("xxl"));
        assertThrows(IllegalArgumentException.class, () -> Size.fromString("XXXL"));
        assertThrows(IllegalArgumentException.class, () -> Size.fromString("  "));
    }

    /** Після невдалого виклику сеттера старе значення зберігається. */
    @Test
    void shouldKeepOldValueWhenSetterFails() {
        Clothes clothes = createValid();

        assertThrows(IllegalArgumentException.class, () -> clothes.setPrice(-10));

        assertEquals(799.99, clothes.getPrice());
    }

    /** Повідомлення винятку вказує на помилкове поле. */
    @Test
    void shouldReportFieldInExceptionMessage() {
        Clothes clothes = createValid();

        IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
                () -> clothes.setMaterial(""));

        assertTrue(e.getMessage().contains("Матеріал"));
    }

    /** Лічильник зростає лише після успішного створення об'єкта. */
    @Test
    void shouldCountOnlySuccessfullyCreatedObjects() {
        int before = Clothes.getCount();

        createValid();
        assertEquals(before + 1, Clothes.getCount());

        assertThrows(IllegalArgumentException.class,
                () -> new Clothes("", "Футболка", Size.M, "чорний", "бавовна", 799.99, 5));
        assertEquals(before + 1, Clothes.getCount());
    }

    /** Конструктор копіювання створює рівний, але незалежний об'єкт і збільшує лічильник. */
    @Test
    void shouldCreateIndependentCopy() {
        Clothes original = createValid();
        int before = Clothes.getCount();

        Clothes copy = new Clothes(original);
        copy.setPrice(1.5);

        assertNotSame(original, copy);
        assertEquals(799.99, original.getPrice());
        assertEquals(before + 1, Clothes.getCount());
        assertEquals(original.getBrand(), copy.getBrand());
        assertEquals(original, new Clothes(original));
    }

    /** Копіювання null викликає виняток. */
    @Test
    void shouldThrowExceptionWhenCopyingNull() {
        assertThrows(IllegalArgumentException.class, () -> new Clothes((Clothes) null));
    }

}