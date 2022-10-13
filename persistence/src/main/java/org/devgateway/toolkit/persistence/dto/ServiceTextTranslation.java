package org.devgateway.toolkit.persistence.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ServiceTextTranslation implements Serializable {
    private String text;
    private String language;

    private ServiceCategory parent;

    public String getText() {
        return text;
    }

    public void setText(final String text) {
        this.text = text;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(final String language) {
        this.language = language;
    }

    public void setParent(final ServiceCategory parent) {
        this.parent = parent;
    }
}
