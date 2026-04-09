package org.devgateway.toolkit.forms.wicket.page.edit;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.concurrent.atomic.AtomicBoolean;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.orm.ObjectOptimisticLockingFailureException;

/**
 * Unit tests for {@link SaveEntityErrorHandler}.
 *
 * Verifies that any unexpected exception thrown during a save results in the
 * generic "failed" modal action being triggered (instead of propagating up to
 * Wicket's error-page renderer), while an
 * ObjectOptimisticLockingFailureException triggers the "save failed" modal.
 *
 * No Wicket application or Spring context is required: SaveEntityErrorHandler
 * accepts plain Runnable callbacks, so the tests track invocations with simple
 * AtomicBoolean flags.
 */
public class AbstractEditPageSaveExceptionTest {

    private AtomicBoolean failedModalShown;
    private AtomicBoolean saveFailedModalShown;
    private SaveEntityErrorHandler handler;

    @BeforeEach
    void setUp() {
        failedModalShown     = new AtomicBoolean(false);
        saveFailedModalShown = new AtomicBoolean(false);
        handler = new SaveEntityErrorHandler(
            () -> failedModalShown.set(true),
            () -> saveFailedModalShown.set(true)
        );
    }

    // -----------------------------------------------------------------------
    // Generic / unexpected exception → failedModal
    // -----------------------------------------------------------------------

    @Test
    void genericRuntimeException_triggersFailedModal() {
        handler.handle(new RuntimeException("db connection lost"));

        assertTrue(failedModalShown.get(),     "failedModal should be shown");
        assertFalse(saveFailedModalShown.get(), "saveFailedModal must not be shown");
    }

    @Test
    void genericCheckedException_triggersFailedModal() {
        handler.handle(new Exception("unexpected checked exception"));

        assertTrue(failedModalShown.get());
        assertFalse(saveFailedModalShown.get());
    }

    @Test
    void illegalStateException_triggersFailedModal() {
        handler.handle(new IllegalStateException("bad state during save"));

        assertTrue(failedModalShown.get());
        assertFalse(saveFailedModalShown.get());
    }

    @Test
    void nullPointerException_triggersFailedModal() {
        handler.handle(new NullPointerException("null entity field"));

        assertTrue(failedModalShown.get());
        assertFalse(saveFailedModalShown.get());
    }

    // -----------------------------------------------------------------------
    // ObjectOptimisticLockingFailureException → saveFailedModal
    // -----------------------------------------------------------------------

    @Test
    void optimisticLockingException_triggersSaveFailedModal() {
        handler.handle(new ObjectOptimisticLockingFailureException(Object.class, 1L));

        assertTrue(saveFailedModalShown.get(),  "saveFailedModal should be shown");
        assertFalse(failedModalShown.get(),     "failedModal must not be shown");
    }

    @Test
    void optimisticLockingException_doesNotTriggerGenericFailedModal() {
        handler.handle(new ObjectOptimisticLockingFailureException(Object.class, 99L));

        assertFalse(failedModalShown.get());
    }

    // -----------------------------------------------------------------------
    // Exceptions must never propagate — otherwise Wicket shows an error page
    // -----------------------------------------------------------------------

    @Test
    void handle_neverThrows_forGenericException() {
        // If handle() re-throws, this test fails — it must not propagate.
        handler.handle(new RuntimeException("should be swallowed"));
    }

    @Test
    void handle_neverThrows_forOptimisticLockingException() {
        handler.handle(new ObjectOptimisticLockingFailureException(Object.class, 2L));
    }
}
