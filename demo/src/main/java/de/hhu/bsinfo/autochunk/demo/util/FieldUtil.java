package de.hhu.bsinfo.autochunk.demo.util;

import java.lang.reflect.Array;

import de.hhu.bsinfo.autochunk.demo.schema.Schema;

/**
 * Utility class for retrieving and creating instances from field specifications.
 */
public final class FieldUtil {

    private static final sun.misc.Unsafe UNSAFE = UnsafeProvider.getUnsafe();

    private FieldUtil() {}

    public static Object getObject(final Object p_object, final Schema.FieldSpec p_fieldSpec) {
        try {
            return p_fieldSpec.getField().get(p_object);
        } catch (IllegalAccessException e) {
            // This should never happen since all fields have been made accessible
            throw new IllegalStateException(String.format("Field %s is not accessible", p_fieldSpec.getName()));
        }
    }

    public static Object[] getArray(final Object p_object, final Schema.FieldSpec p_fieldSpec) {
        return (Object[]) getObject(p_object, p_fieldSpec);
    }

    public static Object allocateInstance(final Schema.FieldSpec p_fieldSpec) {
        try {
            return UNSAFE.allocateInstance(p_fieldSpec.getField().getType());
        } catch (InstantiationException e) {
            throw new IllegalArgumentException(String.format("Couldn't create an instance of %s", p_fieldSpec.getField().getType().getCanonicalName()));
        }
    }

    public static Object[] allocateArray(final Schema.FieldSpec p_fieldSpec, final int p_length) {
        return (Object[]) Array.newInstance(p_fieldSpec.getField().getType().getComponentType(), p_length);
    }

    public static Object allocateComponent(final Schema.FieldSpec p_fieldSpec) {
        try {
            return UNSAFE.allocateInstance(p_fieldSpec.getField().getType().getComponentType());
        } catch (InstantiationException p_e) {
            throw new IllegalArgumentException(String.format("Couldn't create an instance of %s", p_fieldSpec.getField().getType().getCanonicalName()));
        }
    }

}
