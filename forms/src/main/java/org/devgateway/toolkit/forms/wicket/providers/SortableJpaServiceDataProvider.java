/*******************************************************************************
 * Copyright (c) 2015 Development Gateway, Inc and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the MIT License (MIT)
 * which accompanies this distribution, and is available at
 * https://opensource.org/licenses/MIT
 *
 * Contributors:
 * Development Gateway - initial API and implementation
 *******************************************************************************/
package org.devgateway.toolkit.forms.wicket.providers;

import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.IFilterStateLocator;
import org.apache.wicket.extensions.markup.html.repeater.util.SortParam;
import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.devgateway.toolkit.forms.WebConstants;
import org.devgateway.toolkit.forms.models.PersistableJpaRepositoryModel;
import org.devgateway.toolkit.forms.wicket.components.table.filter.JpaFilterState;
import org.devgateway.toolkit.persistence.dao.GenericPersistable;
import org.devgateway.toolkit.persistence.service.AdminSettingsService;
import org.devgateway.toolkit.persistence.service.BaseJpaService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

import java.io.Serializable;
import java.util.Iterator;

/**
 * @author mpostelnicu
 * <p>
 * Smart generic {@link SortableDataProvider} that binds to {@link BaseJpaService}
 */
public class SortableJpaServiceDataProvider<T extends GenericPersistable & Serializable>
        extends SortableDataProvider<T, String> implements IFilterStateLocator<JpaFilterState<T>> {
    private static final long serialVersionUID = 6507887810859971417L;

    private final Integer pageSize;

    private final BaseJpaService<T> jpaService;

    private JpaFilterState<T> filterState;

    public SortableJpaServiceDataProvider(final BaseJpaService<T> jpaService) {
        this(jpaService, WebConstants.DEFAULT_PAGE_SIZE);
    }

    /**
     * Always provide a proxy jpaService here! For example one coming from a {@link SpringBean}
     *
     * @param jpaService
     */
    public SortableJpaServiceDataProvider(final BaseJpaService<T> jpaService, final Integer pageSize) {
        this.jpaService = jpaService;
        this.pageSize = pageSize;
    }

    /**
     * Translates from a {@link SortParam} to a Spring {@link Sort}
     */
    protected Sort translateSort() {
        if (getSort() == null) {
            return null;
        }

        return Sort.by(getSort().isAscending() ? Direction.ASC : Direction.DESC,
                getSort().getProperty());
    }

    /**
     * @see SortableDataProvider#iterator(long, long)
     */
    @Override
    public Iterator<? extends T> iterator(final long first, final long count) {
        int page = (int) ((double) first / getPageSize());
        final Page<T> findAll = jpaService.findAll(filterState.getSpecification(),
                translateSort() == null
                        ? PageRequest.of(page, getPageSize())
                        : PageRequest.of(page, getPageSize(), translateSort()));
        return findAll.iterator();
    }

    private Integer getPageSize() {
        return pageSize;
    }

    @Override
    public long size() {
        return jpaService.count(filterState.getSpecification());
    }

    /**
     * This ensures that the object is detached and reloaded after
     * deserialization of the page, since the
     * {@link PersistableJpaRepositoryModel} is also loadabledetachable
     */
    @Override
    public IModel<T> model(final T object) {
        return new PersistableJpaRepositoryModel<T>(object, jpaService);
    }

    @Override
    public JpaFilterState<T> getFilterState() {
        return filterState;
    }

    @Override
    public void setFilterState(final JpaFilterState<T> filterState) {
        this.filterState = filterState;
    }
}
