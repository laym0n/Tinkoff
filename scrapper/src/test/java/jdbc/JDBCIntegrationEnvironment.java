package jdbc;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.PlatformTransactionManager;
import ru.tinkoff.edu.java.scrapper.configuration.JDBCAccessConfiguration;

import javax.sql.DataSource;

@SpringBootTest(classes = {JDBCIntegrationEnvironment.JDBCConfigClass.class, JDBCAccessConfiguration.class})
public class JDBCIntegrationEnvironment extends IntegrationEnvironment{
    @Configuration
    static class JDBCConfigClass{
        @Bean
        public DataSource dataSource(){
            return new DriverManagerDataSource(singletonPostgreSQLContainer.getJdbcUrl(),
                    singletonPostgreSQLContainer.getUsername(),
                    singletonPostgreSQLContainer.getPassword());
        }
        @Bean
        public PlatformTransactionManager platformTransactionManager(DataSource dataSource){
            return new DataSourceTransactionManager(dataSource);
        }
    }
    @DynamicPropertySource
    static void jdbcProperties(DynamicPropertyRegistry registry){
        registry.add("app.database-access-type", ()->"jdbc");
    }
}
