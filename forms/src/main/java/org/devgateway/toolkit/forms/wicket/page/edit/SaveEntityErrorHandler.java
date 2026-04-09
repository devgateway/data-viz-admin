package org.devgateway.toolkit.forms.wicket.page.edit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.orm.ObjectOptimisticLockingFailureException;

/**
 * Routes a save exception to the appropriate error modal action.
 *
 * <p>Accepts plain {@link Runnable} callbacks so the handler has zero
 * dependency on Wicket types, making it straightforward to unit-test without
 * a running Wicket application or Spring context.
 *
 * <p>Usage in a Wicket page:
 * <pre>{@code
 * new SaveEntityErrorHandler(
 *     () -> { failedModal.show(target);     target.add(failedModal);     },
 *     () -> { saveFailedModal.show(target); target.add(saveFailedModal); }
 * ).handle(exception);
 * }</pre>
 */
public class SaveEntityErrorHandler {

    private static final Logger logger = LoggerFactory.getLogger(
        SaveEntityErrorHandler.class
    );

    private final Runnable showFailedModal;
    private final Runnable showSaveFailedModal;

    /**
     * @param showFailedModal     action that displays the generic failure modal
     * @param showSaveFailedModal action that displays the optimistic-lock modal
     */
    public SaveEntityErrorHandler(
        final Runnable showFailedModal,
        final Runnable showSaveFailedModal
    ) {
        this.showFailedModal = showFailedModal;
        this.showSaveFailedModal = showSaveFailedModal;
    }

    /**
     * Dispatches {@code e} to the correct modal action. Never re-throws.
     *
     * @param e the exception thrown during the save operation
     */
    public void handle(final Exception e) {
        if (e instanceof ObjectOptimisticLockingFailureException) {
            showSaveFailedModal.run();
        } else {
            logger.error("Unexpected error while saving entity", e);
            showFailedModal.run();
        }
    }
}
