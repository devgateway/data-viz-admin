package org.devgateway.toolkit.forms.wicket.components.table.filter;

import org.devgateway.toolkit.persistence.dto.ServiceEntity;

import java.io.Serializable;
import java.util.function.Predicate;

public class ServiceEntityFilterState<T extends ServiceEntity> implements Serializable {

    public Predicate<T> spec() {
        return (T) -> true;
    }

}
