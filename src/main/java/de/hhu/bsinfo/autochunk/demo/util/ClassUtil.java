package de.hhu.bsinfo.autochunk.demo.util;

public class ClassUtil {

    private static final sun.misc.Unsafe UNSAFE = UnsafeProvider.getUnsafe();

    public static <T> T allocateInstance(Class<T> p_class) {
        try {
            return (T) UNSAFE.allocateInstance(p_class);
        } catch (InstantiationException e) {
            return null;
        }
    }
}
