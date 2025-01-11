package org.hibernate.proxy;

import org.hibernate.Hibernate;


public final class HibernateProxyHelper {

        private HibernateProxyHelper() {
            // Prevent instantiation
        }

        public static Class<?> getClassWithoutInitializingProxy(Object object) {
            if (object instanceof HibernateProxy) {
                HibernateProxy proxy = (HibernateProxy)object;
                LazyInitializer li = proxy.getHibernateLazyInitializer();
                return li.getPersistentClass();
            } else {
                return object.getClass();
            }
        }
}
