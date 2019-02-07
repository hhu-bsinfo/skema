package de.hhu.bsinfo.skema.scheme;

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

public final class SchemeRegistry {

    private static final Map<Class<?>, Scheme> SCHEMAS = new HashMap<>();

    static {
        register(
                new ObjectScheme(Byte.class, Collections.singletonList("value")),
                new ObjectScheme(Character.class, Collections.singletonList("value")),
                new ObjectScheme(Short.class, Collections.singletonList("value")),
                new ObjectScheme(Integer.class, Collections.singletonList("value")),
                new ObjectScheme(Long.class, Collections.singletonList("value")),
                new ObjectScheme(Float.class, Collections.singletonList("value")),
                new ObjectScheme(Double.class, Collections.singletonList("value")),
                new ObjectScheme(Boolean.class, Collections.singletonList("value")),
                new ObjectScheme(String.class, Collections.singletonList("value")),
                new ObjectScheme(BigInteger.class, Arrays.asList("signum", "mag")),
                new ObjectScheme(BigDecimal.class, Arrays.asList("intVal", "scale")),
                new ObjectScheme(LocalDate.class, Arrays.asList("day", "month", "year")),
                new ObjectScheme(LocalTime.class, Arrays.asList("hour", "minute", "second", "nano")),
                new ObjectScheme(LocalDateTime.class, Arrays.asList("date", "time")),
                new ObjectScheme(UUID.class, Arrays.asList("mostSigBits", "leastSigBits"))
        );
    }

    public static synchronized void register(Class<?> p_class) {
        SCHEMAS.put(p_class, SchemeGenerator.generate(p_class));
    }

    private static synchronized void register(Scheme... p_scheme) {
        for (Scheme scheme : p_scheme) {
            SCHEMAS.put(scheme.getTarget(), scheme);
        }
    }

    private static synchronized void register(Class<?> p_class, Scheme p_scheme) {
        SCHEMAS.put(p_class, p_scheme);
    }

    private SchemeRegistry() {}

    public static Scheme getSchema(Class<?> p_class) {
        Scheme scheme = SCHEMAS.get(p_class);

        if (scheme == null) {
            throw new IllegalArgumentException(String.format("No scheme for class %s was registered",
                    p_class.getCanonicalName()));
        }

        return scheme;
    }
}
