package org.devgateway.toolkit.persistence.dto;

import java.io.Serializable;

public class ServiceMetadataDimension implements Serializable {

    private Long id;

    private String name;

    private String label;

    private String type;

    public ServiceMetadataDimension() {

    }

    public ServiceMetadataDimension(final Long id, final String name, final String label) {
        this.id = id;
        this.name = name;
        this.label = label;
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(final String label) {
        this.label = label;
    }

    public String getType() {
        return type;
    }

    public void setType(final String type) {
        this.type = type;
    }
}
