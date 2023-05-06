package dataaccess.jpa;

import dataaccess.IntegrationEnvironment;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.PlatformTransactionManager;
import ru.tinkoff.edu.java.scrapper.configuration.JPAAccessConfiguration;

import javax.sql.DataSource;
import java.util.Properties;

@SpringBootTest(classes = {JPAIntegrationEnvironment.JPAConfigClass.class, JPAAccessConfiguration.class})
public class JPAIntegrationEnvironment extends IntegrationEnvironment {
    @Configuration
    @ComponentScan(basePackages = {"ru.tinkoff.edu.java.scrapper.dataaccess.impl.jpa"})
    static class JPAConfigClass {
        @Bean
        public DataSource dataSource(){
            return new DriverManagerDataSource(singletonPostgreSQLContainer.getJdbcUrl(),
                    singletonPostgreSQLContainer.getUsername(),
                    singletonPostgreSQLContainer.getPassword());
        }

        @Bean
        public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource) {
            LocalContainerEntityManagerFactoryBean entityManagerFactory =
                    new LocalContainerEntityManagerFactoryBean();
            entityManagerFactory.setDataSource(dataSource);
            entityManagerFactory.setPackagesToScan("ru.tinkoff.edu.java.scrapper.dataaccess.impl.jpa.entities");
            entityManagerFactory.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
            entityManagerFactory.setJpaProperties(jpaProperties());
            return entityManagerFactory;
        }

        @Bean
        public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
            return new JpaTransactionManager(entityManagerFactory);
        }

        private Properties jpaProperties() {
            Properties props = new Properties();
            props.setProperty("hibernate.show_sql", "true");
            props.setProperty("hibernate.format_sql", "true");
            return props;
        }
    }
    @DynamicPropertySource
    static void jpaProperties(DynamicPropertyRegistry registry){
        registry.add("app.database-access-type", ()-> "jpa");
        registry.add("spring.datasource.url", singletonPostgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", singletonPostgreSQLContainer::getUsername);
        registry.add("spring.datasource.password", singletonPostgreSQLContainer::getPassword);
        registry.add("spring.jpa.properties.hibernate.jdbc.lob.non_contextual_creation",
                ()->Boolean.TRUE);
        registry.add("spring.jpa.properties.hibernate.dialect",
                ()-> "org.hibernate.dialect.PostgreSQLDialect");
    }
}
