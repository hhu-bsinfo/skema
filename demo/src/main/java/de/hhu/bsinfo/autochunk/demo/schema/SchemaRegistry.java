package de.hhu.bsinfo.autochunk.demo.schema;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class SchemaRegistry {

    private static final Map<Class<?>, Schema> SCHEMAS = new HashMap<>();

    static {
        register(
                new ObjectSchema(Byte.class, Collections.singletonList("value")),
                new ObjectSchema(Character.class, Collections.singletonList("value")),
                new ObjectSchema(Short.class, Collections.singletonList("value")),
                new ObjectSchema(Integer.class, Collections.singletonList("value")),
                new ObjectSchema(Long.class, Collections.singletonList("value")),
                new ObjectSchema(Float.class, Collections.singletonList("value")),
                new ObjectSchema(Double.class, Collections.singletonList("value")),
                new ObjectSchema(Boolean.class, Collections.singletonList("value")),
                new ObjectSchema(String.class, Collections.singletonList("value")),
                new ObjectSchema(BigInteger.class, Arrays.asList("signum", "mag")),
                new ObjectSchema(BigDecimal.class, Arrays.asList("intVal", "scale")),
                new ObjectSchema(LocalDate.class, Arrays.asList("day", "month", "year")),
                new ObjectSchema(LocalTime.class, Arrays.asList("hour", "minute", "second", "nano")),
                new ObjectSchema(LocalDateTime.class, Arrays.asList("date", "time")),
                new ObjectSchema(UUID.class, Arrays.asList("mostSigBits", "leastSigBits"))
        );
    }

    public static synchronized void register(Class<?> p_class) {
        SCHEMAS.put(p_class, SchemaGenerator.generate(p_class));
    }

    private static synchronized void register(Schema... p_schema) {
        for (Schema schema : p_schema) {
            SCHEMAS.put(schema.getTarget(), schema);
        }
    }

    private static synchronized void register(Class<?> p_class, Schema p_schema) {
        SCHEMAS.put(p_class, p_schema);
    }

    private SchemaRegistry() {}

    public static Schema getSchema(Class<?> p_class) {
        Schema schema = SCHEMAS.get(p_class);

        if (schema == null) {
            throw new IllegalArgumentException(String.format("No schema for class %s was registered",
                    p_class.getCanonicalName()));
        }

        return schema;
    }
}
