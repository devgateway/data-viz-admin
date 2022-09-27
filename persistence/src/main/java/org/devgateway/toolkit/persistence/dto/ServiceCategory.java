package org.devgateway.toolkit.persistence.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ServiceCategory extends ServiceEntity {

    private String code;

    private String value;

    private Integer position;

    private CssStyle categoryStyle;

    public String getCode() {
        return code;
    }

    public void setCode(final String code) {
        this.code = code;
    }

    public String getValue() {
        return value;
    }

    public void setValue(final String value) {
        this.value = value;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(final Integer position) {
        this.position = position;
    }

    public CssStyle getCategoryStyle() {
        return categoryStyle;
    }

    public void setCategoryStyle(final CssStyle categoryStyle) {
        this.categoryStyle = categoryStyle;
    }

}
