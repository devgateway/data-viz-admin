package org.devgateway.toolkit.persistence.repository.norepository;

import org.apache.commons.lang3.StringUtils;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

/**
 * @author Nadejda Mandrescu
 */
public final class SpecificationUtils {
    private SpecificationUtils() {
    }

    public static Expression<String> ignoreCaseLikeValue(final CriteriaBuilder cb, final String value) {
        return cb.lower(cb.concat(cb.concat(cb.literal("%"), value), "%"));
    }

    public static Predicate equalIgnoreCaseValue(final Root<?> root, final CriteriaBuilder cb,
            final String propertyName, final String value) {
        return cb.equal(cb.lower(root.get(propertyName)), StringUtils.lowerCase(value));
    }

}
