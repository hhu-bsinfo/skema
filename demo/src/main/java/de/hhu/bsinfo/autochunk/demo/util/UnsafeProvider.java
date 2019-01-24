package de.hhu.bsinfo.autochunk.demo.util;

import java.lang.reflect.Field;

import sun.misc.Unsafe;

/**
 * Utility class for accessing sun.misc.Unsafe using Reflection.
 */
public final class UnsafeProvider {

    private static final sun.misc.Unsafe UNSAFE = initUnsafe();

    private UnsafeProvider() {}

    private static sun.misc.Unsafe initUnsafe() {
        try {
            Field field = Unsafe.class.getDeclaredField("theUnsafe");
            field.setAccessible(true);
            return (Unsafe) field.get(null);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Returns an instance of sun.misc.Unsafe.
     *
     * @return An instance of sun.misc.Unsafe.
     */
    public static sun.misc.Unsafe getUnsafe() {
        return UNSAFE;
    }
}
