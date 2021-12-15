package org.devgateway.toolkit.forms.wicket.page.edit.dataset;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.devgateway.toolkit.forms.wicket.components.form.GenericBootstrapFormComponent;

/**
 * @author Viorel Chihai
 */
public abstract class TetsimBaselineVariable extends Panel {

    private final IModel<String> labelModel;

    private final IModel<String> unitModel;

    protected final IModel<?> variableModel;

    protected GenericBootstrapFormComponent inputField;

    public TetsimBaselineVariable(final String id, final IModel<String> labelModel,
                                  final IModel<String> unitModel,
                                  final IModel<?> variableModel) {
        super(id);
        this.labelModel = labelModel;
        this.unitModel = unitModel;
        this.variableModel = variableModel;
        inputField = createInputField();
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
        add(new Label("label", labelModel));
        add(new Label("unit", unitModel));
        add(inputField);
    }

    protected abstract GenericBootstrapFormComponent createInputField();

    public GenericBootstrapFormComponent getInputField() {
        return inputField;
    }

}


