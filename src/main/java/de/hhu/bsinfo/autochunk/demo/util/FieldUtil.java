package de.hhu.bsinfo.autochunk.demo.util;

import java.lang.reflect.Array;
import java.lang.reflect.Field;

import de.hhu.bsinfo.autochunk.demo.schema.Schema;

/**
 * Utility class for retrieving and creating instances from field specifications.
 */
public final class FieldUtil {

    private static final sun.misc.Unsafe UNSAFE = UnsafeProvider.getUnsafe();

    public static final String ARRAY_LENGTH_NAME = "length";

    private FieldUtil() {}

    public static Object getObject(final Object p_object, final Schema.FieldSpec p_fieldSpec) {
        return UNSAFE.getObject(p_object, p_fieldSpec.getOffset());
    }

    public static Object[] getArray(final Object p_object, final Schema.FieldSpec p_fieldSpec) {
        return (Object[]) getObject(p_object, p_fieldSpec);
    }

    public static Object allocateInstance(final Schema.FieldSpec p_fieldSpec) {
        try {
            return UNSAFE.allocateInstance(p_fieldSpec.getType());
        } catch (InstantiationException e) {
            throw new IllegalArgumentException(String.format("Couldn't create an instance of %s", p_fieldSpec.getType().getCanonicalName()));
        }
    }

    public static Object[] allocateArray(final Schema.FieldSpec p_fieldSpec, final int p_length) {
        return (Object[]) Array.newInstance(p_fieldSpec.getType().getComponentType(), p_length);
    }

    public static Object allocateComponent(final Schema.FieldSpec p_fieldSpec) {
        try {
            return UNSAFE.allocateInstance(p_fieldSpec.getType().getComponentType());
        } catch (InstantiationException p_e) {
            throw new IllegalArgumentException(String.format("Couldn't create an instance of %s", p_fieldSpec.getType().getCanonicalName()));
        }
    }

    public static Object getOrAllocateObject(final Object p_object, final Schema.FieldSpec p_fieldSpec) {
        Object object = getObject(p_object, p_fieldSpec);
        if (object == null) {
            object = allocateInstance(p_fieldSpec);
            UNSAFE.putObject(p_object, p_fieldSpec.getOffset(), object);
        }
        return object;
    }

    public static Object[] getOrAllocateArray(final Object p_object, final Schema.FieldSpec p_fieldSpec, final int p_length) {
        Object[] object = getArray(p_object, p_fieldSpec);
        if (object == null) {
            object = allocateArray(p_fieldSpec, p_length);
            UNSAFE.putObject(p_object, p_fieldSpec.getOffset(), object);
        }
        return object;
    }

    public static Object getOrAllocateComponent(final Object[] p_array, final Schema.FieldSpec p_fieldSpec, final int p_index) {
        Object object = p_array[p_index];
        if (object == null) {
            object = allocateComponent(p_fieldSpec);
            p_array[p_index] = object;
        }
        return object;
    }
}
