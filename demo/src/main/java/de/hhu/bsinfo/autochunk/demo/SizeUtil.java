package de.hhu.bsinfo.autochunk.demo;

import java.lang.reflect.Array;

public class SizeUtil {

    private static final SizeFunction[] SIZE_FUNCTIONS = new SizeFunction[FieldType.values().length];

    static {
        SIZE_FUNCTIONS[FieldType.BYTE.getId()] = ((p_object, p_fieldSpec) -> Byte.BYTES);
        SIZE_FUNCTIONS[FieldType.SHORT.getId()] = ((p_object, p_fieldSpec) -> Short.BYTES);
        SIZE_FUNCTIONS[FieldType.INT.getId()] = ((p_object, p_fieldSpec) -> Integer.BYTES);
        SIZE_FUNCTIONS[FieldType.LONG.getId()] = ((p_object, p_fieldSpec) -> Long.BYTES);
        SIZE_FUNCTIONS[FieldType.BYTE_BOXED.getId()] = ((p_object, p_fieldSpec) -> Byte.BYTES);
        SIZE_FUNCTIONS[FieldType.SHORT_BOXED.getId()] = ((p_object, p_fieldSpec) -> Short.BYTES);
        SIZE_FUNCTIONS[FieldType.INT_BOXED.getId()] = ((p_object, p_fieldSpec) -> Integer.BYTES);
        SIZE_FUNCTIONS[FieldType.LONG_BOXED.getId()] = ((p_object, p_fieldSpec) -> Long.BYTES);
        SIZE_FUNCTIONS[FieldType.BYTE_ARRAY.getId()] = SizeUtil::getArraySize;
        SIZE_FUNCTIONS[FieldType.SHORT_ARRAY.getId()] = SizeUtil::getArraySize;
        SIZE_FUNCTIONS[FieldType.INT_ARRAY.getId()] = SizeUtil::getArraySize;
        SIZE_FUNCTIONS[FieldType.LONG_ARRAY.getId()] = SizeUtil::getArraySize;
    }

    /**
     * Returns the size of an array in bytes including the length field.
     *
     * @param p_object The array.
     * @param p_fieldSpec The field specification.
     * @return The array's size in bytes.
     */
    public static int getArraySize(final Object p_object, final Schema.FieldSpec p_fieldSpec) {
        Object array = ArrayUtil.getArray(p_object, p_fieldSpec);
        return getArraySize(Array.getLength(array), p_fieldSpec);
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
                return p_length * Byte.BYTES + Integer.BYTES;
            case SHORT_ARRAY:
                return p_length * Short.BYTES + Integer.BYTES;
            case INT_ARRAY:
                return p_length * Integer.BYTES + Integer.BYTES;
            case LONG_ARRAY:
                return p_length * Long.BYTES + Integer.BYTES;
            default:
                throw new IllegalArgumentException(String.format("%s is not an array type", p_fieldSpec.getType()));
        }
    }

    public static int sizeOf(final Object p_object, final Schema.FieldSpec p_fieldSpec) {
        return SIZE_FUNCTIONS[p_fieldSpec.getType().getId()].sizeOf(p_object, p_fieldSpec);
    }

}
