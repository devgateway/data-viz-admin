package org.devgateway.toolkit.forms.client;

import java.time.ZonedDateTime;

public class DatasetJobStatus {

    private Long id;

    private ZonedDateTime createdDate;

    private ZonedDateTime endDate;

    private String status;

    private String code;

    private String message;

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public ZonedDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(final ZonedDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public ZonedDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(final ZonedDateTime endDate) {
        this.endDate = endDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(final String status) {
        this.status = status;
    }

    public String getCode() {
        return code;
    }

    public void setCode(final String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(final String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "DatasetJobStatus{" +
                "id=" + id +
                ", createdDate=" + createdDate +
                ", endDate=" + endDate +
                ", status='" + status + '\'' +
                ", code='" + code + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
