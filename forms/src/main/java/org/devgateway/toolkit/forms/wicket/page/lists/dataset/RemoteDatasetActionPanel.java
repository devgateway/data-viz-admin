package org.devgateway.toolkit.forms.wicket.page.lists.dataset;

import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapAjaxButton;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.Buttons;
import de.agilecoders.wicket.core.markup.html.bootstrap.dialog.Modal;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.StringResourceModel;
import org.devgateway.toolkit.persistence.dto.ServiceDataset;

public abstract class RemoteDatasetActionPanel extends Panel {

    public RemoteDatasetActionPanel(final String id, final IModel<ServiceDataset> model) {
        super(id, model);

        Form form = new Form("actionForm");
        add(form);

        BootstrapAjaxButton reject = new BootstrapAjaxButton("remove",
                new StringResourceModel("remove"), Buttons.Type.Danger) {
            @Override
            public void onSubmit(final AjaxRequestTarget target) {
                openRemoveModal(target, model);
            }

            @Override
            protected void onConfigure() {
                super.onConfigure();
            }
        };
        form.add(reject);
    }

    private void openRemoveModal(final AjaxRequestTarget target, final IModel<ServiceDataset> model) {
        Modal removeModal = new RemoveRemoteDatasetModal("dialogModal", model) {
            @Override
            protected void onSubmit(final AjaxRequestTarget target) {
                super.onSubmit(target);
                onConfirmationSubmit(target);
            }
        };
        removeModal.show(true);

        onOpenModal(target, removeModal);
    }

    protected abstract void onConfirmationSubmit(AjaxRequestTarget target);

    protected abstract void onOpenModal(AjaxRequestTarget target, Modal modal);

}
