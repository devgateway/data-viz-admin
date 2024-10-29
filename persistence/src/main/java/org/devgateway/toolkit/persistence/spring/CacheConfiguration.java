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

import org.ehcache.jsr107.EhcacheCachingProvider;
import org.hibernate.cache.jcache.ConfigSettings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.jcache.JCacheCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.auditing.DateTimeProvider;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

//import javax.cache.Caching;
//import javax.cache.CacheManager;
//import javax.management.MBeanServer;
import java.net.URISyntaxException;
import java.time.ZonedDateTime;
import java.util.Optional;

/**
 * @author mpostelnicu
 *
 */
@Configuration
@EnableCaching
@EnableJpaAuditing(dateTimeProviderRef = "auditingDateTimeProvider")
public class CacheConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(CacheConfiguration.class);
    @Bean(name = "auditingDateTimeProvider")
    public DateTimeProvider dateTimeProvider() {
        return () -> Optional.of(ZonedDateTime.now());
    }


//    @Autowired(required = false)
//    private MBeanServer mbeanServer;

    @Bean
    public HibernatePropertiesCustomizer hibernateSecondLevelCacheCustomizer(final JCacheCacheManager cacheManager) {
        return (properties) -> properties.put(ConfigSettings.CACHE_MANAGER, cacheManager.getCacheManager());
    }

//    @Bean
//    public JCacheCacheManager cacheManager() throws URISyntaxException {
//        logger.info("Location of ehcache.xml " + getClass().getResource("/ehcache.xml"));
//        CacheManager cacheManager = Caching.getCachingProvider(EhcacheCachingProvider.class.getName())
//                .getCacheManager(getClass().getResource("/ehcache.xml").toURI(), getClass().getClassLoader());
//        return new JCacheCacheManager(cacheManager);
//    }


}
