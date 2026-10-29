package org.example;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/** Тести створення об'єктів зі значень форми (без JavaFX). */
class ClothesFactoryTest {

    private Map<String, String> fields() {
        Map<String, String> fields = new HashMap<>();
        fields.put("brand", "Levi's");
        fields.put("size", "M");
        fields.put("color", "синій");
        fields.put("material", "денім");
        fields.put("price", "1500,50");
        fields.put("quantity", "4");
        fields.put("number", "100");
        fields.put("choice", "SLIM");
        fields.put("hooded", "false");
        return fields;
    }

    @Test
    void shouldCreatePantsFromFields() {
        Clothes item = ClothesFactory.create(ClothesFactory.PANTS, fields());

        assertTrue(item instanceof Pants);
        assertEquals(1500.5, item.getPrice());
        assertEquals(Fit.SLIM, ((Pants) item).getFit());
        assertNotNull(item.getUuid());
    }

    @Test
    void shouldCreateEveryCategory() {
        Map<String, String> fields = fields();
        fields.put("choice", "SHORT");
        fields.put("number", "40");
        assertTrue(ClothesFactory.create(ClothesFactory.SHIRTS, fields) instanceof Shirts);

        fields.put("choice", "WINTER");
        fields.put("hooded", "true");
        Jacket jacket = (Jacket) ClothesFactory.create(ClothesFactory.JACKET, fields);
        assertTrue(jacket.isHooded());

        fields.put("choice", "EVENING");
        fields.put("number", "110");
        assertTrue(ClothesFactory.create(ClothesFactory.DRESS, fields) instanceof Dress);
    }

    @Test
    void shouldRejectInvalidValues() {
        Map<String, String> fields = fields();
        fields.put("price", "abc");
        assertThrows(IllegalArgumentException.class, () -> ClothesFactory.create(ClothesFactory.PANTS, fields));

        Map<String, String> noBrand = fields();
        noBrand.put("brand", "  ");
        assertThrows(IllegalArgumentException.class, () -> ClothesFactory.create(ClothesFactory.PANTS, noBrand));

        Map<String, String> badLength = fields();
        badLength.put("number", "5");
        assertThrows(IllegalArgumentException.class, () -> ClothesFactory.create(ClothesFactory.PANTS, badLength));

        Map<String, String> badChoice = fields();
        badChoice.put("choice", "TIGHT");
        assertThrows(IllegalArgumentException.class, () -> ClothesFactory.create(ClothesFactory.PANTS, badChoice));

        assertThrows(IllegalArgumentException.class, () -> ClothesFactory.create("Шапка", fields()));
        assertThrows(IllegalArgumentException.class, () -> ClothesFactory.create(ClothesFactory.PANTS, new HashMap<>()));
    }
}