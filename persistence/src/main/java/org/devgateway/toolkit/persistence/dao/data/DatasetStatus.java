package org.devgateway.toolkit.persistence.dao.data;

public enum DatasetStatus {

    DRAFT("Draft"),
    SAVED("Saved"),
    PUBLISHED("Published"),
    UNPUBLISHED("Unpublished");

    private String label;

    DatasetStatus(final String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
