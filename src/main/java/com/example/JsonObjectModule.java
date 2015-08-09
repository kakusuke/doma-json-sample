package com.example;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.Deserializers;
import com.fasterxml.jackson.databind.ser.Serializers;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.ParameterizedType;

/**
 * Created by kakusuke on 15/08/10.
 */
public class JsonObjectModule extends Module {
    @Override
    public String getModuleName() {
        return "JsonObjectModule";
    }

    @Override
    public Version version() {
        return Version.unknownVersion();
    }

    @Override
    public void setupModule(SetupContext ctx) {
        ctx.addSerializers(new Serializers.Base() {
            @Override
            public JsonSerializer<?> findSerializer(SerializationConfig config, JavaType type, BeanDescription beanDesc) {
                Class<?> raw = type.getRawClass();
                if (JsonObject.class.isAssignableFrom(raw)) {
                    return new JsonObjectSerializer();
                }
                return super.findSerializer(config, type, beanDesc);
            }
        });
        ctx.addDeserializers(new Deserializers.Base() {
            @Override
            public JsonDeserializer<?> findBeanDeserializer(JavaType type, DeserializationConfig config, BeanDescription beanDesc) throws JsonMappingException {
                Class<?> raw = type.getRawClass();
                if (JsonObject.class.isAssignableFrom(raw)) {
                    return new JsonObjectDeserializer(type);
                }
                return super.findBeanDeserializer(type, config, beanDesc);
            }
        });
    }

    private static class JsonObjectSerializer extends JsonSerializer<JsonObject<?>> {
        @Override
        public void serialize(JsonObject<?> value, JsonGenerator gen, SerializerProvider serializers) throws IOException, JsonProcessingException {
            gen.writeObject(value.get());
        }
    }

    private static class JsonObjectDeserializer extends JsonDeserializer<JsonObject<?>> {
        private final JavaType type;

        public JsonObjectDeserializer(JavaType type) {
            this.type = type;
        }

        @Override
        public JsonObject<?> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            @SuppressWarnings("unchecked")
            Class<? extends JsonObject<?>> raw = (Class<? extends JsonObject<?>>) type.getRawClass();
            JavaType javaType = ctxt.constructType(((ParameterizedType) raw.getGenericSuperclass()).getActualTypeArguments()[0]);
            try {
                Constructor<? extends JsonObject<?>> constructor = raw.getConstructor(javaType.getRawClass());
                Object object = p.getCodec().readValue(p, javaType);
                return constructor.newInstance(object);
            } catch (ReflectiveOperationException e) {
                throw ctxt.mappingException(e.getMessage());
            }
        }
    }
}
