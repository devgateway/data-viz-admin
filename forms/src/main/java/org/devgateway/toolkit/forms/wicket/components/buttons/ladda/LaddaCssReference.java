package org.devgateway.toolkit.forms.wicket.components.buttons.ladda;

import org.apache.wicket.request.resource.CssResourceReference;

public class LaddaCssReference extends CssResourceReference {
    public static final LaddaCssReference INSTANCE = new LaddaCssReference();

    public LaddaCssReference() {
        super(LaddaCssReference.class, "css/ladda-themeless.css");
    }
}
