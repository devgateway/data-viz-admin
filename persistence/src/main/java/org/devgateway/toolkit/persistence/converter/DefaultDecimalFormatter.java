package org.devgateway.toolkit.persistence.converter;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Nadejda Mandrescu
 */
public class DefaultDecimalFormatter implements INumberFormatter, Serializable {

    private final ConcurrentHashMap<Locale, DecimalFormat> decimalFormats = new ConcurrentHashMap<>();

    public NumberFormat get(final Locale locale) {
        return decimalFormats.computeIfAbsent(locale, l -> init(locale));
    }

    public DecimalFormat init(final Locale locale) {
        DecimalFormat decimalFormat = (DecimalFormat) DecimalFormat.getInstance(locale);

        DecimalFormatSymbols decimalFormatSymbols = DecimalFormatSymbols.getInstance(locale);
        decimalFormatSymbols.setDecimalSeparator('.');
        decimalFormatSymbols.setGroupingSeparator(',');
        decimalFormat.setDecimalFormatSymbols(decimalFormatSymbols);
        return decimalFormat;
    }

}
