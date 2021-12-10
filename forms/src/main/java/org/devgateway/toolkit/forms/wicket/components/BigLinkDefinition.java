package org.devgateway.toolkit.forms.wicket.components;

import de.agilecoders.wicket.core.markup.html.bootstrap.image.IconType;
import org.apache.wicket.Page;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;

import java.io.Serializable;

/**
 * @author Octavian Ciubotaru
 */
public class BigLinkDefinition implements Serializable {

    private final String id;
    private final Class<? extends Page> pageClass;
    private final IconType iconType;

    public BigLinkDefinition(String id, Class<? extends Page> pageClass, IconType iconType) {
        this.id = id;
        this.pageClass = pageClass;
        this.iconType = iconType;
    }

    public IModel<String> getLabelModel() {
        return new ResourceModel(id + ".label");
    }

    public IModel<String> getDescModel() {
        return new ResourceModel(id + ".desc");
    }

    public Class<? extends Page> getPageClass() {
        return pageClass;
    }

    public IconType getIconType() {
        return iconType;
    }
}
