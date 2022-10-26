package org.devgateway.toolkit.forms.wicket.page.edit.dataset;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.devgateway.toolkit.persistence.dao.data.TetsimDataset;
import org.devgateway.toolkit.persistence.dao.data.TobaccoProduct;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Viorel Chihai
 */
public class TetsimIndustryResponsesPanel extends Panel {

    protected final IModel<TetsimDataset> tetsimDatasetIModel;

    public TetsimIndustryResponsesPanel(final String id, final IModel<TetsimDataset> tetsimDatasetIModel) {
        super(id);
        this.tetsimDatasetIModel = tetsimDatasetIModel;
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
        add(getIndustryResponsesHeaders());
        add(getIndustryResponsesVariables());
    }

    private Component getIndustryResponsesHeaders() {
        RepeatingView analysisHeaders = new RepeatingView("industryResponsesHeaders");
        analysisHeaders.add(new Label(analysisHeaders.newChildId(),
                Model.of("Industry responses to the change in taxation")));
        analysisHeaders.add(new Label(analysisHeaders.newChildId(), Model.of("Unit")));

        List<TobaccoProduct> tobaccoProducts = TobaccoProduct.ALL.stream().collect(Collectors.toList());
        for (TobaccoProduct tobaccoProduct : tobaccoProducts) {
            analysisHeaders.add(new Label(analysisHeaders.newChildId(),
                    new PropertyModel<>(tobaccoProduct, "label")));
        }

        return analysisHeaders;
    }

    private Component getIndustryResponsesVariables() {
        RepeatingView variables = new RepeatingView("industryResponsesVariables");

        variables.add(getOverShifting(variables.newChildId()));
        variables.add(getUnderShifting(variables.newChildId()));

        return variables;
    }

    /**
     * Get overshifting variable panel with tobacco product inputs.
     * Mandatory. Numerical fields with decimals.
     *
     * @param id
     * @return TetsimTobaccoProductsVariable
     */
    private TetsimTobaccoProductsVariable getOverShifting(final String id) {
        return new TetsimTobaccoProductsVariable(id,
                new StringResourceModel("overshifting.label"),
                new StringResourceModel("overshifting.unit"),
                new PropertyModel<>(tetsimDatasetIModel, "overshifting"), true) {
        };
    }

    /**
     * Get undershifting variable panel with tobacco product inputs.
     * Mandatory. Numerical fields with decimals.
     *
     * @param id
     * @return TetsimTobaccoProductsVariable
     */
    private TetsimTobaccoProductsVariable getUnderShifting(final String id) {
        return new TetsimTobaccoProductsVariable(id,
                new StringResourceModel("undershifting.label"),
                new StringResourceModel("undershifting.unit"),
                new PropertyModel<>(tetsimDatasetIModel, "undershifting"), true) {
        };
    }

}


