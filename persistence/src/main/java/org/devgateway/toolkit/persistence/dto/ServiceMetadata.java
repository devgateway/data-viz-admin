package org.devgateway.toolkit.persistence.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ServiceMetadata implements Serializable {

    private String id;

    private String name;

    private String description;

    private String url;

    private String type;

    private String status;

    private String label;

    private List<ServiceDimension> dimensions = new ArrayList<>();

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(final String url) {
        this.url = url;
    }

    public String getType() {
        return type;
    }

    public void setType(final String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return getName();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(final String status) {
        this.status = status;
    }

    public List<ServiceDimension> getDimensions() {
        return dimensions;
    }

    public void setDimensions(final List<ServiceDimension> dimensions) {
        this.dimensions = dimensions;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(final String label) {
        this.label = label;
    }
}
