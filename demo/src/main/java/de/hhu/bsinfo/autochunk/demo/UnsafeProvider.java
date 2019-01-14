package de.hhu.bsinfo.autochunk.demo;

import java.lang.reflect.Field;

import sun.misc.Unsafe;

public class UnsafeProvider {

    private static final sun.misc.Unsafe m_unsafe = initUnsafe();

    private static sun.misc.Unsafe initUnsafe() {
        try {
            Field field = Unsafe.class.getDeclaredField("theUnsafe");
            field.setAccessible(true);
            return (Unsafe) field.get(null);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static sun.misc.Unsafe getUnsafe() {
        return m_unsafe;
    }
}
