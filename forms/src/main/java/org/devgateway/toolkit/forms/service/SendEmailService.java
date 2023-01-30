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
package org.devgateway.toolkit.forms.service;

import org.apache.commons.lang3.StringUtils;
import org.devgateway.toolkit.persistence.dao.Person;
import org.devgateway.toolkit.persistence.service.AdminSettingsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

/**
 * Service to send emails to users to validate email addresses or reset
 * passwords
 *
 * @author mpostelnicu
 *
 */
@Component
public class SendEmailService {

    private static final Logger logger = LoggerFactory.getLogger(SendEmailService.class);

    private final static String DEFAULT_EMAIL_FROM_ADDRESS = "tcdisupport@developmentgateway.org";

    @Value("${spring.mail.sender}")
    private String from;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private AdminSettingsService adminSettingsService;

    private SimpleMailMessage templateMessage;

    /**
     * Send a reset password email. This is UNSAFE because passwords are sent in
     * clear text. Nevertheless some customers will ask for these emails to be
     * sent, so ...
     *
     * @param person
     * @param newPassword
     */
    public void sendEmailResetPassword(final Person person, final String newPassword) {
        String country = adminSettingsService.get().getCountryName();
        final SimpleMailMessage msg = new SimpleMailMessage();
        String fromEmail = StringUtils.isNotBlank(from) ? from : DEFAULT_EMAIL_FROM_ADDRESS;
        msg.setTo(person.getEmail());
        msg.setFrom(fromEmail);
        msg.setSubject("TCDI " + country + " - Recover your password");
        msg.setText("Dear " + person.getFirstName() + " " + person.getLastName() + ",\n\n"
                + "These are your new login credentials for TCDI Admin " + country+ ".\n\n"
                + "Username: " + person.getUsername() + "\n"
                + "Password: " + newPassword + "\n\n"
                + "At login, you will be prompted to change your password to one of your choice.\n\n" + "Thank you,\n"
                + "TCDI Team.");
        try {
            javaMailSender.send(msg);
        } catch (MailException e) {
            logger.error(e.getMessage(), e);
        }

    }
}
