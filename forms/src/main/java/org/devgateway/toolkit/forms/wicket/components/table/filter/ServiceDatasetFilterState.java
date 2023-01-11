package org.devgateway.toolkit.forms.wicket.components.table.filter;

import org.apache.commons.lang3.StringUtils;
import org.devgateway.toolkit.persistence.dto.ServiceDataset;

import java.util.function.Predicate;

public class ServiceDatasetFilterState extends ServiceEntityFilterState<ServiceDataset> {

    private String code;

    private String value;

    @Override
    public Predicate<ServiceDataset> spec() {
        Predicate<ServiceDataset> filter = super.spec();
        if (StringUtils.isNotBlank(code)) {
            filter = filter.and((ServiceDataset) -> StringUtils.containsIgnoreCase(ServiceDataset.getCode(), code));
        }

        if (StringUtils.isNotBlank(value)) {
            filter = filter.and((ServiceDataset) -> StringUtils.containsIgnoreCase(ServiceDataset.getValue(), value));
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
