package org.devgateway.toolkit.persistence.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 *
 * @author vchihai
 */
public class TetsimOutputs implements Serializable {

   private List<TetsimOutput> overshift = new ArrayList<>();

   private List<TetsimOutput> undershift = new ArrayList<>();

    public List<TetsimOutput> getOvershift() {
        return overshift;
    }

    public void setOvershift(final List<TetsimOutput> overshift) {
        this.overshift = overshift;
    }

    public List<TetsimOutput> getUndershift() {
        return undershift;
    }

    public void setUndershift(final List<TetsimOutput> undershift) {
        this.undershift = undershift;
    }
}
