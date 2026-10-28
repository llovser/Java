package org.example;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

/** Тести DatabaseManager без реальної бази: з'єднання підміняється заглушкою. */
class DatabaseManagerTest {

    /** Запис викликів PreparedStatement: "метод:індекс:значення". */
    private final List<String> calls = new ArrayList<>();
    private String preparedSql;
    private boolean failOnExecute;

    private DatabaseManager createManager() {
        InvocationHandler statementHandler = (proxy, method, args) -> {
            if (method.getName().equals("executeUpdate") && failOnExecute) {
                throw new SQLException("test failure");
            }
            if (method.getName().startsWith("set")) {
                calls.add(method.getName() + ":" + args[0] + ":" + args[1]);
            }
            return method.getReturnType() == int.class ? Integer.valueOf(1) : null;
        };
        PreparedStatement statement = (PreparedStatement) Proxy.newProxyInstance(
                getClass().getClassLoader(), new Class<?>[]{PreparedStatement.class}, statementHandler);
        InvocationHandler connectionHandler = (proxy, method, args) -> {
            if (method.getName().equals("prepareStatement")) {
                preparedSql = (String) args[0];
                return statement;
            }
            return null;
        };
        Connection connection = (Connection) Proxy.newProxyInstance(
                getClass().getClassLoader(), new Class<?>[]{Connection.class}, connectionHandler);
        return new DatabaseManager(connection);
    }

    @Test
    void shouldInsertPantsWithDiscriminatorAndOwnFields() throws SQLException {
        DatabaseManager manager = createManager();

        manager.insert(new Pants("Levi's", Size.M, "синій", "денім", 1500, 4, 100, Fit.SLIM));

        assertTrue(preparedSql.contains("?"));
        assertFalse(preparedSql.contains("Levi"));
        assertTrue(calls.contains("setString:1:Pants"));
        assertTrue(calls.contains("setString:3:Штани"));
        assertTrue(calls.contains("setInt:9:100"));
        assertTrue(calls.contains("setString:10:SLIM"));
        assertTrue(calls.contains("setNull:11:12"));
    }

    @Test
    void shouldInsertJacketWithItsOwnFields() throws SQLException {
        DatabaseManager manager = createManager();

        manager.insert(new Jacket("North Face", Size.L, "чорний", "поліестер", 6000, 2, Season.WINTER, true));

        assertTrue(calls.contains("setString:1:Jacket"));
        assertTrue(calls.contains("setString:13:WINTER"));
        assertTrue(calls.contains("setBoolean:14:true"));
        assertTrue(calls.contains("setNull:9:4"));
    }

    @Test
    void shouldPassSqlExceptionToCaller() {
        DatabaseManager manager = createManager();
        failOnExecute = true;

        assertThrows(SQLException.class, () -> manager.insert(
                new Dress("Mango", Size.M, "червоний", "віскоза", 2200, 3, 110, Occasion.EVENING)));
    }

    @Test
    void shouldReadConfigFromFile() throws IOException {
        File file = File.createTempFile("dbconfig", ".properties");
        file.deleteOnExit();
        String text = "db.url=jdbc:postgresql://localhost:5432/shop\ndb.user=admin\ndb.password=secret\n";
        Files.write(file.toPath(), text.getBytes(StandardCharsets.UTF_8));

        Properties config = DatabaseManager.loadConfig(file.getPath());

        assertEquals("jdbc:postgresql://localhost:5432/shop", config.getProperty("db.url"));
        assertEquals("admin", config.getProperty("db.user"));
    }

    @Test
    void shouldRejectMissingOrIncompleteConfig() throws IOException {
        File file = File.createTempFile("dbconfig", ".properties");
        file.deleteOnExit();
        Files.write(file.toPath(), "db.url=jdbc:postgresql://localhost/x\n".getBytes(StandardCharsets.UTF_8));

        assertThrows(IOException.class, () -> DatabaseManager.loadConfig(file.getPath()));
        assertThrows(IOException.class, () -> DatabaseManager.loadConfig("no_such_config_12345.properties"));
    }
}