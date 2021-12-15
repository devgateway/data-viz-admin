package org.devgateway.toolkit.forms.wicket.page.edit.dataset;

import org.apache.wicket.model.IModel;
import org.devgateway.toolkit.forms.wicket.components.form.GenericBootstrapFormComponent;
import org.devgateway.toolkit.forms.wicket.components.form.TextFieldBootstrapFormComponent;

public class TetsimBaselineDecimalVariable extends TetsimBaselineVariable {

    public TetsimBaselineDecimalVariable(final String id, final IModel<String> labelModel,
                                         final IModel<String> unitModel, final IModel<?> variableModel) {
        super(id, labelModel, unitModel, variableModel);
    }

    @Override
    protected GenericBootstrapFormComponent createInputField() {
        TextFieldBootstrapFormComponent field = new TextFieldBootstrapFormComponent<>("input", variableModel);
        field.decimal();
        field.required();
        field.hideLabel();
        return field;
    }
}
