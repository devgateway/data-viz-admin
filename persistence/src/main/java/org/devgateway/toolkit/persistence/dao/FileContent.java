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
package org.devgateway.toolkit.persistence.dao;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Lob;

/**
 * @author idobre
 * @since 11/13/14
 * <p>
 * Entity used to store the content of uploaded files
 */

@Entity
public class FileContent extends AbstractAuditableEntity {
    private static final int LOB_LENGTH = 10000000;

    @Lob
    @Column(length = LOB_LENGTH)
    @JdbcTypeCode(SqlTypes.BLOB)
    private byte[] bytes;

    public byte[] getBytes() {
        return bytes;
    }

    public void setBytes(final byte[] bytes) {
        this.bytes = bytes;
    }

    @Override
    public AbstractAuditableEntity getParent() {
        return null;
    }
}
