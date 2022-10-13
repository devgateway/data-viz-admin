package org.devgateway.toolkit.persistence.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.devgateway.toolkit.persistence.dto.ServiceTextTranslation;

import java.io.IOException;
import java.util.List;

/**
 * @author Viorel Chihai
 */
public class ServiceTextTranslationSerializer extends StdSerializer<List<ServiceTextTranslation>> {

    protected ServiceTextTranslationSerializer(Class<List<ServiceTextTranslation>> t) {
        super(t);
    }

    ServiceTextTranslationSerializer() {
        this(null);
    }

    @Override
    public void serialize(final List<ServiceTextTranslation> localeTexts, final JsonGenerator jsonGenerator, final SerializerProvider serializerProvider) throws IOException {

        jsonGenerator.writeStartObject();
        for (ServiceTextTranslation localeText : localeTexts) {
            jsonGenerator.writeStringField(localeText.getLanguage(), localeText.getText());
        }
        jsonGenerator.writeEndObject();
    }

}

