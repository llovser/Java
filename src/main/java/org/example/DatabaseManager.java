package org.example;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Properties;

/** Робота з базою даних: підключення за конфігураційним файлом і збереження товарів (INSERT). */
public class DatabaseManager implements AutoCloseable {

    private static final String INSERT_SQL = "INSERT INTO clothes (type, brand, clothes_type, size, color, "
            + "material, price, quantity, length, fit, sleeve, neck_size, season, hooded, occasion) "
            + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    private final Connection connection;

    /** Створює менеджер для вже відкритого підключення. */
    public DatabaseManager(Connection connection) {
        this.connection = connection;
    }

    /**
     * Читає параметри підключення з файлу й відкриває з'єднання.
     *
     * @param configPath шлях до файлу з db.url, db.user, db.password
     * @throws IOException  якщо файл не вдалося прочитати
     * @throws SQLException якщо підключитися не вдалося
     */
    public static DatabaseManager connect(String configPath) throws IOException, SQLException {
        Properties config = loadConfig(configPath);
        Connection connection = DriverManager.getConnection(
                config.getProperty("db.url"), config.getProperty("db.user"), config.getProperty("db.password"));
        return new DatabaseManager(connection);
    }

    /**
     * Читає конфігураційний файл і перевіряє наявність усіх параметрів.
     *
     * @throws IOException якщо файл не прочитано або в ньому бракує параметра
     */
    public static Properties loadConfig(String configPath) throws IOException {
        Properties config = new Properties();
        try (InputStream in = new FileInputStream(configPath)) {
            config.load(in);
        }
        String[] keys = {"db.url", "db.user", "db.password"};
        for (String key : keys) {
            if (config.getProperty(key) == null) {
                throw new IOException("У файлі " + configPath + " немає параметра " + key);
            }
        }
        return config;
    }

    /**
     * Додає товар у таблицю clothes; поле type визначається класом об'єкта.
     *
     * @throws SQLException якщо запит не вдалося виконати
     */
    public void insert(Clothes item) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(INSERT_SQL)) {
            statement.setString(1, item.getClass().getSimpleName());
            statement.setString(2, item.getBrand());
            statement.setString(3, item.getType());
            statement.setString(4, item.getSize().name());
            statement.setString(5, item.getColor());
            statement.setString(6, item.getMaterial());
            statement.setDouble(7, item.getPrice());
            statement.setInt(8, item.getQuantity());
            setSpecificFields(statement, item);
            statement.executeUpdate();
        }
    }

    /** Заповнює поля, які є лише в окремих підкласів; решта отримує NULL. */
    private void setSpecificFields(PreparedStatement statement, Clothes item) throws SQLException {
        statement.setNull(9, Types.INTEGER);
        statement.setNull(10, Types.VARCHAR);
        statement.setNull(11, Types.VARCHAR);
        statement.setNull(12, Types.INTEGER);
        statement.setNull(13, Types.VARCHAR);
        statement.setNull(14, Types.BOOLEAN);
        statement.setNull(15, Types.VARCHAR);
        if (item instanceof Pants) {
            Pants pants = (Pants) item;
            statement.setInt(9, pants.getLength());
            statement.setString(10, pants.getFit().name());
        } else if (item instanceof Shirts) {
            Shirts shirts = (Shirts) item;
            statement.setString(11, shirts.getSleeve().name());
            statement.setInt(12, shirts.getNeckSize());
        } else if (item instanceof Jacket) {
            Jacket jacket = (Jacket) item;
            statement.setString(13, jacket.getSeason().name());
            statement.setBoolean(14, jacket.isHooded());
        } else if (item instanceof Dress) {
            Dress dress = (Dress) item;
            statement.setInt(9, dress.getLength());
            statement.setString(15, dress.getOccasion().name());
        }
    }

    /** Закриває підключення до бази даних. */
    @Override
    public void close() throws SQLException {
        connection.close();
    }
}