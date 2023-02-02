package org.devgateway.toolkit.forms.wicket.components.table.filter;

import org.apache.commons.lang3.StringUtils;
import org.devgateway.toolkit.persistence.dto.ServiceDimension;

import java.util.function.Predicate;

public class ServiceDimensionFilterState extends ServiceEntityFilterState<ServiceDimension> {

    private String code;

    private String value;

    @Override
    public Predicate<ServiceDimension> spec() {
        Predicate<ServiceDimension> filter = super.spec();
        if (StringUtils.isNotBlank(code)) {
            filter = filter.and((ServiceDimension) -> StringUtils.containsIgnoreCase(ServiceDimension.getCode(), code));
        }

        if (StringUtils.isNotBlank(value)) {
            filter = filter.and((ServiceDimension) -> StringUtils.containsIgnoreCase(ServiceDimension.getValue(), value));
        }

        return filter;
    }

    public String getCode() {
        return code;
    }

    public void setCode(final String code) {
        this.code = code;
    }

    public String getValue() {
        return value;
    }

    public void setValue(final String value) {
        this.value = value;
    }
}
