package org.devgateway.toolkit.persistence.dto;

import java.io.Serializable;

public class ServiceEntity implements Serializable {

    private Long id;

    ServiceEntity() {
    }

    public ServiceEntity(final Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }
}
