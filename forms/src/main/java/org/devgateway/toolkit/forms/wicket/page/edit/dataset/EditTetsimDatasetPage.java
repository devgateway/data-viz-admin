package org.devgateway.toolkit.forms.wicket.page.edit.dataset;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.validation.AbstractFormValidator;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.devgateway.toolkit.forms.wicket.components.form.TextFieldBootstrapFormComponent;
import org.devgateway.toolkit.forms.wicket.page.edit.AbstractEditStatusEntityPage;
import org.devgateway.toolkit.forms.wicket.page.lists.dataset.ListTetsimDatasetPage;
import org.devgateway.toolkit.persistence.dao.data.TetsimDataset;
import org.devgateway.toolkit.persistence.dao.data.TetsimPriceVariable;
import org.devgateway.toolkit.persistence.dao.data.TetsimTobaccoProductValue;
import org.devgateway.toolkit.persistence.service.data.TetsimDatasetService;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.wicketstuff.annotation.mount.MountPath;

import java.math.BigDecimal;

import static org.devgateway.toolkit.forms.WebConstants.MAXIMUM_PERCENTAGE;
import static org.devgateway.toolkit.persistence.dao.DBConstants.Status.DELETED;

/**
 * @author vchihai
 */
@MountPath(value = "/editTetsimDataset")
public class EditTetsimDatasetPage extends AbstractEditStatusEntityPage<TetsimDataset> {

    private static final long serialVersionUID = -8460878260874111506L;

    @SpringBean
    protected TetsimDatasetService tetsimDatasetService;

    public EditTetsimDatasetPage(final PageParameters parameters) {
        super(parameters);
        this.jpaService = tetsimDatasetService;
        this.listPageClass = ListTetsimDatasetPage.class;
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        editForm.add(getYear());
        editForm.add(getBaseLineNumbers());
        editForm.add(getPriceAnalysisNumbers());
        editForm.add(getIndustryResponsesNumbers());

        editForm.add(new TetsimMarketSharePercentageValidator());
    }

    private TextFieldBootstrapFormComponent<Integer> getYear() {
        final TextFieldBootstrapFormComponent<Integer> year = new TextFieldBootstrapFormComponent<>("year");
        year.required();
        year.integer();
        return year;
    }

    @Override
    protected void onDelete(final AjaxRequestTarget target) {
        try {
            // save the object and go back to the list page
            TetsimDataset saveable = editForm.getModelObject();
            saveable.setStatus(DELETED);

            beforeSaveEntity(saveable);
            // saves the entity and flushes the changes
            jpaService.saveAndFlush(saveable);

            // clears session and detaches all entities that are currently attached
            entityManager.clear();

            // we flush the mondrian/wicket/reports cache to ensure it gets rebuilt
            flushReportingCaches();
            afterSaveEntity(saveable);
        } catch (ObjectOptimisticLockingFailureException e) {
            deleteFailedModal.show(target);
            target.add(deleteFailedModal);
        }
        setResponsePage(listPageClass);
    }

    private TetsimBaselineNumbersPanel getBaseLineNumbers() {
        return new TetsimBaselineNumbersPanel("baseLineNumbers", editForm.getModel());
    }

    private TetsimPriceAnalysisPanel getPriceAnalysisNumbers() {
        return new TetsimPriceAnalysisPanel("priceAnalysisNumbers", editForm.getModel());
    }

    private TetsimIndustryResponsesPanel getIndustryResponsesNumbers() {
        return new TetsimIndustryResponsesPanel("industryResponsesNumbers", editForm.getModel());
    }

    private class TetsimMarketSharePercentageValidator extends AbstractFormValidator {

        @Override
        public FormComponent<?>[] getDependentFormComponents() {
            return new FormComponent[0];
        }

        @Override
        public void validate(final Form<?> form) {
            TetsimPriceVariable marketShare = editForm.getModelObject().getMarketShare();
            BigDecimal sum = marketShare.getValues().stream()
                    .map(TetsimTobaccoProductValue::getValue)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            if (sum.intValue() > MAXIMUM_PERCENTAGE) {
                editForm.error(getString("error.form.validation.marketShare.percentage"));
            }
        }
    }
}
