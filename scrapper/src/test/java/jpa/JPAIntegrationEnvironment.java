package jpa;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.PlatformTransactionManager;
import ru.tinkoff.edu.java.scrapper.configuration.JPAAccessConfiguration;
import javax.sql.DataSource;

@SpringBootTest(classes = {JPAIntegrationEnvironment.JPAConfigClass.class, JPAAccessConfiguration.class})
public class JPAIntegrationEnvironment extends IntegrationEnvironment {
    @Configuration
    static class JPAConfigClass {
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
        registry.add("app.database-access-type", ()->"jpa");
    }
}
