package org.devgateway.toolkit.forms.wicket.components.buttons.ladda;

import de.agilecoders.wicket.core.markup.html.bootstrap.behavior.ICssClassNameProvider;
import de.agilecoders.wicket.core.util.Attributes;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.references.SpinJsReference;
import org.apache.wicket.Component;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.util.lang.Args;
import org.apache.wicket.util.string.Strings;

public class LaddaBehavior extends Behavior {
    private Effect effect;
    private String spinnerColor;
    private int spinnerSize;

    public LaddaBehavior() {
        this.effect = LaddaBehavior.Effect.ZOOM_OUT;
    }

    public LaddaBehavior withEffect(Effect effect) {
        this.effect = (Effect)Args.notNull(effect, "effect");
        return this;
    }

    public LaddaBehavior withSpinnerColor(String color) {
        this.spinnerColor = color;
        return this;
    }

    public LaddaBehavior withSpinnerSize(int size) {
        this.spinnerSize = size;
        return this;
    }

    public void onComponentTag(Component component, ComponentTag tag) {
        super.onComponentTag(component, tag);
        Attributes.addClass(tag, new String[]{"ladda-button"});
        Attributes.set(tag, "data-style", this.effect.cssClassName());
        if (!Strings.isEmpty(this.spinnerColor)) {
            Attributes.set(tag, "data-spinner-color", this.spinnerColor);
        }

        if (this.spinnerSize > 0) {
            Attributes.set(tag, "data-spinner-size", String.valueOf(this.spinnerSize));
        }

    }

    public void renderHead(Component component, IHeaderResponse response) {
        super.renderHead(component, response);
        response.render(CssHeaderItem.forReference(LaddaCssReference.INSTANCE));
        response.render(JavaScriptHeaderItem.forReference(SpinJsReference.INSTANCE));
        response.render(JavaScriptHeaderItem.forReference(LaddaJsReference.INSTANCE));
    }

    public static enum Effect implements ICssClassNameProvider {
        EXPAND_LEFT,
        EXPAND_RIGHT,
        EXPAND_UP,
        EXPAND_DOWN,
        ZOOM_IN,
        ZOOM_OUT,
        SLIDE_LEFT,
        SLIDE_RIGHT,
        SLIDE_UP,
        SLIDE_DOWN,
        CONTRACT;

        private Effect() {
        }

        public String cssClassName() {
            return this.name().toLowerCase().replace('_', '-');
        }
    }
}