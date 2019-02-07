package de.hhu.bsinfo.autochunk.demo.util;

public class Constants {
    private static final sun.misc.Unsafe UNSAFE = UnsafeProvider.getUnsafe();
    public static final long BYTE_ARRAY_OFFSET = UNSAFE.arrayBaseOffset(byte[].class);
    public static final long CHAR_ARRAY_OFFSET = UNSAFE.arrayBaseOffset(char[].class);
    public static final long SHORT_ARRAY_OFFSET = UNSAFE.arrayBaseOffset(long[].class);
    public static final long INT_ARRAY_OFFSET = UNSAFE.arrayBaseOffset(int[].class);
    public static final long LONG_ARRAY_OFFSET = UNSAFE.arrayBaseOffset(long[].class);
    public static final long FLOAT_ARRAY_OFFSET = UNSAFE.arrayBaseOffset(float[].class);
    public static final long DOUBLE_ARRAY_OFFSET = UNSAFE.arrayBaseOffset(double[].class);
    public static final long BOOLEAN_ARRAY_OFFSET = UNSAFE.arrayBaseOffset(boolean[].class);
    public static final long OBJECT_ARRAY_OFFSET = UNSAFE.arrayBaseOffset(Object[].class);
    public static final int REFERENCE_SIZE = UNSAFE.arrayIndexScale(Object[].class);
    public static final byte TRUE = 1;
    public static final byte FALSE = 0;
}
