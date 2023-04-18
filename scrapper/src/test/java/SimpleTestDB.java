import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertTrue;


public class SimpleTestDB extends IntegrationEnvironment {
    @Test
    public void simpleTest(){
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(
                    singletonPostgreSQLContainer.getJdbcUrl(),
                    singletonPostgreSQLContainer.getUsername(),
                    singletonPostgreSQLContainer.getPassword()
            );
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        ResultSet tables = null;
        try {
            tables = connection.getMetaData().getTables(null, null, "chat", null);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        try {
            assertTrue(tables.next(), "Таблица chat не найдена");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
