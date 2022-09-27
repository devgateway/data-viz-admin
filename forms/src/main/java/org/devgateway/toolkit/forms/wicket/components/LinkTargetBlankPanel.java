package org.devgateway.toolkit.forms.wicket.components;

import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

public class LinkTargetBlankPanel extends Panel {

    public LinkTargetBlankPanel(String id, IModel<String> url) {
        this(id, url, url);
    }

    public LinkTargetBlankPanel(String id, IModel<String> url, IModel<String> label) {
        super(id);
        add(new ExternalLink("link", url, label) {
            private static final long serialVersionUID = -8010560272317354356L;

            @Override
            protected void onComponentTag(ComponentTag tag) {
                super.onComponentTag(tag);
                tag.put("target", "_blank");
            }
        });
    }
}
