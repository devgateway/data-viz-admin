package org.devgateway.toolkit.forms.validators;

import org.apache.wicket.Component;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.IValidator;
import org.apache.wicket.validation.ValidationError;
import org.devgateway.toolkit.persistence.service.category.TobaccoProductService;

import java.util.Collection;
import java.util.Collections;

/**
 * @author Viorel Chihai
 */
public class UniqueTobaccoProductIllicitValidator implements IValidator<Boolean> {
    private static final long serialVersionUID = -695597033541103492L;

    protected final TobaccoProductService tobaccoProductService;

    protected final Collection<Long> entityIdsToIgnore;

    protected final String propertyName;

    protected final IModel<String> propertyLabel;

    public UniqueTobaccoProductIllicitValidator(final TobaccoProductService tobaccoProductService,
                                                final long entityIdToIgnore, final String propertyName,
                                                final Component component) {
        this(tobaccoProductService, Collections.singleton(entityIdToIgnore), propertyName, component);
    }

    public UniqueTobaccoProductIllicitValidator(final TobaccoProductService uniquePropertyService,
                                                final Collection<Long> entityIdsToIgnore, final String propertyName,
                                                final Component component) {
        this(uniquePropertyService, entityIdsToIgnore, propertyName,
                new StringResourceModel(propertyName + ".label", component));
    }

    public UniqueTobaccoProductIllicitValidator(final TobaccoProductService tobaccoProductService,
                                                final Collection<Long> entityIdsToIgnore, final String propertyName,
                                                final IModel<String> propertyLabel) {
        if (entityIdsToIgnore.isEmpty()) {
            entityIdsToIgnore.add(-1L);
        }

        this.tobaccoProductService = tobaccoProductService;
        this.entityIdsToIgnore = entityIdsToIgnore;
        this.propertyName = propertyName;
        this.propertyLabel = propertyLabel;
    }

    @Override
    public void validate(final IValidatable<Boolean> validatable) {
        final Boolean value = validatable.getValue();
        if (Boolean.TRUE.equals(value)
                && tobaccoProductService.existsByProperty(propertyName, value, entityIdsToIgnore)) {
            ValidationError error = new ValidationError(this);
            validatable.error(error);
        }
    }

}
