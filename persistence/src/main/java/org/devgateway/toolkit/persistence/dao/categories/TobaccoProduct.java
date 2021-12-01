package org.devgateway.toolkit.persistence.dao.categories;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;

import javax.persistence.Entity;

@Entity
@Audited
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class TobaccoProduct extends Category {

    private Boolean illicit;

    public Boolean isIllicit() {
        return illicit;
    }

    public void setIllicit(final Boolean illicit) {
        this.illicit = illicit;
    }
}
