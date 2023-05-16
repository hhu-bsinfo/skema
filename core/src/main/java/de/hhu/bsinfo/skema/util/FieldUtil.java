package de.hhu.bsinfo.skema.util;

import java.lang.reflect.Array;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import de.hhu.bsinfo.skema.schema.Schema;
import de.hhu.bsinfo.skema.schema.SchemaRegistry;

/**
 * Utility class for retrieving and creating instances from field specifications.
 */
@SuppressWarnings("sunapi")
public final class FieldUtil {

    private static final sun.misc.Unsafe UNSAFE = UnsafeProvider.getUnsafe();

    public static final String ARRAY_LENGTH_NAME = "length";

    private FieldUtil() {}

    public static Object getObject(final Object instance, final Schema.FieldSpec fieldSpec) {
        return UNSAFE.getObject(instance, fieldSpec.getOffset());
    }

    public static Object[] getArray(final Object instance, final Schema.FieldSpec fieldSpec) {
        return (Object[]) getObject(instance, fieldSpec);
    }

    public static Object allocateInstance(final Schema.FieldSpec fieldSpec) {
        try {
            return UNSAFE.allocateInstance(fieldSpec.getType());
        } catch (InstantiationException e) {
            throw new IllegalArgumentException(String.format("Couldn't create an instance of %s", fieldSpec.getType().getCanonicalName()));
        }
    }

    public static Object[] allocateArray(final Schema.FieldSpec fieldSpec, final int length) {
        return (Object[]) Array.newInstance(fieldSpec.getType().getComponentType(), length);
    }

    public static Object allocateComponent(final Schema.FieldSpec fieldSpec) {
        try {
            return UNSAFE.allocateInstance(fieldSpec.getType().getComponentType());
        } catch (InstantiationException p_e) {
            throw new IllegalArgumentException(String.format("Couldn't create an instance of %s", fieldSpec.getType().getCanonicalName()));
        }
    }

    public static Object getOrAllocateObject(final Object instance, final Schema.FieldSpec fieldSpec) {
        Object object = getObject(instance, fieldSpec);
        if (object == null) {
            object = allocateInstance(fieldSpec);
            UNSAFE.putObject(instance, fieldSpec.getOffset(), object);
        }
        return object;
    }

    public static Object[] getOrAllocateArray(final Object instance, final Schema.FieldSpec fieldSpec, final int length) {
        Object[] object = getArray(instance, fieldSpec);
        if (object == null) {
            object = allocateArray(fieldSpec, length);
            UNSAFE.putObject(instance, fieldSpec.getOffset(), object);
        }
        return object;
    }

    public static Object getOrAllocateComponent(final Object[] array, final Schema.FieldSpec fieldSpec, final int index) {
        Object object = array[index];
        if (object == null) {
            object = allocateComponent(fieldSpec);
            array[index] = object;
        }
        return object;
    }

    public static Object randomArray(final Schema.FieldSpec fieldSpec, final int length, final Random random) {
        if (!fieldSpec.isArray() || fieldSpec.getFieldType() == FieldType.OBJECT_ARRAY) {
            return null;
        }

        // Create an array and calculate its size in bytes
        Object array = Array.newInstance(fieldSpec.getType().getComponentType(), length);
        int size = SizeUtil.getArraySize(length, fieldSpec);

        // Create an array containing random bytes
        byte[] randomBytes = new byte[size];
        random.nextBytes(randomBytes);

        // Copy random bytes into target array
        UNSAFE.copyMemory(randomBytes, 0, array, fieldSpec.getFieldType().getBaseOffset(), size);

        return array;
    }

    public static Object randomEnum(final Schema.FieldSpec fieldSpec, final Random random) {
        return randomEnum(fieldSpec.getType(), random);
    }

    public static Object randomEnum(final Class<?> clazz, final Random random) {
        Schema schema = SchemaRegistry.getSchema(clazz);
        return schema.getEnumConstant(random.nextInt(schema.getEnumCount()));
    }
}
