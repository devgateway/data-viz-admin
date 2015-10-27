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

import org.devgateway.toolkit.persistence.dao.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Component;

@Component
public class SendEmailService {

	@Autowired
	private JavaMailSenderImpl javaMailSenderImpl;

	private SimpleMailMessage templateMessage;

	public SimpleMailMessage getTemplateMessage() {
		return templateMessage;
	}

	public void setTemplateMessage(SimpleMailMessage templateMessage) {
		this.templateMessage = templateMessage;
	}

	public void sendEmailToEnable(Person person, String urlEnable) {

		SimpleMailMessage msg = new SimpleMailMessage();
		msg.setTo(person.getEmail());
		msg.setFrom("support@developmentgateway.org");
		msg.setSubject("Activate your account");
		msg.setText("Dear " + person.getFirstName() + " " + person.getLastName() + ",\n\n"
				+ "To re-enable your account, please use the following link:\n\n"
				+ urlEnable + person.getSecret() + "\n\n"
				+ "The system will then prompt you to log in with your current password."
				+ " If you don't know your current password, please use the 'Forgot your password?' button."
				+ " Note that the email address you enter must match the address connected to your account.\n\n"
				+ "Thank you,\n"
				+ "DG Team");
		try {
			javaMailSenderImpl.send(msg);
		} catch (MailException e) {
			e.printStackTrace();
		}

	}

	public void sendEmailResetPassword(Person person, String newPassword) {

		SimpleMailMessage msg = new SimpleMailMessage();
		msg.setTo(person.getEmail());
		msg.setFrom("support@developmentgateway.org");
		msg.setSubject("Recover your password");
		msg.setText("Dear " + person.getFirstName() + " "+ person.getLastName() + ",\n\n"
				+ "These are your new login credentials for DGToolkit.\n\n"
				+ "Username: " + person.getUsername() + "\n" 
				+ "Password: " + newPassword + "\n\n"
				+ "At login, you will be prompted to change your password to one of your choice.\n\n"
				+ "Thank you,\n"
				+ "DG Team");
		try {
			javaMailSenderImpl.send(msg);
		} catch (MailException e) {
			e.printStackTrace();
		}

	}
}