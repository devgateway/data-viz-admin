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

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public final class DBConstants {

    private DBConstants() {

    }

    public static final class Status {
        public static final String DRAFT = "DRAFT";
        public static final String SAVED = "SAVED";
        public static final String PUBLISHING = "PUBLISHING";
        public static final String PUBLISHED = "PUBLISHED";
        public static final String ERROR_IN_PUBLISHING = "ERROR_IN_PUBLISHING";
        public static final String DELETED = "DELETED";

        public static final String UNPUBLISHING = "UNPUBLISHING";

        public static final String ERROR_IN_UNPUBLISHING = "ERROR_IN_UNPUBLISHING";


        public static final String[] ALL = {DRAFT, SAVED, PUBLISHING, PUBLISHED, ERROR_IN_PUBLISHING, UNPUBLISHING,
                ERROR_IN_UNPUBLISHING, DELETED};
        public static final List<String> ALL_LIST = Collections.unmodifiableList(Arrays.asList(ALL));
    }

    public static final int MAX_DEFAULT_TEXT_LENGTH = 32000;
    public static final int STD_DEFAULT_TEXT_LENGTH = 255;
    public static final int MAX_DEFAULT_TEXT_LENGTH_ONE_LINE = 3000;
    public static final int MAX_DEFAULT_TEXT_AREA = 10000;

}
