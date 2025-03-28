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
package org.devgateway.toolkit.forms.wicket.converters;

import org.apache.wicket.util.convert.converter.BigDecimalConverter;
import org.devgateway.toolkit.persistence.converter.INumberFormatter;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * @author mpostelnicu
 *
 *         This is a {@link BigDecimalConverter} adapted to remove all
 *         non-decimal characters before it complains (throws exception) that
 *         the number is not a number. This will lead to all {@link BigDecimal}
 *         fields in the application to refresh showing only the number portion
 *         of the text being typed. Example: you type 'afefw@#123456' and when
 *         the textfield loses focus it will refresh to show '123,456' (the
 *         comma is the decimal separator applied by
 *         {@link BigDecimalConverter#convertToObject(String, Locale)} but
 *         that is done only after the {@link BigDecimal} was read by
 *         convertInput()
 */
public class NonNumericFilteredBigDecimalConverter extends BigDecimalConverter {

    private static final long serialVersionUID = 1L;

    private static final int MAX_SCALE_BIG_DECIMAL = 16;

    private final INumberFormatter numberFormatter;

    public NonNumericFilteredBigDecimalConverter(final INumberFormatter numberFormatter) {
        super();
        this.numberFormatter = numberFormatter;
    }

    @Override
    protected NumberFormat newNumberFormat(final Locale locale) {
        return numberFormatter.get(locale);
    }

    @Override
    public BigDecimal convertToObject(final String value, final Locale locale) {
//        String newValue = value;
//        if (newValue != null) {
//            newValue = newValue.replaceAll("[^\\d\\.]", "");
//        }

        return parse(value, null, BigDecimal.TEN.scaleByPowerOfTen(MAX_SCALE_BIG_DECIMAL), locale);
    }
}
