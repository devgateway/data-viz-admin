package org.devgateway.toolkit.forms.wicket.components.table.filter;

import org.apache.commons.lang3.StringUtils;
import org.devgateway.toolkit.persistence.dto.ServiceFilter;

import java.util.function.Predicate;

public class ServiceFilterFilterState extends ServiceEntityFilterState<ServiceFilter> {

    private String code;

    private String value;

    private String fieldType;

    @Override
    public Predicate<ServiceFilter> spec() {
        Predicate<ServiceFilter> filter = super.spec();
        if (StringUtils.isNotBlank(code)) {
            filter = filter.and((ServiceFilter) -> StringUtils.containsIgnoreCase(ServiceFilter.getCode(), code));
        }

        if (StringUtils.isNotBlank(value)) {
            filter = filter.and((ServiceFilter) -> StringUtils.containsIgnoreCase(ServiceFilter.getValue(), value));
        }

        if (StringUtils.isNotBlank(fieldType)) {
            filter = filter.and((ServiceFilter) -> StringUtils.containsIgnoreCase(ServiceFilter.getFieldType(), fieldType));
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

    public String getFieldType() {
        return fieldType;
    }

    public void setFieldType(final String fieldType) {
        this.fieldType = fieldType;
    }
}
