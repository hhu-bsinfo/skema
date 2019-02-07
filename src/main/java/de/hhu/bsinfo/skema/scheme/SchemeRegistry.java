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

    private static final Map<Class<?>, Scheme> SCHEMES = new HashMap<>();

    private static boolean m_isAutoRegistrationEnabled = false;

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

    public static synchronized Scheme register(Class<?> p_class) {
        Scheme scheme = SchemeGenerator.generate(p_class);
        SCHEMES.put(p_class, scheme);
        return scheme;
    }

    private static synchronized void register(Scheme... p_scheme) {
        for (Scheme scheme : p_scheme) {
            SCHEMES.put(scheme.getTarget(), scheme);
        }
    }

    public static void enableAutoRegistration() {
        m_isAutoRegistrationEnabled = true;
    }

    public static void disableAutoRegistration() {
        m_isAutoRegistrationEnabled = false;
    }

    private static synchronized void register(Class<?> p_class, Scheme p_scheme) {
        SCHEMES.put(p_class, p_scheme);
    }

    private SchemeRegistry() {}

    public static Scheme getSchema(Class<?> p_class) {
        Scheme scheme = SCHEMES.get(p_class);

        if (scheme != null) {
            return scheme;
        }

        if (m_isAutoRegistrationEnabled) {
            scheme = register(p_class);
        }

        if (scheme == null) {
            throw new IllegalArgumentException(String.format("No scheme for class %s was registered",
                    p_class.getCanonicalName()));
        }

        return scheme;
    }
}
