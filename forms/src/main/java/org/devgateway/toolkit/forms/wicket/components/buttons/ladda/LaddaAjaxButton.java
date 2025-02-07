package org.devgateway.toolkit.forms.wicket.components.buttons.ladda;

import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapAjaxButton;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.Buttons;
import java.io.Serializable;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.attributes.AjaxRequestAttributes;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.IModel;

public class LaddaAjaxButton extends BootstrapAjaxButton {
    private final LaddaBehavior laddaBehavior = new LaddaBehavior();

    public LaddaAjaxButton(String id, Buttons.Type type) {
        super(id, type);
    }

    public LaddaAjaxButton(String id, IModel<String> model, Buttons.Type type) {
        super(id, model, type);
    }

    public LaddaAjaxButton(String id, Form<?> form, Buttons.Type type) {
        super(id, form, type);
    }

    public LaddaAjaxButton(String id, IModel<String> model, Form<?> form, Buttons.Type type) {
        super(id, model, form, type);
    }

    protected void onInitialize() {
        super.onInitialize();
        this.add(new Behavior[]{this.laddaBehavior});
    }

    public LaddaAjaxButton setEffect(LaddaBehavior.Effect effect) {
        this.laddaBehavior.withEffect(effect);
        return this;
    }

    public LaddaAjaxButton setSpinnerColor(String color) {
        this.laddaBehavior.withSpinnerColor(color);
        return this;
    }

    public LaddaAjaxButton setSpinnerSize(int size) {
        this.laddaBehavior.withSpinnerSize(size);
        return this;
    }

    protected void updateAjaxAttributes(AjaxRequestAttributes attributes) {
        super.updateAjaxAttributes(attributes);
        attributes.getAjaxCallListeners().add(new LaddaAjaxCallListener());
    }

    protected <L extends Serializable> Component newLabel(String markupId, IModel<L> model) {
        Component label = super.newLabel(markupId, model);
        label.add(new Behavior[]{AttributeModifier.append("class", "ladda-label")});
        return label;
    }
}
