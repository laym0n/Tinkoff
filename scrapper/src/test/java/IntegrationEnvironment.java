import liquibase.Contexts;
import liquibase.LabelExpression;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.DatabaseException;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;
import liquibase.resource.DirectoryResourceAccessor;
import org.testcontainers.containers.PostgreSQLContainer;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public abstract class IntegrationEnvironment {
    static final PostgreSQLContainer singletonPostgreSQLContainer;

    static {
        singletonPostgreSQLContainer = new PostgreSQLContainer();
        singletonPostgreSQLContainer
                .withUsername("user")
                .withPassword("pass")
                .withDatabaseName("scrapper");
        singletonPostgreSQLContainer.start();
        Connection connection = null;
        System.out.println("sdsafdsafsadf");
        try {
            connection = DriverManager.getConnection(
                    singletonPostgreSQLContainer.getJdbcUrl(),
                    singletonPostgreSQLContainer.getUsername(),
                    singletonPostgreSQLContainer.getPassword()
            );
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        Database database = null;
        try {
            database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(connection));
        } catch (DatabaseException e) {
            throw new RuntimeException(e);
        }
        DirectoryResourceAccessor directoryResourceAccessor = null;
        try {
            directoryResourceAccessor = new DirectoryResourceAccessor(new File("").toPath().resolve("src").resolve("main").resolve("resources")
                    .resolve("migrations"));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        Liquibase liquibase = new liquibase.Liquibase(
                "master.xml",
                directoryResourceAccessor, database);
        try {
            liquibase.update(new Contexts(), new LabelExpression());
        } catch (LiquibaseException e) {
            throw new RuntimeException(e);
        }
        System.out.println("214313541324");
    }
}

