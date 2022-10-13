package org.devgateway.toolkit.forms.wicket.page.edit.admin.panel;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.TransparentWebMarkupContainer;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;
import org.devgateway.toolkit.forms.wicket.components.CompoundSectionPanel;
import org.devgateway.toolkit.forms.wicket.components.form.BootstrapAddButton;
import org.devgateway.toolkit.forms.wicket.components.form.BootstrapDeleteButton;
import org.devgateway.toolkit.forms.wicket.components.form.TextFieldBootstrapFormComponent;
import org.devgateway.toolkit.forms.wicket.components.util.ComponentUtil;
import org.devgateway.toolkit.persistence.dto.ServiceCategory;
import org.devgateway.toolkit.persistence.dto.ServiceTextTranslation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class ListViewTextTranslationSectionPanel extends CompoundSectionPanel<List<ServiceTextTranslation>> {
    private static final Logger logger = LoggerFactory.getLogger(ListViewTextTranslationSectionPanel.class);

    protected WebMarkupContainer listWrapper;

    protected ListView<ServiceTextTranslation> listView;

    public ListViewTextTranslationSectionPanel(final String id) {
        super(id);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        setOutputMarkupId(true);
        setOutputMarkupPlaceholderTag(true);

        listWrapper = new TransparentWebMarkupContainer("listWrapper");
        listWrapper.setOutputMarkupId(true);
        add(listWrapper);

        listWrapper.add(new Label("panelTitle", title));

        listView = new ListView<ServiceTextTranslation>("list", getModel()) {
            @Override
            protected void populateItem(final ListItem<ServiceTextTranslation> item) {
                item.setOutputMarkupId(true);
                item.setOutputMarkupPlaceholderTag(true);

                // we wrap the item model on a compound model so we can use the field ids as property models
                final CompoundPropertyModel<ServiceTextTranslation> compoundPropertyModel = new CompoundPropertyModel<>(item.getModel());

                // we set back the model as the compound model, thus ensures the rest of the items added will benefit
                item.setModel(compoundPropertyModel);

                // we add the rest of the items in the listItem
                populateCompoundListItem(item);

                addItemContainer(item);
            }
        };

        listView.setReuseItems(true);
        listView.setOutputMarkupId(true);
        listWrapper.add(listView);

        final BootstrapAddButton addButton = getAddNewChildButton();
        add(addButton);
    }

    private void addItemContainer(final ListItem<ServiceTextTranslation> item) {
        // we add the remove button
        final BootstrapDeleteButton removeButton = getRemoveChildButton(item.getModelObject());
        item.add(removeButton);
    }

    /**
     * Removes a child based on its index
     *
     * @param item
     * @return
     */
    private BootstrapDeleteButton getRemoveChildButton(final ServiceTextTranslation item) {
        final BootstrapDeleteButton removeButton = new BootstrapDeleteButton("remove",
                new ResourceModel("removeButton")) {
            @Override
            protected void onSubmit(final AjaxRequestTarget target) {
                ListViewTextTranslationSectionPanel.this.getModelObject().remove(item);
                listView.removeAll();
                target.add(listWrapper);
            }
        };

        removeButton.setOutputMarkupPlaceholderTag(true);
        return removeButton;
    }

    /**
     * Returns the new child button.
     */
    final BootstrapAddButton getAddNewChildButton() {
        final BootstrapAddButton newButton = new BootstrapAddButton("newButton", new ResourceModel("newButton")) {
            @Override
            protected void onSubmit(final AjaxRequestTarget target) {
                final ServiceTextTranslation newChild = createNewChild(
                        (IModel<ServiceCategory>) ListViewTextTranslationSectionPanel.this.getParent().getDefaultModel());
                ListViewTextTranslationSectionPanel.this.getModel().getObject().add(newChild);

                listView.removeAll();
                target.add(listWrapper);
            }

        };

        newButton.setOutputMarkupPlaceholderTag(true);
        return newButton;
    }

    public ServiceTextTranslation createNewChild(IModel<ServiceCategory> parentModel) {
        ServiceTextTranslation textTranslation = new ServiceTextTranslation();
        textTranslation.setParent(parentModel.getObject());

        return textTranslation;
    }

    public void populateCompoundListItem(ListItem<ServiceTextTranslation> item) {
        final TextFieldBootstrapFormComponent<String> header = ComponentUtil.addTextField(item, "language");
        header.required();

        final TextFieldBootstrapFormComponent<String> value = ComponentUtil.addTextField(item, "text");
        value.required();
    }
}
