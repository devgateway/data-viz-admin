package org.devgateway.toolkit.forms.wicket.page.lists.dataset;

import org.apache.wicket.model.IModel;
import org.devgateway.toolkit.forms.wicket.components.modal.ConfirmationModal;
import org.devgateway.toolkit.persistence.dto.ServiceDataset;

public class RemoveRemoteDatasetModal extends ConfirmationModal<ServiceDataset> {
    public RemoveRemoteDatasetModal(final String markupId, final IModel<ServiceDataset> model) {
        super(markupId, model);
    }
}
