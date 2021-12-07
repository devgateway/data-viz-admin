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
package org.devgateway.toolkit.forms.wicket.page.edit.category;

import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.string.StringValue;
import org.devgateway.toolkit.forms.WebConstants;
import org.devgateway.toolkit.forms.validators.UniquePropertyValidator;
import org.devgateway.toolkit.forms.wicket.components.form.CheckBoxYesNoToggleBootstrapFormComponent;
import org.devgateway.toolkit.forms.wicket.components.form.TextFieldBootstrapFormComponent;
import org.devgateway.toolkit.forms.wicket.page.edit.AbstractEditPage;
import org.devgateway.toolkit.forms.wicket.page.lists.ListTobaccoProductPage;
import org.devgateway.toolkit.persistence.dao.categories.TobaccoProduct;
import org.devgateway.toolkit.persistence.service.category.TobaccoProductService;
import org.wicketstuff.annotation.mount.MountPath;

/**
 * @author mpostelnicu
 */
@MountPath(value = "/editTobaccoProduct")
public class EditTobaccoProductPage extends AbstractEditPage<TobaccoProduct> {

    private static final long serialVersionUID = 8165480663853267637L;

    @SpringBean
    protected TobaccoProductService tobaccoProductService;

    public EditTobaccoProductPage(final PageParameters parameters) {
        super(parameters);
        this.jpaService = tobaccoProductService;
        this.listPageClass = ListTobaccoProductPage.class;
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        final TextFieldBootstrapFormComponent<String> name = new TextFieldBootstrapFormComponent<>("label");
        name.required();
        final StringValue id = getPageParameters().get(WebConstants.PARAM_ID);
        name.getField().add(new UniquePropertyValidator<>(tobaccoProductService, id.toLong(-1L),
                "label", this));
        name.getField().add(WebConstants.StringValidators.MAXIMUM_LENGTH_VALIDATOR_STD_DEFAULT_TEXT);
        editForm.add(name);

        editForm.add(new CheckBoxYesNoToggleBootstrapFormComponent("illicit"));
    }
}
