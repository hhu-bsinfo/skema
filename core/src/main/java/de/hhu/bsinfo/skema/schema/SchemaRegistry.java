package de.hhu.bsinfo.skema.schema;

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
import java.util.concurrent.atomic.AtomicInteger;

public final class SchemaRegistry {

    private static final Map<Class<?>, Schema> SCHEMAS = new HashMap<>();

    private static boolean isAutoRegistrationEnabled = false;

    private static final Class<?>[] CLASS_MAP = new Class[Short.MAX_VALUE];

    private static final Map<Class<?>, Short> ID_MAP = new HashMap<>();

    private static final AtomicInteger ID_COUNTER = new AtomicInteger(1);

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

    public static synchronized Schema register(Class<?> clazz) {
        Schema schema = SchemaGenerator.generate(clazz);
        register(schema);
        return schema;
    }

    private static synchronized void register(Schema... schemas) {
        short identifier;
        for (Schema schema : schemas) {
            SCHEMAS.put(schema.getTarget(), schema);
            identifier = (short) ID_COUNTER.getAndIncrement();
            CLASS_MAP[identifier] = schema.getTarget();
            ID_MAP.put(schema.getTarget(), identifier);
        }
    }

    public static void enableAutoRegistration() {
        isAutoRegistrationEnabled = true;
    }

    public static void disableAutoRegistration() {
        isAutoRegistrationEnabled = false;
    }

    public static <T> Class<T> resolveClass(final short identifier) {
        return (Class<T>)CLASS_MAP[identifier];
    }

    public static short resolveIdentifier(final Class<?> clazz) {
        return ID_MAP.get(clazz);
    }

    private SchemaRegistry() {}

    public static Schema getSchema(Class<?> clazz) {
        Schema schema = SCHEMAS.get(clazz);

        if (schema != null) {
            return schema;
        }

        if (isAutoRegistrationEnabled) {
            schema = register(clazz);
        }

        if (schema == null) {
            throw new IllegalArgumentException(String.format("No schema for class %s was registered",
                    clazz.getCanonicalName()));
        }

        return schema;
    }
}
