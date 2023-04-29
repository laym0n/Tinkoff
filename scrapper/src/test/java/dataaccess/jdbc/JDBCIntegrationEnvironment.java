package dataaccess.jdbc;

import dataaccess.IntegrationEnvironment;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;
import ru.tinkoff.edu.java.scrapper.configuration.JDBCAccessConfiguration;

import javax.sql.DataSource;

@SpringBootTest(classes = {JDBCIntegrationEnvironment.JDBCConfigClass.class, JDBCAccessConfiguration.class})
public class JDBCIntegrationEnvironment extends IntegrationEnvironment {
    @Configuration
    @ComponentScan(basePackages = {"ru.tinkoff.edu.java.scrapper.dataaccess.impl.jdbc"})
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
        @Bean
        public TransactionTemplate transactionTemplate(PlatformTransactionManager platformTransactionManager){
            return new TransactionTemplate(platformTransactionManager);
        }
    }
    @DynamicPropertySource
    static void jdbcProperties(DynamicPropertyRegistry registry){
        registry.add("app.database-access-type", ()-> "jdbc");
//        registry.add("spring.datasource.url", singletonPostgreSQLContainer::getJdbcUrl);
//        registry.add("spring.datasource.username", singletonPostgreSQLContainer::getUsername);
//        registry.add("spring.datasource.password", singletonPostgreSQLContainer::getPassword);
    }
}
