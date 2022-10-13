package org.devgateway.toolkit.persistence.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.devgateway.toolkit.persistence.serializer.ServiceTextTranslationDeserializer;
import org.devgateway.toolkit.persistence.serializer.ServiceTextTranslationSerializer;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ServiceCategory extends ServiceEntity {

    private String code;

    private String value;

    private Integer position;

    private CssStyle categoryStyle;

    private String type;

    @JsonDeserialize(using = ServiceTextTranslationDeserializer.class)
    @JsonSerialize(using = ServiceTextTranslationSerializer.class)
    private List<ServiceTextTranslation> labels;

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

    public String getType() {
        return type;
    }

    public void setType(final String type) {
        this.type = type;
    }

    public List<ServiceTextTranslation> getLabels() {
        return labels;
    }

    public void setLabels(final List<ServiceTextTranslation> labels) {
        this.labels = labels;
    }
}
