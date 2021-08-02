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
/**
 *
 */
package org.devgateway.toolkit.forms.wicket.components.form;

import de.agilecoders.wicket.core.markup.html.bootstrap.behavior.CssClassNameAppender;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.form.datetime.DatetimePicker;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.form.datetime.DatetimePickerConfig;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.form.datetime.DatetimePickerIconConfig;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.icon.FontAwesome5IconType;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxLink;
import org.apache.wicket.model.IModel;

import java.util.Date;

import static org.devgateway.toolkit.persistence.PersistenceConstants.DATE_TIME_PATTERN;

/**
 * @author mpostelnicu
 *
 */
public class DateTimeFieldBootstrapFormComponent extends GenericBootstrapFormComponent<Date, DatetimePicker> {
    private static final long serialVersionUID = 6829640010904041758L;

    private DatetimePickerConfig config;

    /**
     * @param id
     * @param labelModel
     * @param model
     */
    public DateTimeFieldBootstrapFormComponent(final String id, final IModel<String> labelModel,
            final IModel<Date> model) {
        super(id, labelModel, model);
    }

    public DateTimeFieldBootstrapFormComponent(final String id) {
        super(id);
    }

    /**
     * @param id
     * @param model
     */
    public DateTimeFieldBootstrapFormComponent(final String id, final IModel<Date> model) {
        super(id, model);
    }

    @Override
    protected DatetimePicker inputField(final String id, final IModel<Date> model) {
        config = new DatetimePickerConfig().withFormat(DATE_TIME_PATTERN);
        config.with(
                new DatetimePickerIconConfig()
                        .useTimeIcon(FontAwesome5IconType.clock_r));
        return new DatetimePicker("field", initFieldModel(), config);
    }

    @Override
    public String getUpdateEvent() {
        return "change";
    }

    /*
     * (non-Javadoc)
     *
     * @see org.devgateway.toolkit.forms.wicket.components.form.
     * GenericBootstrapFormComponent#onConfigure()
     */
    @Override
    protected void onInitialize() {
        super.onInitialize();

        border.add(new CssClassNameAppender("position-relative"));

        IndicatingAjaxLink<String> clearDateLink = new IndicatingAjaxLink<String>("clearDate") {
            private static final long serialVersionUID = -1705495886974891511L;

            @Override
            public void onClick(final AjaxRequestTarget target) {
                DateTimeFieldBootstrapFormComponent.this.field.setModelObject(null);
                target.add(DateTimeFieldBootstrapFormComponent.this.field);
            }
        };
        border.add(clearDateLink);
    }

}
