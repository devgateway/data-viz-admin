package org.devgateway.toolkit.forms.validators;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.validation.AbstractFormValidator;
import org.devgateway.toolkit.persistence.dto.ServiceCategory;
import org.devgateway.toolkit.persistence.dto.ServiceTextTranslation;

import java.util.stream.Collectors;

public class UniqueLanguageTranslationValidator extends AbstractFormValidator {

    @Override
    public FormComponent<?>[] getDependentFormComponents() {
        return new FormComponent[0];
    }

    @Override
    public void validate(final Form<?> form) {
        ServiceCategory serviceCategory = (ServiceCategory) form.getModelObject();
        serviceCategory.getLabels().stream()
                .filter(label -> label.getLanguage() != null)
                .collect(Collectors.groupingBy(s -> s.getLanguage().toUpperCase()))
                .entrySet().stream()
                .filter(entry -> entry.getValue().size() > 1)
                .forEach(entry -> {
                    form.error("Language " + entry.getKey() + " is duplicated");
                });
    }
}
