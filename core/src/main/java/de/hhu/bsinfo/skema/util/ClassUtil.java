package de.hhu.bsinfo.skema.util;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("sunapi")
public class ClassUtil {

    private static final sun.misc.Unsafe UNSAFE = UnsafeProvider.getUnsafe();

    private static final Map<String, Class> CLASS_CACHE = new HashMap<>();

    public static <T> T allocateInstance(Class<T> clazz) {
        try {
            return (T) UNSAFE.allocateInstance(clazz);
        } catch (InstantiationException e) {
            return null;
        }
    }

    public static <T> T allocateInstance(String clazz) {
        Class cachedClass = CLASS_CACHE.get(clazz);
        if (cachedClass != null) {
            try {
                return (T) UNSAFE.allocateInstance(cachedClass);
            } catch (InstantiationException ignored) {
                return null;
            }
        }

        try {
            cachedClass = Class.forName(clazz);
        } catch (ClassNotFoundException ignored) {
            return null;
        }

        CLASS_CACHE.put(clazz, cachedClass);

        try {
            return (T) UNSAFE.allocateInstance(cachedClass);
        } catch (InstantiationException ignored) {
            return null;
        }
    }
}
