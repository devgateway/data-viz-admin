package org.devgateway.toolkit.persistence.serializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.std.StdScalarDeserializer;
import org.devgateway.toolkit.persistence.dto.ServiceTextTranslation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Viorel Chihai
 */
public class ServiceTextTranslationDeserializer extends StdScalarDeserializer<List<ServiceTextTranslation>> {

    protected ServiceTextTranslationDeserializer(Class<List<ServiceTextTranslation>> t) {
        super(t);
    }

    ServiceTextTranslationDeserializer() {
        this(null);
    }

    @Override
    public List<ServiceTextTranslation> deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        if (jp.getCurrentToken() == JsonToken.START_OBJECT) {
            JsonDeserializer<Object> deserializer = ctxt.findRootValueDeserializer(ctxt.constructType(Map.class));
            Map<String, String> translations = (Map<String, String>) deserializer.deserialize(jp, ctxt);
            List<ServiceTextTranslation> textTranslations = new ArrayList<>();
            translations.forEach((key, value) -> {
                ServiceTextTranslation translation = new ServiceTextTranslation();
                translation.setLanguage(key);
                translation.setText(value);
                textTranslations.add(translation);
            });
            return textTranslations;
        }

        return null;
    }

}

