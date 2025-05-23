/*******************************************************************************
 * Copyright (c) 2015 Development Gateway, Inc and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the MIT License (MIT)
 * which accompanies this distribution, and is available at
 * https://opensource.org/licenses/MIT
 *
 * Contributors:
 * Development Gateway - initial API and implementation
 *******************************************************************************/
package org.devgateway.toolkit.forms.wicket;

import org.apache.wicket.protocol.http.WicketFilter;
import org.apache.wicket.spring.SpringWebApplicationFactory;
import org.devgateway.toolkit.web.util.SettingsUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.web.session.HttpSessionEventPublisher;

import jakarta.servlet.FilterRegistration;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;

/**
 * This class is the replacement of the web.xml. It registers the wicket filter
 * in the spring aware configuration style.
 *
 * @author Stefan Kloe
 *
 */
@Configuration
public class WebInitializer implements ServletContextInitializer {

    @Autowired
    private SettingsUtils settingsUtils;

    private static final String PARAM_APP_BEAN = "applicationBean";

    @Override
    public void onStartup(final ServletContext sc) throws ServletException {

        // AUTO configured by spring boot 1.2.x and upper
        // sc.addFilter(AbstractSecurityWebApplicationInitializer.DEFAULT_FILTER_NAME,
        // new DelegatingFilterProxy("springSecurityFilterChain"))
        // .addMappingForUrlPatterns(null, false, "/*");

        sc.addFilter("Spring OpenEntityManagerInViewFilter",
                org.springframework.orm.jpa.support.OpenEntityManagerInViewFilter.class)
                .addMappingForUrlPatterns(null, false, "/*");

        FilterRegistration filter = sc.addFilter("wicket-filter", WicketFilter.class);
        filter.setInitParameter(WicketFilter.APP_FACT_PARAM, SpringWebApplicationFactory.class.getName());
        filter.setInitParameter(PARAM_APP_BEAN, "formsWebApplication");
        // This line is the only surprise when comparing to the equivalent
        // web.xml. Without some initialization seems to be missing.
        filter.setInitParameter(WicketFilter.FILTER_MAPPING_PARAM, settingsUtils.getFormsBasePath() + "/*");
        filter.addMappingForUrlPatterns(null, false, settingsUtils.getFormsBasePath() + "/*");

        // // Request Listener
        // sc.addListener(new RequestContextListener());
        //
        // sc.addListener(new ContextCleanupListener());
        //
        sc.addListener(new HttpSessionEventPublisher());

    }

    // @Bean
    // public SessionFactory sessionFactory(EntityManagerFactory factory) {
    // if (factory.unwrap(SessionFactory.class) == null) {
    // throw new NullPointerException("factory is not a hibernate factory");
    // }
    //
    // return factory.unwrap(SessionFactory.class);
    // }
    //
}
