/**
 * Copyright (c) 2015 Development Gateway, Inc and others.
 * <p>
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the MIT License (MIT)
 * which accompanies this distribution, and is available at
 * https://opensource.org/licenses/MIT
 * <p>
 * Contributors:
 * Development Gateway - initial API and implementation
 */
/**
 *
 */
package org.devgateway.toolkit.persistence.spring;

import com.zaxxer.hikari.HikariDataSource;
import liquibase.integration.spring.SpringLiquibase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.mock.jndi.SimpleNamingContextBuilder;

import javax.naming.NamingException;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

/**
 * @author mpostelnicu
 *
 */
@Configuration
@PropertySources({
    @PropertySource("classpath:/org/devgateway/toolkit/persistence/application.properties"),
    @PropertySource(
            value = "classpath:/org/devgateway/toolkit/persistence/application-${spring.profiles.active}.properties",
            ignoreResourceNotFound = true)
})
@Profile("!integration")
public class DatabaseConfiguration {
    private static final Logger logger = LoggerFactory.getLogger(DatabaseConfiguration.class);

    @Value("${dg-toolkit.datasource.jndi-name}")
    private String datasourceJndiName;

    /**
     * This bean creates the JNDI tree and registers the
     * {@link javax.sql.DataSource} to this tree. This allows Pentaho Classic
     * Engine to use a {@link javax.sql.DataSource} ,in our case backed by a
     * connection pool instead of always opening up JDBC connections. Should
     * significantly improve performance of all classic reports. In PRD use
     * connection type=JNDI and name toolkitDS. To use it in PRD you need to add
     * the configuration to the local PRD. Edit
     * ~/.pentaho/simple-jndi/default.properties and add the following:
     * toolkitDS/type=javax.sql.DataSource
     * toolkitDS/driver=org.apache.derby.jdbc.ClientDriver toolkitDS/user=app
     * toolkitDS/password=app
     * toolkitDS/url=jdbc:derby://localhost//derby/toolkit
     *
     * @return
     */
    @Bean
    public SimpleNamingContextBuilder jndiBuilder() {
        SimpleNamingContextBuilder builder = new SimpleNamingContextBuilder();
        builder.bind(datasourceJndiName, dataSource());
        try {
            builder.activate();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (NamingException e) {
            e.printStackTrace();
        }
        return builder;
    }

    /**
     * Creates a {@link javax.sql.DataSource} based on HikariCP {@link HikariDataSource}
     *
     * @return
     */
    @Bean
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource dataSource() {
        return DataSourceBuilder.create().build();
    }


    @Bean
    public SpringLiquibaseRunner liquibaseAfterJPA(final SpringLiquibase springLiquibase,
                                                   final EntityManagerFactory entityManagerFactory) {
        logger.info("Instantiating SpringLiquibaseRunner after initialization of entityManager using factory "
                + entityManagerFactory);
        return new SpringLiquibaseRunner(springLiquibase);
    }

}
