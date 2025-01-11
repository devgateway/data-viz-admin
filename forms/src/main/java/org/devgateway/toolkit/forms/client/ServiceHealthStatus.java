package org.devgateway.toolkit.forms.client;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ServiceHealthStatus {

    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(final String status) {
        this.status = status;
    }

    public boolean isUp() {
        return "UP".equalsIgnoreCase(status);
    }
}
