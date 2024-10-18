package org.devgateway.toolkit.persistence.derby;

import org.hibernate.dialect.DerbyDialect;
import org.hibernate.boot.model.TypeContributions;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.type.descriptor.jdbc.BlobJdbcType;
import org.hibernate.type.spi.TypeConfiguration;

import static java.sql.Types.VARBINARY;

/**
 * Setup non-default derby configurations.
 *
 * @author Nadejda Mandrescu
 */
public class CustomDerbyDialect extends DerbyDialect {

    public CustomDerbyDialect() {
        super();
    }

    @Override
    public void contributeTypes(TypeContributions typeContributions, ServiceRegistry serviceRegistry) {
        super.contributeTypes(typeContributions, serviceRegistry);
        typeContributions.getTypeConfiguration().getJdbcTypeRegistry()
                .addDescriptor(VARBINARY, BlobJdbcType.BLOB_BINDING);
    }
}
