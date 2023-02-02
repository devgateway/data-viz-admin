package org.devgateway.toolkit.forms.wicket.components.table.filter;

import org.apache.commons.lang3.StringUtils;
import org.devgateway.toolkit.persistence.dto.ServiceCategory;

import java.util.function.Predicate;

public class ServiceCategoryFilterState extends ServiceEntityFilterState<ServiceCategory> {

    private String code;

    private String value;

    private String type;

    @Override
    public Predicate<ServiceCategory> spec() {
        Predicate<ServiceCategory> filter = super.spec();
        if (StringUtils.isNotBlank(code)) {
            filter = filter.and((ServiceCategory) -> StringUtils.containsIgnoreCase(ServiceCategory.getCode(), code));
        }

        if (StringUtils.isNotBlank(value)) {
            filter = filter.and((ServiceCategory) -> StringUtils.containsIgnoreCase(ServiceCategory.getValue(), value));
        }

        if (StringUtils.isNotBlank(type)) {
            filter = filter.and((ServiceCategory) -> StringUtils.containsIgnoreCase(ServiceCategory.getType(), type));
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

    public String getType() {
        return type;
    }

    public void setType(final String type) {
        this.type = type;
    }
}
