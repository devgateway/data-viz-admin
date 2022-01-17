package org.devgateway.toolkit.forms.wicket.page.edit.dataset;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.StringResourceModel;
import org.devgateway.toolkit.forms.wicket.page.lists.ListTobaccoProductPage;

public class NoTobaccoProductsPanel extends Panel {

    public NoTobaccoProductsPanel(final String id) {
        super(id);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
        add(new Label("message", getString("message")));
        add(new BookmarkablePageLink<>("link", ListTobaccoProductPage.class, null)
                .setBody(new StringResourceModel("linkText")));
    }
}
