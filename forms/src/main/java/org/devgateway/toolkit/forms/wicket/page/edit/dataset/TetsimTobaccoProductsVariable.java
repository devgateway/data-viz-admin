package org.devgateway.toolkit.forms.wicket.page.edit.dataset;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.devgateway.toolkit.forms.wicket.components.form.TextFieldBootstrapFormComponent;
import org.devgateway.toolkit.persistence.dao.categories.TobaccoProduct;
import org.devgateway.toolkit.persistence.dao.data.TetsimPriceVariable;
import org.devgateway.toolkit.persistence.dao.data.TetsimTobaccoProductValue;
import org.devgateway.toolkit.persistence.service.category.TobaccoProductService;

import java.util.List;
import java.util.Set;

/**
 * @author Viorel Chihai
 */
public class TetsimTobaccoProductsVariable extends Panel {

    private final IModel<String> labelModel;

    private final IModel<String> unitModel;

    private final boolean required;

    private final boolean illicitOnly;

    protected final IModel<TetsimPriceVariable> variableModel;

    @SpringBean
    protected TobaccoProductService tobaccoProductService;

    protected RepeatingView inputFields;

    public TetsimTobaccoProductsVariable(final String id, final IModel<String> labelModel,
                                         final IModel<String> unitModel,
                                         final IModel<TetsimPriceVariable> variableModel) {
        this(id, labelModel, unitModel, variableModel, true);
    }

    public TetsimTobaccoProductsVariable(final String id, final IModel<String> labelModel,
                                         final IModel<String> unitModel,
                                         final IModel<TetsimPriceVariable> variableModel,
                                         final boolean required) {
        this(id, labelModel, unitModel, variableModel, required, false);
    }

    public TetsimTobaccoProductsVariable(final String id, final IModel<String> labelModel,
                                         final IModel<String> unitModel,
                                         final IModel<TetsimPriceVariable> variableModel,
                                         final boolean required, final boolean illicitOnly) {
        super(id);
        this.labelModel = labelModel;
        this.unitModel = unitModel;
        this.variableModel = variableModel;
        this.required = required;
        this.illicitOnly = illicitOnly;

        inputFields = createInputFields();
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
        add(new Label("label", labelModel));
        add(new Label("unit", unitModel));
        add(inputFields);
    }

    protected RepeatingView createInputFields() {
        RepeatingView fields = new RepeatingView("tobaccoProducts");
        List<TobaccoProduct> tobaccoProducts = tobaccoProductService.findAllSorted();
        TetsimPriceVariable priceVariable = variableModel.getObject();

        for (TobaccoProduct tobaccoProduct: tobaccoProducts) {
            if (!illicitOnly || tobaccoProduct.isIllicit()) {
                TetsimTobaccoProductValue tobaccoProductValue = getTobaccoProductValue(priceVariable, tobaccoProduct);
                TextFieldBootstrapFormComponent field = new TextFieldBootstrapFormComponent<>(fields.newChildId(),
                        new PropertyModel<>(Model.of(tobaccoProductValue), "value"));
                field.decimal();
                field.hideLabel();
                field.getField().setRequired(required);

                addBehavioursToTobaccoProductVariable(field);

                fields.add(field);
            } else {
                fields.add(new WebMarkupContainer(fields.newChildId()));
            }
        }

        return fields;
    }

    public TetsimTobaccoProductValue getTobaccoProductValue(final TetsimPriceVariable priceVariable,
                                                            final TobaccoProduct product) {
        Set<TetsimTobaccoProductValue> tobaccoProductValues = priceVariable.getValues();

        TetsimTobaccoProductValue productValue = tobaccoProductValues.stream()
                .filter(t -> t.getProduct().getId().equals(product.getId())).findAny()
                .orElse(null);

        if (productValue == null) {
            productValue = new TetsimTobaccoProductValue();
            productValue.setProduct(product);
            productValue.setPriceVariable(priceVariable);
            tobaccoProductValues.add(productValue);
        }

        return productValue;
    }

    protected void addBehavioursToTobaccoProductVariable(final TextFieldBootstrapFormComponent variable) {
    }

}


