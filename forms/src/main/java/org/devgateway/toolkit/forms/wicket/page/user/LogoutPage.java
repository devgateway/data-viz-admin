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
package org.devgateway.toolkit.forms.wicket.page.user;

import org.apache.wicket.authroles.authentication.AbstractAuthenticatedWebSession;
import org.apache.wicket.markup.html.pages.RedirectPage;
import org.apache.wicket.request.cycle.RequestCycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.authentication.RememberMeServices;
import org.wicketstuff.annotation.mount.MountPath;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * @author mpostelnicu
 *
 */
@MountPath("/preLogout")
public class LogoutPage extends RedirectPage {
    private static final long serialVersionUID = 1L;

    @Autowired(required = false)
    private RememberMeServices rememberMeServices;

    public LogoutPage() {
        super(RequestCycle.get().getRequest().getContextPath()
                + RequestCycle.get().getRequest().getFilterPath() + "/login");
        AbstractAuthenticatedWebSession.get().invalidate();

        if (rememberMeServices != null) {
            rememberMeServices.loginFail((HttpServletRequest) RequestCycle.get().getRequest().getContainerRequest(),
                    (HttpServletResponse) RequestCycle.get().getResponse().getContainerResponse());
        }

    }
}
