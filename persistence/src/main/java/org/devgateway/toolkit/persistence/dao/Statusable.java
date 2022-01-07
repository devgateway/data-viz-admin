package org.devgateway.toolkit.persistence.dao;

import com.fasterxml.jackson.annotation.JsonIgnore;

import static org.devgateway.toolkit.persistence.dao.DBConstants.Status.PUBLISHED;

/**
 * @author mihai
 * <p>
 * Assigned to objects that provide a status, in our case, objects derived from
 * {@link AbstractStatusAuditableEntity}
 */
public interface Statusable {

    String getStatus();

    @JsonIgnore
    default boolean isPublished() {
        return PUBLISHED.equals(getStatus());
    }
}
