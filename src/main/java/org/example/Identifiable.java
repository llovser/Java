package org.example;

import java.util.UUID;

/** Контракт об'єкта, який має унікальний ідентифікатор. */
public interface Identifiable {

    /** Повертає UUID об'єкта. */
    UUID getUuid();
}