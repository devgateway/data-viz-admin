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

import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.model.IModel;
import org.apache.wicket.validation.validator.StringValidator;
import org.devgateway.toolkit.forms.WebConstants;

/**
 * @author mpostelnicu
 * 
 */
public class TextAreaFieldBootstrapFormComponent<TYPE> extends GenericBootstrapFormComponent<TYPE, TextArea<TYPE>> {
	private StringValidator validator=WebConstants.StringValidators.maximumLengthValidatorTextArea;


	/**
	 * 
	 */
	private static final long serialVersionUID = -7822733988194369835L;

	public TextAreaFieldBootstrapFormComponent(String id, IModel<String> labelModel, IModel<TYPE> model) {
		super(id, labelModel, model);
	}
	
	public TextAreaFieldBootstrapFormComponent(String id, IModel<String> labelModel) {
		super(id, labelModel, null);
	}
	
	

	/**
	 * @param id
	 */
	public TextAreaFieldBootstrapFormComponent(String id) {
		super(id);
	}

	@Override
	protected TextArea<TYPE> inputField(String id, IModel<TYPE> model) {
		TextArea<TYPE> textArea=new TextArea<TYPE>(id,initFieldModel());
		return textArea;
	}
	

    @Override
    protected void onInitialize() {
    	super.onInitialize();
    	getField().add(validator);
    }
}