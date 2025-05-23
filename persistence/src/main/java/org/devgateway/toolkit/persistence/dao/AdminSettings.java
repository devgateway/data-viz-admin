package org.devgateway.toolkit.persistence.dao;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;

import jakarta.persistence.Entity;
import java.time.Duration;
import java.time.LocalDateTime;

import static org.springframework.beans.support.PagedListHolder.DEFAULT_PAGE_SIZE;

/**
 * @author idobre
 * @since 6/22/16
 */
@Entity
@Audited
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class AdminSettings extends AbstractAuditableEntity {
    private static final long serialVersionUID = -1051140524022133178L;

    public static final Duration REBOOT_ALERT_DURATION = Duration.ofMinutes(10L);
    public static final int AUTOSAVE_TIME_DEFAULT = 10;

    private Boolean rebootServer = false;

    private LocalDateTime rebootAlertSince;

    private Integer autosaveTime = AUTOSAVE_TIME_DEFAULT;

    private Integer pageSize = DEFAULT_PAGE_SIZE;

    private String tetsimCurrency;

    private String countryName;

    @Override
    public AbstractAuditableEntity getParent() {
        return null;
    }

    public Boolean getRebootServer() {
        return rebootServer;
    }

    public void setRebootServer(final Boolean rebootServer) {
        this.rebootServer = rebootServer;
    }

    public boolean isRebootServer() {
        return Boolean.TRUE.equals(getRebootServer());
    }

    public LocalDateTime getRebootAlertSince() {
        return rebootAlertSince;
    }

    public void setRebootAlertSince(final LocalDateTime rebootAlertSince) {
        this.rebootAlertSince = rebootAlertSince;
    }

    public Integer getAutosaveTime() {
        return autosaveTime;
    }

    public void setAutosaveTime(Integer autosaveTime) {
        this.autosaveTime = autosaveTime;
    }

    public String getTetsimCurrency() {
        return tetsimCurrency;
    }

    public void setTetsimCurrency(String tetsimCurrency) {
        this.tetsimCurrency = tetsimCurrency;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(final String countryName) {
        this.countryName = countryName;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(final Integer pageSize) {
        this.pageSize = pageSize;
    }
}
