package org.devgateway.toolkit.forms.wicket.page.lists.dataset;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.devgateway.toolkit.forms.wicket.page.lists.AbstractListPage;
import org.devgateway.toolkit.persistence.dao.GenericPersistable;
import org.devgateway.toolkit.persistence.dao.data.Dataset;

import static org.devgateway.toolkit.forms.WebConstants.PARAM_SERVICE;

public abstract class AbstractListDatasetPage<T extends Dataset> extends AbstractListPage {
    private static final long serialVersionUID = -7637952537702961987L;

    protected Fragment titleFragment;

    public AbstractListDatasetPage(final PageParameters pageParameters) {
        super(pageParameters);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        addTitleFragment();
    }

    protected void addTitleFragment() {
        String serviceName = getPageParameters().get(PARAM_SERVICE).toString();

        titleFragment = new Fragment("topPage", "localDatasetsTitleFragment",
                AbstractListDatasetPage.this);
        replace(titleFragment);

        titleFragment = new Fragment("bottomPage", "remoteDatasetsTitleFragment",
                AbstractListDatasetPage.this);
        titleFragment.add(new ListRemoteDatasetsPanel("remoteDatasetsPanel", serviceName));
        replace(titleFragment);
    }

    @Override
    protected Component getRevisionsLink(final GenericPersistable entity) {
        return new WebMarkupContainer("revisions").setVisibilityAllowed(false);
    }
}
