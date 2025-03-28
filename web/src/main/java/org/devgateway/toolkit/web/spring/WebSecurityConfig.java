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
package org.devgateway.toolkit.web.spring;

import org.devgateway.toolkit.persistence.spring.CustomJPAUserDetailsService;
import org.devgateway.toolkit.web.util.SettingsUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.annotation.Order;
import org.springframework.security.access.expression.SecurityExpressionHandler;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextPersistenceFilter;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.security.web.firewall.StrictHttpFirewall;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * @author mpostelnicu This configures the spring security for the Web project.
 * An
 */

@Configuration
@ConditionalOnMissingClass("org.devgateway.toolkit.forms.FormsSecurityConfig")
@Order(2) // this loads the security config after the forms security (if you use
// them overlayed, it must pick that one first)
@PropertySource("classpath:allowedApiEndpoints.properties")
@EnableWebSecurity
public class WebSecurityConfig {

    @Autowired
    protected CustomJPAUserDetailsService customJPAUserDetailsService;

    @Value("${allowedApiEndpoints}")
    private String[] allowedApiEndpoints;

    @Value("${roleHierarchy}")
    private String roleHierarchyStringRepresentation;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private SettingsUtils settingsUtils;

    @Bean
    public HttpFirewall allowUrlEncodedSlashHttpFirewall() {
        final StrictHttpFirewall firewall = new StrictHttpFirewall();
        firewall.setAllowUrlEncodedSlash(true);
        firewall.setAllowSemicolon(true);
        firewall.setAllowUrlEncodedDoubleSlash(true);
        return firewall;
    }

    @Bean
    public HttpSessionSecurityContextRepository httpSessionSecurityContextRepository() {
        return new HttpSessionSecurityContextRepository();
    }

    @Bean
    public SecurityContextPersistenceFilter securityContextPersistenceFilter() {
        final SecurityContextPersistenceFilter securityContextPersistenceFilter =
                new SecurityContextPersistenceFilter(httpSessionSecurityContextRepository());
        return securityContextPersistenceFilter;
    }

//    @Override
//    public void configure(final WebSecurity web) throws Exception {
//        web.httpFirewall(allowUrlEncodedSlashHttpFirewall())
//                .ignoring().antMatchers(getAllowedAPIEndpointsWithBasePath()).and()
//                .ignoring().antMatchers(
//                        settingsUtils.getFormsBasePath() + "/login",
//                        settingsUtils.getFormsBasePath() + "/forgotPassword/**");
//    }

    private String[] getAllowedAPIEndpointsWithBasePath() {
        if (allowedApiEndpoints != null) {
            return Arrays.stream(allowedApiEndpoints)
                    .map(s -> settingsUtils.getFormsBasePath() + s)
                    .collect(Collectors.toList()).toArray(new String[allowedApiEndpoints.length]);
        }

        return new String[]{};
    }

//    @Override
//    protected void configure(final HttpSecurity http) throws Exception {
//        http.authorizeRequests().expressionHandler(webExpressionHandler()) // inject role hierarchy
//                .antMatchers(settingsUtils.getFormsBasePath() + "/monitoring/**").access("hasRole('ROLE_ADMIN')")
//                .antMatchers(settingsUtils.getFormsBasePath() + "/**").authenticated().and()
//                .formLogin().loginPage(settingsUtils.getFormsBasePath() + "/login").permitAll().and()
//                .requestCache().and().logout().permitAll().and()
//                .sessionManagement().and().csrf().disable();
//        http.addFilter(securityContextPersistenceFilter());
//    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.securityContext(securityContext -> securityContext.securityContextRepository(httpSessionSecurityContextRepository()))
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers(settingsUtils.getFormsBasePath() + "/monitoring/**").hasRole("ADMIN")
                        .requestMatchers(settingsUtils.getFormsBasePath() + "/**").authenticated()
                )
                .formLogin(form -> form
                        .loginPage(settingsUtils.getFormsBasePath() + "/login").permitAll()
                )
                .requestCache(requestCache -> requestCache.disable())
                .logout(logout -> logout.permitAll())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))
                .csrf(csrf -> csrf.disable());

        // Apply the custom SecurityExpressionHandler here
        http.setSharedObject(SecurityExpressionHandler.class, webExpressionHandler());

//        http.setSharedObject(HttpFirewall.class, allowUrlEncodedSlashHttpFirewall());
//
//        http.ignoring()
//                .requestMatchers(getAllowedAPIEndpointsWithBasePath())
//                .requestMatchers(settingsUtils.getFormsBasePath() + "/login", settingsUtils.getFormsBasePath() + "/forgotPassword/**");
//        // Firewall configuration, equivalent to 'allowUrlEncodedSlashHttpFirewall'
//        http.getSharedObject(WebSecurity.class)
//                .httpFirewall(allowUrlEncodedSlashHttpFirewall());

        // Ignoring paths (migrated from 'configure(WebSecurity web)')
//        http.getSharedObject(WebSecurity.class)
//                .ignoring()
//                .requestMatchers(getAllowedAPIEndpointsWithBasePath())
//                .requestMatchers(settingsUtils.getFormsBasePath() + "/login", settingsUtils.getFormsBasePath() + "/forgotPassword/**");

        return http.build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> {
            web.httpFirewall(allowUrlEncodedSlashHttpFirewall());
            web.ignoring()
                    .requestMatchers(getAllowedAPIEndpointsWithBasePath())
                    .requestMatchers(settingsUtils.getFormsBasePath() + "/login", settingsUtils.getFormsBasePath() + "/forgotPassword/**");
        };
    }

    /**
     * Instantiates {@see DefaultWebSecurityExpressionHandler} and assigns to it
     * role hierarchy.
     *
     * @return
     */
    private SecurityExpressionHandler<FilterInvocation> webExpressionHandler() {
        final DefaultWebSecurityExpressionHandler handler = new DefaultWebSecurityExpressionHandler();
        handler.setRoleHierarchy(roleHierarchy());
        return handler;
    }

    /**
     * Enable hierarchical roles. This bean can be used to extract all effective
     * roles.
     */
    @Bean
    RoleHierarchy roleHierarchy() {
        final RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
        roleHierarchy.setHierarchy(roleHierarchyStringRepresentation);
        return roleHierarchy;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(customJPAUserDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder);
        return authenticationProvider;
    }

    @Autowired
    public void configureGlobal(final AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(customJPAUserDetailsService).passwordEncoder(passwordEncoder);
    }
}
