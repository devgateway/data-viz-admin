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
package org.devgateway.toolkit.forms;

import org.devgateway.toolkit.persistence.spring.CustomJPAUserDetailsService;
import org.devgateway.toolkit.web.spring.WebSecurityConfig;
import org.devgateway.toolkit.web.util.SettingsUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.RememberMeAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.rememberme.AbstractRememberMeServices;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;

import java.util.Arrays;
import java.util.stream.Collectors;

@Configuration
@EnableWebSecurity
@Order(1)
public class FormsSecurityConfig extends WebSecurityConfig {

    /**
     * Remember me key for {@link TokenBasedRememberMeServices}
     */
    private static final String UNIQUE_SECRET_REMEMBER_ME_KEY = "secret";

    @Autowired
    private SettingsUtils settingsUtils;

    @Autowired
    protected CustomJPAUserDetailsService customJPAUserDetailsService;

    @Value("${allowedApiEndpoints}")
    private String[] allowedApiEndpoints;

    @Value("${roleHierarchy}")
    private String roleHierarchyStringRepresentation;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private String[] getAllowedAPIEndpointsWithBasePath() {
        if (allowedApiEndpoints != null) {
            return Arrays.stream(allowedApiEndpoints)
                    .map(s -> settingsUtils.getFormsBasePath() + s)
                    .collect(Collectors.toList()).toArray(new String[allowedApiEndpoints.length]);
        }

        return new String[]{};
    }


    /**
     * This bean defines the same key in the
     * {@link RememberMeAuthenticationProvider}
     *
     * @return
     */
    @Bean
    public AuthenticationProvider rememberMeAuthenticationProvider() {
        return new RememberMeAuthenticationProvider(UNIQUE_SECRET_REMEMBER_ME_KEY);
    }

    /**
     * This bean configures the {@link TokenBasedRememberMeServices} with
     * {@link CustomJPAUserDetailsService}
     *
     * @return
     */
    @Bean
    public AbstractRememberMeServices rememberMeServices() {
        TokenBasedRememberMeServices rememberMeServices =
                new TokenBasedRememberMeServices(UNIQUE_SECRET_REMEMBER_ME_KEY, customJPAUserDetailsService);
        rememberMeServices.setAlwaysRemember(true);
        return rememberMeServices;
    }

    /**
     * We ensure the superclass configuration is being applied Take note the
     * {@link FormsSecurityConfig} extends {@link WebSecurityConfig} which has
     * configuration for the dg-toolkit/web module. We then apply ant matchers
     * and ignore security for css/js/images resources, and wicket mounted
     * resources
     */
    @Bean
    public SecurityFilterChain formsSecurityFilterChain(HttpSecurity http) throws Exception {
        String formsBasePath = settingsUtils.getFormsBasePath();

        http.securityContext(securityContext ->
                        securityContext.securityContextRepository(httpSessionSecurityContextRepository())
                )
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers(formsBasePath + "/monitoring/**").hasRole("ADMIN")
                        .requestMatchers(formsBasePath + "/img/**", formsBasePath + "/css*/**",
                                formsBasePath + "/js*/**", formsBasePath + "/assets*/**",
                                formsBasePath + "/favicon.ico", formsBasePath + "/resources/**",
                                formsBasePath + "/resources/public/**",
                                formsBasePath + "/wicket/resource/**/*.js", formsBasePath + "/wicket/resource/**/*.css",
                                formsBasePath + "/wicket/resource/**/*.png", formsBasePath + "/wicket/resource/**/*.jpg",
                                formsBasePath + "/wicket/resource/**/*.woff", formsBasePath + "/wicket/resource/**/*.woff2",
                                formsBasePath + "/wicket/resource/**/*.ttf", formsBasePath + "/wicket/resource/**/*.svg",
                                formsBasePath + "/wicket/resource/**/*.gif", formsBasePath + "/forgotPassword/**"
                        ).permitAll() // Ignore static resources
                        .requestMatchers(formsBasePath + "/error/**", "/error/**").permitAll()
                        .requestMatchers(formsBasePath + "/**").authenticated()
                )
                .formLogin(form ->
                        form.loginPage(formsBasePath + "/login").permitAll()
                )
                .rememberMe(rememberMe ->
                        rememberMe.key(UNIQUE_SECRET_REMEMBER_ME_KEY).alwaysRemember(true)
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.NEVER))
                .csrf(csrf -> csrf.disable())
                .anonymous(anonymous -> anonymous.disable()) // Disable anonymous users
                .headers(headers ->
                        headers.frameOptions(frameOptions -> frameOptions.sameOrigin())
                                .contentTypeOptions(Customizer.withDefaults())
                                .xssProtection(Customizer.withDefaults())
                                .contentSecurityPolicy(csp -> csp.policyDirectives("script-src 'self'; style-src 'self'"))
                                .cacheControl(Customizer.withDefaults())
                );

        return http.build();
    }
}
