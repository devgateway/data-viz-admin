package org.devgateway.toolkit.forms.client;

import org.devgateway.toolkit.persistence.dto.ServiceEntity;
import org.glassfish.jersey.client.JerseyClient;
import org.glassfish.jersey.client.JerseyClientBuilder;
import org.glassfish.jersey.media.multipart.MultiPartFeature;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import java.util.List;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.Response.Status.Family.SUCCESSFUL;
import static org.devgateway.toolkit.forms.client.ClientConstants.PATH_HEALTH;

public abstract class ServiceEntityClient<T extends ServiceEntity> {

    protected final JerseyClient client;

    protected final String baseUrl;

    public ServiceEntityClient(final String baseUrl) {
        this.baseUrl = baseUrl;
        this.client = JerseyClientBuilder.createClient();
        this.client.register(MultiPartFeature.class);
    }

    public abstract String getEntitiesPath();

    public List<T> findAll() {
        if (isUp()) {
            Response response = client.target(baseUrl)
                    .path(getEntitiesPath())
                    .request(APPLICATION_JSON).get();

            if (response.getStatusInfo().getFamily() == SUCCESSFUL) {
                return response.readEntity(getGenericListType());
            }
            return null;
        }

        throw new RuntimeException(("Service is not up"));
    }

    public T findOne(final Long id) {
        if (isUp()) {
            Response response = client.target(baseUrl)
                    .path(getEntitiesPath())
                    .path(id.toString())
                    .request(APPLICATION_JSON).get();

            if (response.getStatusInfo().getFamily() == SUCCESSFUL) {
                return response.readEntity(getGenericType());
            }

            return null;
        }

        throw new RuntimeException(("Service is not up"));
    }

    public void save(final T entity) {
        if (isUp()) {
            Response measuresResponse = client.target(baseUrl)
                    .path(getEntitiesPath())
                    .request(APPLICATION_JSON)
                    .post(Entity.entity(entity, APPLICATION_JSON));

            if (measuresResponse.getStatusInfo().getFamily() != SUCCESSFUL) {
                throw new RuntimeException("Error in updating the entity");
            }
        } else {
            throw new RuntimeException(("Service is not up"));
        }
    }

    public void update(final T entity) {
        if (isUp()) {
            Response measuresResponse = client.target(baseUrl)
                    .path(getEntitiesPath())
                    .path(entity.getId().toString())
                    .request(APPLICATION_JSON)
                    .put(Entity.entity(entity, APPLICATION_JSON));

            if (measuresResponse.getStatusInfo().getFamily() != SUCCESSFUL) {
                throw new RuntimeException("Error in updating the entity");
            }
        } else {
            throw new RuntimeException(("Service is not up"));
        }
    }

    public void delete(final T entity) {
        if (isUp()) {
            Response response = client.target(baseUrl)
                    .path(getEntitiesPath())
                    .path(entity.getId().toString())
                    .request(APPLICATION_JSON)
                    .delete();

            if (response.getStatusInfo().getFamily() != SUCCESSFUL) {
                throw new RuntimeException("Error in deleting the entity");
            }
        } else {
            throw new RuntimeException(("Service is not up"));
        }
    }

    public boolean isUp() {
        Response healthResponse = client.target(baseUrl).path(PATH_HEALTH)
                .request(APPLICATION_JSON).get();

        if (healthResponse.getStatusInfo().getFamily() == SUCCESSFUL) {
            return healthResponse.readEntity(ServiceHealthStatus.class).isUp();
        }

        return false;
    }

    protected abstract GenericType<T> getGenericType();

    protected abstract GenericType<List<T>> getGenericListType();

}
