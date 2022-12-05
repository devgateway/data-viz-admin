package org.devgateway.toolkit.forms.wicket.components.breadcrumbs;

import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapBookmarkablePageLink;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.devgateway.toolkit.forms.wicket.page.BasePage;

import java.util.ArrayList;
import java.util.List;

import static de.agilecoders.wicket.core.markup.html.bootstrap.button.Buttons.Type.Link;

/**
 * A component that renders bread crumbs.
 *
 * <pre>
 * first / second / third
 * </pre>
 *
 * @author Viorel Chihai
 */
public abstract class BreadCrumbPanel extends Panel {

    private List<Breadcrumb> breadcrumbs;

    public BreadCrumbPanel(final String id) {
        super(id);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
        getBreadcrumbs().clear();
        createBreadcrumb();

        IModel<List<Breadcrumb>> breadcrumbsModel = new IModel<List<Breadcrumb>>() {
            private static final long serialVersionUID = 1L;

            @Override
            public List<Breadcrumb> getObject() {
                return getBreadcrumbs();
            }
        };

        ListView<Breadcrumb> breadcrumbList = new ListView<Breadcrumb>("breadcrumb", breadcrumbsModel) {
            private static final long serialVersionUID = 1L;

            @Override
            protected void populateItem(final ListItem<Breadcrumb> item) {
                final Breadcrumb dto = item.getModelObject();
                BootstrapBookmarkablePageLink link = new BootstrapBookmarkablePageLink("bcLink", dto.getPage(), dto.getPageParameters(), Link);
                link.setLabel(dto.getLabel());
                item.add(link);
                link.setEnabled(dto.isUseLink());
            }
        };
        add(breadcrumbList);
    }

    public List<Breadcrumb> getBreadcrumbs() {
        if (breadcrumbs == null) {
            breadcrumbs = new ArrayList<>();
        }

        return breadcrumbs;
    }

    public void addBreadcrumb(final Breadcrumb breadcrumb) {
        getBreadcrumbs().add(breadcrumb);
    }

    protected void createBreadcrumb() {
        BreadCrumbPage breadCrumbAnnotation = getPageClass().getDeclaredAnnotation(BreadCrumbPage.class);
        if (breadCrumbAnnotation != null) {
            if (!breadCrumbAnnotation.isRoot()) {
                createParentBreadcrumb(breadCrumbAnnotation.parent());
            }

            Breadcrumb bc = new Breadcrumb(new IModel<String>() {
                private static final long serialVersionUID = 1L;

                @Override
                public String getObject() {
                    return getLabelModel().getObject();
                }
            }, getPageClass());
            bc.setUseLink(false);
            addBreadcrumb(bc);
        }
    }

    protected void createParentBreadcrumb(final Class<? extends BasePage> clazz) {
        if (clazz != null && clazz.getDeclaredAnnotation(BreadCrumbPage.class) != null) {
            if (!clazz.getDeclaredAnnotation(BreadCrumbPage.class).isRoot()) {
                createParentBreadcrumb(clazz.getDeclaredAnnotation(BreadCrumbPage.class).parent());
            }

            Breadcrumb bc = new Breadcrumb(new IModel<String>() {
                private static final long serialVersionUID = 1L;

                @Override
                public String getObject() {
                    return getLabelModel(clazz).getObject();
                }
            }, clazz, getBreadcrumbPageParameters(clazz));
            addBreadcrumb(bc);
        }
    }

    private PageParameters getBreadcrumbPageParameters(final Class<? extends BasePage> clazz) {
        String[] params = clazz.getDeclaredAnnotation(BreadCrumbPage.class).params();

        if (params.length > 0) {
            PageParameters pageParameters = new PageParameters();
            String paramValue = this.getPage().getPageParameters().get(params[0]).toString();
            pageParameters.set(params[0], paramValue);
            return pageParameters;
        }

        return new PageParameters();

    }

    protected abstract Class<? extends WebPage> getPageClass();

    protected abstract IModel<String> getLabelModel();

    protected abstract IModel<String> getLabelModel(Class<? extends BasePage> clazz);


}
