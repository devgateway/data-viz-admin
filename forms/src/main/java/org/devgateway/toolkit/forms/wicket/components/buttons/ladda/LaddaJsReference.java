package org.devgateway.toolkit.forms.wicket.components.buttons.ladda;

import org.apache.wicket.request.resource.JavaScriptResourceReference;

public class LaddaJsReference extends JavaScriptResourceReference {
    public static final LaddaJsReference INSTANCE = new LaddaJsReference();

    public LaddaJsReference() {
        super(LaddaJsReference.class, "js/ladda.js");
    }
}
