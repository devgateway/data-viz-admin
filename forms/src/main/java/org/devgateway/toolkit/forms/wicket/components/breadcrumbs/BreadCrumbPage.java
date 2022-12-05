package org.devgateway.toolkit.forms.wicket.components.breadcrumbs;

import org.devgateway.toolkit.forms.wicket.page.BasePage;
import org.devgateway.toolkit.forms.wicket.page.Homepage;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Used to declare a breadcrumb on a class.
 *
 * @author Viorel Chihai
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface BreadCrumbPage {

    /**
     * The upper level in breadcrumb hierarchy
     */
    Class<? extends BasePage> parent() default Homepage.class;

    boolean isRoot() default false;

    String[] params() default {};
}
