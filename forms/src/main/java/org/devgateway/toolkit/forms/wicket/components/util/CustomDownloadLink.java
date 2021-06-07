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
package org.devgateway.toolkit.forms.wicket.components.util;

import de.agilecoders.wicket.core.markup.html.bootstrap.components.TooltipBehavior;
import de.agilecoders.wicket.core.markup.html.bootstrap.components.TooltipConfig;
import de.agilecoders.wicket.core.markup.html.bootstrap.image.IconBehavior;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.icon.FontAwesome5IconType;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.handler.resource.ResourceStreamRequestHandler;
import org.apache.wicket.request.resource.ContentDisposition;
import org.apache.wicket.util.resource.AbstractResourceStreamWriter;
import org.devgateway.toolkit.persistence.dao.FileMetadata;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Serial;

/**
 * @author idobre
 * @since 11/14/14
 */
public class CustomDownloadLink extends Link<FileMetadata> {
    @Serial
    private static final long serialVersionUID = 8322284898374040757L;

    private static final TooltipConfig TOOLTIP_CONFIG =
            new TooltipConfig().withPlacement(TooltipConfig.Placement.bottom);

    public CustomDownloadLink(final String id, final IModel<FileMetadata> model) {
        super(id, model);
        add(new IconBehavior(FontAwesome5IconType.download_s));
        add(new TooltipBehavior(new StringResourceModel("downloadUploadedFileTooltip", this, null), TOOLTIP_CONFIG));
    }

    @Override
    public void onClick() {
        final AbstractResourceStreamWriter rstream = new AbstractResourceStreamWriter() {
            @Serial
            private static final long serialVersionUID = -4701627793323070910L;

            @Override
            public void write(final OutputStream output) throws IOException {
                output.write(getModelObject().getContent().getBytes());
            }

            @Override
            public String getContentType() {
                return getModelObject().getContentType();
            }
        };

        ResourceStreamRequestHandler handler = new ResourceStreamRequestHandler(rstream, getModelObject().getName());
        handler.setContentDisposition(ContentDisposition.ATTACHMENT);
        getRequestCycle().scheduleRequestHandlerAfterCurrent(handler);
    }
}
