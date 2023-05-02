package de.hhu.bsinfo.skema.util;

import de.hhu.bsinfo.skema.schema.Schema;
import de.hhu.bsinfo.skema.schema.SchemaRegistry;

/**
 * Utility class for calculating the size of fields within various objects.
 */
@SuppressWarnings("sunapi")
public final class SizeUtil {

    private static final sun.misc.Unsafe UNSAFE = UnsafeProvider.getUnsafe();

    // All array base offsets are equal to 16 and the
    // length field is right in front of them (for now)
    public static final long ARRAY_LENGTH_OFFSET = 12;

    private static final int LENGHT_FIELD_BYTES = Integer.BYTES;

    private static final int NO_SIZE = 0;

    private static final SizeFunction[] SIZE_FUNCTIONS = new SizeFunction[FieldType.values().length];

    static {
        SIZE_FUNCTIONS[FieldType.OBJECT.getId()] = SizeUtil::getObjectSize;
        SIZE_FUNCTIONS[FieldType.BYTE.getId()] = ((object, fieldSpec) -> Byte.BYTES);
        SIZE_FUNCTIONS[FieldType.CHAR.getId()] = ((object, fieldSpec) -> Character.BYTES);
        SIZE_FUNCTIONS[FieldType.SHORT.getId()] = ((object, fieldSpec) -> Short.BYTES);
        SIZE_FUNCTIONS[FieldType.INT.getId()] = ((object, fieldSpec) -> Integer.BYTES);
        SIZE_FUNCTIONS[FieldType.LONG.getId()] = ((object, fieldSpec) -> Long.BYTES);
        SIZE_FUNCTIONS[FieldType.FLOAT.getId()] = ((object, fieldSpec) -> Float.BYTES);
        SIZE_FUNCTIONS[FieldType.DOUBLE.getId()] = ((object, fieldSpec) -> Double.BYTES);
        SIZE_FUNCTIONS[FieldType.BOOLEAN.getId()] = ((object, fieldSpec) -> Byte.BYTES);
        SIZE_FUNCTIONS[FieldType.CHAR_ARRAY.getId()] = SizeUtil::getArraySize;
        SIZE_FUNCTIONS[FieldType.BYTE_ARRAY.getId()] = SizeUtil::getArraySize;
        SIZE_FUNCTIONS[FieldType.SHORT_ARRAY.getId()] = SizeUtil::getArraySize;
        SIZE_FUNCTIONS[FieldType.INT_ARRAY.getId()] = SizeUtil::getArraySize;
        SIZE_FUNCTIONS[FieldType.LONG_ARRAY.getId()] = SizeUtil::getArraySize;
        SIZE_FUNCTIONS[FieldType.FLOAT_ARRAY.getId()] = SizeUtil::getArraySize;
        SIZE_FUNCTIONS[FieldType.DOUBLE_ARRAY.getId()] = SizeUtil::getArraySize;
        SIZE_FUNCTIONS[FieldType.BOOLEAN_ARRAY.getId()] = SizeUtil::getArraySize;
        SIZE_FUNCTIONS[FieldType.OBJECT_ARRAY.getId()] = SizeUtil::getObjectArraySize;
        SIZE_FUNCTIONS[FieldType.ENUM.getId()] = ((object, fieldSpec) -> Integer.BYTES);
        SIZE_FUNCTIONS[FieldType.LENGTH.getId()] = ((object, fieldSpec) -> Integer.BYTES);
    }

    private SizeUtil() {}

    public static int getObjectArraySize(final Object instance, final Schema.FieldSpec fieldSpec) {
        Object[] array = (Object[]) FieldUtil.getObject(instance, fieldSpec);
        Schema schema = SchemaRegistry.getSchema(array[0].getClass());

        if (schema.isConstant()) {
            return array.length * schema.getConstantSize();
        }

        int size = 0;
        for (Object object : array) {
            size += schema.getSize(object);
        }
        return size;
    }

    /**
     * Returns the size of an object in bytes.
     *
     * @param instance The parent object.
     * @param fieldSpec The object's field specification.
     * @return The size in bytes.
     */
    public static int getObjectSize(final Object instance, final Schema.FieldSpec fieldSpec) {
        Object object = FieldUtil.getObject(instance, fieldSpec);
        Schema schema = SchemaRegistry.getSchema(object.getClass());
        return schema.getSize(object);
    }

    /**
     * Returns the size of an array in bytes including the length field.
     *
     * @param instance The array.
     * @param fieldSpec The field specification.
     * @return The array's size in bytes.
     */
    public static int getArraySize(final Object instance, final Schema.FieldSpec fieldSpec) {
        Object array = FieldUtil.getObject(instance, fieldSpec);
        return getArraySize(getArrayLength(array), fieldSpec);
    }

    public static int getArrayLength(final Object array) {
        return UNSAFE.getInt(array, ARRAY_LENGTH_OFFSET);
    }

    /**
     * Returns the size of an array in bytes including the length field.
     *
     * @param length The array's length.
     * @param fieldSpec The array's field specification.
     * @return The array's size in bytes.
     */
    public static int getArraySize(final int length, final Schema.FieldSpec fieldSpec) {
        switch (fieldSpec.getFieldType()) {
            case BYTE_ARRAY:
                return length * Byte.BYTES;
            case CHAR_ARRAY:
                return length * Character.BYTES;
            case SHORT_ARRAY:
                return length * Short.BYTES;
            case INT_ARRAY:
                return length * Integer.BYTES;
            case LONG_ARRAY:
                return length * Long.BYTES;
            case FLOAT_ARRAY:
                return length * Float.BYTES;
            case DOUBLE_ARRAY:
                return length * Double.BYTES;
            case BOOLEAN_ARRAY:
                return length * Byte.BYTES;
            default:
                throw new IllegalArgumentException(String.format("%s is not an array type", fieldSpec.getFieldType()));
        }
    }

    public static int sizeOf(final Object instance, final Schema.FieldSpec fieldSpec) {
        return SIZE_FUNCTIONS[fieldSpec.getFieldType().getId()].sizeOf(instance, fieldSpec);
    }

    public static int constantSizeOf(final Schema.FieldSpec fieldSpec) {
        if (!fieldSpec.hasConstantSize()) {
            return NO_SIZE;
        }

        return sizeOf(null, fieldSpec);
    }

}
