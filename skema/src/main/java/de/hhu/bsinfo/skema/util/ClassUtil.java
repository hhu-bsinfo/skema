package de.hhu.bsinfo.skema.util;

import java.util.HashMap;
import java.util.Map;

public class ClassUtil {

    private static final sun.misc.Unsafe UNSAFE = UnsafeProvider.getUnsafe();

    private static final Map<String, Class> CLASS_CACHE = new HashMap<>();

    public static <T> T allocateInstance(Class<T> p_class) {
        try {
            return (T) UNSAFE.allocateInstance(p_class);
        } catch (InstantiationException e) {
            return null;
        }
    }

    public static <T> T allocateInstance(String p_class) {
        Class cachedClass = CLASS_CACHE.get(p_class);
        if (cachedClass != null) {
            try {
                return (T) UNSAFE.allocateInstance(cachedClass);
            } catch (InstantiationException ignored) {
                return null;
            }
        }

        try {
            cachedClass = Class.forName(p_class);
        } catch (ClassNotFoundException ignored) {
            return null;
        }

        CLASS_CACHE.put(p_class, cachedClass);

        try {
            return (T) UNSAFE.allocateInstance(cachedClass);
        } catch (InstantiationException ignored) {
            return null;
        }
    }
}
