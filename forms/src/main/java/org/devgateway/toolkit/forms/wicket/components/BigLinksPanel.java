package org.devgateway.toolkit.forms.wicket.components;

import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.GenericPanel;
import org.apache.wicket.model.IModel;
import org.devgateway.toolkit.forms.wicket.components.buttons.HomepageButton;

import java.util.List;

/**
 * @author Octavian Ciubotaru
 */
public class BigLinksPanel extends GenericPanel<List<BigLinkDefinition>> {

    public BigLinksPanel(String id, IModel<List<BigLinkDefinition>> model) {
        super(id, model);

        int size = model.getObject().size();
        int colSpan = (size == 0 || size > 3) ? 3 : 12 / size;

        add(new ListView<BigLinkDefinition>("buttons", model) {

            @Override
            protected void populateItem(ListItem<BigLinkDefinition> item) {
                BigLinkDefinition def = item.getModelObject();
                item.add(new AttributeAppender("class", "col-md-" + colSpan, " "));
                item.add(new HomepageButton<>("button",
                        def.getPageClass(), def.getLabelModel(), def.getDescModel(), def.getIconType()));
            }
        });
    }
}
