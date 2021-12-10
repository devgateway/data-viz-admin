package org.devgateway.toolkit.forms.wicket.components.buttons;

import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapBookmarkablePageLink;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.Buttons;
import de.agilecoders.wicket.core.markup.html.bootstrap.image.IconType;
import org.apache.wicket.Component;
import org.apache.wicket.Page;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;

public class HomepageButton<T> extends BootstrapBookmarkablePageLink<T> {

    public <P extends Page> HomepageButton(final String componentId, final Class<P> pageClass,
                                           final IconType iconType) {
        this(componentId, pageClass, null, null, iconType);
    }

    public <P extends Page> HomepageButton(String componentId, Class<P> pageClass, IModel<String> labelModel,
                                           IModel<String> descriptionModel, IconType iconType) {
        super(componentId, pageClass, Buttons.Type.Default);

        setLabel(labelModel == null
                ? new ResourceModel(componentId + ".label")
                : labelModel);

        add(new Label("description", descriptionModel == null
                ? new ResourceModel(componentId + ".desc")
                : descriptionModel));

        setIconType(iconType);
        setSize(Buttons.Size.Large);
    }

    protected Component newLabel(final String markupId) {
        return super.newLabel(markupId).setRenderBodyOnly(false);
    }
}
