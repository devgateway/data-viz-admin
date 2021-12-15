package org.devgateway.toolkit.forms.validators;

import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.IValidator;
import org.apache.wicket.validation.ValidationError;

import java.math.BigDecimal;

public class PositiveBigDecimalValidator implements IValidator<BigDecimal> {

    private boolean includeZero;

    public PositiveBigDecimalValidator() {
        this(false);
    }

    public PositiveBigDecimalValidator(boolean includeZero) {
        this.includeZero = includeZero;
    }

    @Override
    public void validate(final IValidatable<BigDecimal> validatable) {
        BigDecimal value = validatable.getValue();
        if (includeZero) {
            if (value.compareTo(BigDecimal.ZERO) < 0) {
                ValidationError error = new ValidationError(this);
                error.addKey("positive");
                validatable.error(error);
            }
        } else if (value.compareTo(BigDecimal.ZERO) <= 0) {
            ValidationError error = new ValidationError(this);
            error.addKey("nonNegative");
            validatable.error(error);
        }
    }
}
