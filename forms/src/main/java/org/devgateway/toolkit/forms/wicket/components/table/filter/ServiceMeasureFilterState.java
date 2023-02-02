package org.devgateway.toolkit.forms.wicket.components.table.filter;

import org.apache.commons.lang3.StringUtils;
import org.devgateway.toolkit.persistence.dto.ServiceMeasure;

import java.util.function.Predicate;

public class ServiceMeasureFilterState extends ServiceEntityFilterState<ServiceMeasure> {

    private String code;

    private String value;

    private String parent;

    @Override
    public Predicate<ServiceMeasure> spec() {
        Predicate<ServiceMeasure> filter = super.spec();
        if (StringUtils.isNotBlank(code)) {
            filter = filter.and((ServiceMeasure) -> StringUtils.containsIgnoreCase(ServiceMeasure.getCode(), code));
        }

        if (StringUtils.isNotBlank(value)) {
            filter = filter.and((ServiceMeasure) -> StringUtils.containsIgnoreCase(ServiceMeasure.getValue(), value));
        }

        if (StringUtils.isNotBlank(parent)) {
            filter = filter.and((ServiceCategory) -> StringUtils.containsIgnoreCase(ServiceCategory.getParent(), parent));
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

    public String getParent() {
        return parent;
    }

    public void setParent(final String parent) {
        this.parent = parent;
    }
}
