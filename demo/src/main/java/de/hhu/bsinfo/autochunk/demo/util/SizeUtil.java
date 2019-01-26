package de.hhu.bsinfo.autochunk.demo.util;

import java.lang.reflect.Array;
import java.util.Arrays;

import de.hhu.bsinfo.autochunk.demo.schema.Schema;
import de.hhu.bsinfo.autochunk.demo.SchemaSerializer;

/**
 * Utility class for calculating the size of fields within various objects.
 */
public final class SizeUtil {

    private static final sun.misc.Unsafe UNSAFE = UnsafeProvider.getUnsafe();

    // All array base offsets are equal to 16 and the
    // length field is right in front of them (for now)
    private static final long ARRAY_LENGTH_OFFSET = 12;

    private static final int LENGHT_FIELD_BYTES = Integer.BYTES;

    private static final int NO_SIZE = 0;

    private static final SizeFunction[] SIZE_FUNCTIONS = new SizeFunction[FieldType.values().length];

    static {
        SIZE_FUNCTIONS[FieldType.OBJECT.getId()] = SizeUtil::getObjectSize;
        SIZE_FUNCTIONS[FieldType.BYTE.getId()] = ((p_object, p_fieldSpec) -> Byte.BYTES);
        SIZE_FUNCTIONS[FieldType.CHAR.getId()] = ((p_object, p_fieldSpec) -> Character.BYTES);
        SIZE_FUNCTIONS[FieldType.SHORT.getId()] = ((p_object, p_fieldSpec) -> Short.BYTES);
        SIZE_FUNCTIONS[FieldType.INT.getId()] = ((p_object, p_fieldSpec) -> Integer.BYTES);
        SIZE_FUNCTIONS[FieldType.LONG.getId()] = ((p_object, p_fieldSpec) -> Long.BYTES);
        SIZE_FUNCTIONS[FieldType.FLOAT.getId()] = ((p_object, p_fieldSpec) -> Float.BYTES);
        SIZE_FUNCTIONS[FieldType.DOUBLE.getId()] = ((p_object, p_fieldSpec) -> Double.BYTES);
        SIZE_FUNCTIONS[FieldType.BOOLEAN.getId()] = ((p_object, p_fieldSpec) -> Byte.BYTES);
        SIZE_FUNCTIONS[FieldType.CHAR_ARRAY.getId()] = SizeUtil::getArraySize;
        SIZE_FUNCTIONS[FieldType.BYTE_ARRAY.getId()] = SizeUtil::getArraySize;
        SIZE_FUNCTIONS[FieldType.SHORT_ARRAY.getId()] = SizeUtil::getArraySize;
        SIZE_FUNCTIONS[FieldType.INT_ARRAY.getId()] = SizeUtil::getArraySize;
        SIZE_FUNCTIONS[FieldType.LONG_ARRAY.getId()] = SizeUtil::getArraySize;
        SIZE_FUNCTIONS[FieldType.FLOAT_ARRAY.getId()] = SizeUtil::getArraySize;
        SIZE_FUNCTIONS[FieldType.DOUBLE_ARRAY.getId()] = SizeUtil::getArraySize;
        SIZE_FUNCTIONS[FieldType.BOOLEAN_ARRAY.getId()] = SizeUtil::getArraySize;
        SIZE_FUNCTIONS[FieldType.OBJECT_ARRAY.getId()] = SizeUtil::getObjectArraySize;
    }

    private SizeUtil() {}

    public static int getObjectArraySize(final Object p_object, final Schema.FieldSpec p_fieldSpec) {
        Object[] array = (Object[]) FieldUtil.getObject(p_object, p_fieldSpec);
        Schema schema = SchemaSerializer.getSchema(array[0].getClass());

        if (schema.isConstant()) {
            return array.length * schema.getConstantSize() + LENGHT_FIELD_BYTES;
        }

        int size = 0;
        for (Object object : array) {
            size += schema.getSize(object);
        }
        return size + LENGHT_FIELD_BYTES;
    }

    /**
     * Returns the size of an object in bytes.
     *
     * @param p_object The parent object.
     * @param p_fieldSpec The object's field specification.
     * @return The size in bytes.
     */
    public static int getObjectSize(final Object p_object, final Schema.FieldSpec p_fieldSpec) {
        Object object = FieldUtil.getObject(p_object, p_fieldSpec);
        Schema schema = SchemaSerializer.getSchema(object.getClass());
        return schema.getSize(object);
    }

    /**
     * Returns the size of an array in bytes including the length field.
     *
     * @param p_object The array.
     * @param p_fieldSpec The field specification.
     * @return The array's size in bytes.
     */
    public static int getArraySize(final Object p_object, final Schema.FieldSpec p_fieldSpec) {
        Object array = FieldUtil.getObject(p_object, p_fieldSpec);
        return getArraySize(getArrayLength(array), p_fieldSpec);
    }

    public static int getArrayLength(final Object p_array) {
        return UNSAFE.getInt(p_array, ARRAY_LENGTH_OFFSET);
    }

    /**
     * Returns the size of an array in bytes including the length field.
     *
     * @param p_length The array's length.
     * @param p_fieldSpec The array's field specification.
     * @return The array's size in bytes.
     */
    public static int getArraySize(final int p_length, final Schema.FieldSpec p_fieldSpec) {
        switch (p_fieldSpec.getType()) {
            case BYTE_ARRAY:
                return p_length * Byte.BYTES + LENGHT_FIELD_BYTES;
            case CHAR_ARRAY:
                return p_length * Character.BYTES + LENGHT_FIELD_BYTES;
            case SHORT_ARRAY:
                return p_length * Short.BYTES + LENGHT_FIELD_BYTES;
            case INT_ARRAY:
                return p_length * Integer.BYTES + LENGHT_FIELD_BYTES;
            case LONG_ARRAY:
                return p_length * Long.BYTES + LENGHT_FIELD_BYTES;
            case FLOAT_ARRAY:
                return p_length * Float.BYTES + LENGHT_FIELD_BYTES;
            case DOUBLE_ARRAY:
                return p_length * Double.BYTES + LENGHT_FIELD_BYTES;
            case BOOLEAN_ARRAY:
                return p_length * Byte.BYTES + LENGHT_FIELD_BYTES;
            default:
                throw new IllegalArgumentException(String.format("%s is not an array type", p_fieldSpec.getType()));
        }
    }

    public static int sizeOf(final Object p_object, final Schema.FieldSpec p_fieldSpec) {
        return SIZE_FUNCTIONS[p_fieldSpec.getType().getId()].sizeOf(p_object, p_fieldSpec);
    }

    public static int constantSizeOf(final Schema.FieldSpec p_fieldSpec) {
        if (!p_fieldSpec.hasConstantSize()) {
            return NO_SIZE;
        }

        return sizeOf(null, p_fieldSpec);
    }

}
