/**
 * Copyright (c) 2015 Development Gateway, Inc and others.
 * <p>
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the MIT License (MIT)
 * which accompanies this distribution, and is available at
 * https://opensource.org/licenses/MIT
 * <p>
 * Contributors:
 * Development Gateway - initial API and implementation
 */
/**
 *
 */
package org.devgateway.toolkit.forms.exceptions;

/**
 * @author vchihai
 */
public class NullServiceEntityServiceException extends RuntimeException {
    private static final long serialVersionUID = 7516874812755335131L;

    public NullServiceEntityServiceException() {
        super("serviceEntityService is null! Please set the serviceEntityService in your constructor");
    }

}
