package org.devgateway.toolkit.persistence.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

@JsonIgnoreProperties({"labels", "descriptions"})
public class ServiceDimension implements Serializable {

    private Long id;

    private String code;

    private String label;

    private String type;

    private String value;

    private Integer position;

    private CssStyle categoryStyle;

    private String field;

    private String fieldType;

    private Class aClass;

    public ServiceDimension() {
    }

    public ServiceDimension(final Long id, final String label) {
        this.id = id;
        this.label = label;
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(final String label) {
        this.label = label;
    }

    public String getType() {
        return type;
    }

    public void setType(final String type) {
        this.type = type;
    }

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

    public String getField() {
        return field;
    }

    public void setField(final String field) {
        this.field = field;
    }

    public String getFieldType() {
        return fieldType;
    }

    public void setFieldType(final String fieldType) {
        this.fieldType = fieldType;
    }

    public Class getaClass() {
        return aClass;
    }

    public void setaClass(final Class aClass) {
        this.aClass = aClass;
    }
}
