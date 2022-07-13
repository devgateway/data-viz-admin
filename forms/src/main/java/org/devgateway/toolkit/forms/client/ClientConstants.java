package org.devgateway.toolkit.forms.client;

public class ClientConstants {

    public static final class JobStatus {
        public static final String COMPLETED = "COMPLETED";
        public static final String ERROR = "ERROR";
    }

    public final static String PATH_HEALTH = "/actuator/health";
    public final static String PATH_JOBS = "/admin/jobs";
    public final static String PATH_DATASETS = "/admin/datasets";
    public final static String PATH_EXTERNAL = "/external";

    public final static String EXTERNAL_ID_PREFIX = "tcdi-";

}
