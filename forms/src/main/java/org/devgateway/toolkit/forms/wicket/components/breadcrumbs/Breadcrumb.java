package org.devgateway.toolkit.forms.wicket.components.breadcrumbs;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import java.io.Serializable;

/**
 * @author Viorel Chihai
 */
public class Breadcrumb implements Serializable {

    private static final long serialVersionUID = 1L;

    private IModel<String> labelModel;

    private boolean useLink = true;

    private boolean visible = true;

    private Class<? extends WebPage> page;

    PageParameters pageParameters;

    public Breadcrumb(final IModel<String> labelModel, final Class<? extends WebPage> page) {
        this(labelModel, page, new PageParameters());
    }

    public Breadcrumb(final IModel<String> labelModel, final Class<? extends WebPage> page, final PageParameters pageParameters) {
        this.labelModel = labelModel;
        this.page = page;
        this.pageParameters = pageParameters;
    }

    public IModel<String> getLabel() {
        return labelModel;
    }

    public void setLabel(final IModel<String> labelModel) {
        this.labelModel = labelModel;
    }

    public boolean isUseLink() {
        return useLink;
    }

    public void setUseLink(final boolean useLink) {
        this.useLink = useLink;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(final boolean visible) {
        this.visible = visible;
    }

    public void setPage(final Class<? extends WebPage> page) {
        this.page = page;
    }

    public Class<? extends WebPage> getPage() {
        return page;
    }

    public PageParameters getPageParameters() {
        return pageParameters;
    }

}