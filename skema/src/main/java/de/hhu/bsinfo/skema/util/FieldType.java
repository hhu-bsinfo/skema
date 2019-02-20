package de.hhu.bsinfo.skema.util;

/**
 * Indicates a field's type.
 */
public enum FieldType {
    OBJECT(0, Object.class.getCanonicalName(), false, Constants.NO_OFFSET),
    BYTE(1, byte.class.getCanonicalName(), true, Constants.NO_OFFSET),
    CHAR(2, char.class.getCanonicalName(), true, Constants.NO_OFFSET),
    SHORT(3, short.class.getCanonicalName(), true, Constants.NO_OFFSET),
    INT(4, int.class.getCanonicalName(), true, Constants.NO_OFFSET),
    LONG(5, long.class.getCanonicalName(), true, Constants.NO_OFFSET),
    FLOAT(6, float.class.getCanonicalName(), true, Constants.NO_OFFSET),
    DOUBLE(7, double.class.getCanonicalName(), true, Constants.NO_OFFSET),
    BOOLEAN(8, boolean.class.getCanonicalName(), true, Constants.NO_OFFSET),
    BYTE_ARRAY(9, byte[].class.getCanonicalName(), false, Constants.BYTE_ARRAY_OFFSET),
    CHAR_ARRAY(10, char[].class.getCanonicalName(), false, Constants.CHAR_ARRAY_OFFSET),
    SHORT_ARRAY(11, short[].class.getCanonicalName(), false, Constants.SHORT_ARRAY_OFFSET),
    INT_ARRAY(12, int[].class.getCanonicalName(), false, Constants.INT_ARRAY_OFFSET),
    LONG_ARRAY(13, long[].class.getCanonicalName(), false, Constants.LONG_ARRAY_OFFSET),
    FLOAT_ARRAY(14, float[].class.getCanonicalName(), false, Constants.FLOAT_ARRAY_OFFSET),
    DOUBLE_ARRAY(15, double[].class.getCanonicalName(), false, Constants.DOUBLE_ARRAY_OFFSET),
    BOOLEAN_ARRAY(16, boolean[].class.getCanonicalName(), false, Constants.BOOLEAN_ARRAY_OFFSET),
    OBJECT_ARRAY(17, Object[].class.getCanonicalName(), false, Constants.OBJECT_ARRAY_OFFSET),
    ENUM(18, Enum.class.getCanonicalName(), true, Constants.NO_OFFSET),
    LENGTH(19, HiddenField.class.getCanonicalName(), true, Constants.NO_OFFSET);

    /**
     * The field type's unique identifier.
     */
    private final int m_id;

    /**
     * The field type's name.
     */
    private final String m_name;

    /**
     * Indicates if this type's size is constant.
     */
    private final boolean m_hasConstantSize;

    /**
     * This types base offset.
     */
    private final long m_baseOffset;

    FieldType(final int p_id, final String p_name, final boolean p_hasConstantSize, final long p_baseOffset) {
        m_id = p_id;
        m_name = p_name;
        m_hasConstantSize = p_hasConstantSize;
        m_baseOffset = p_baseOffset;
    }

    public String getName() {
        return m_name;
    }

    public int getId() {
        return m_id;
    }

    public long getBaseOffset() {
        return m_baseOffset;
    }

    public boolean hasConstantSize() {
        return m_hasConstantSize;
    }

    public static FieldType fromClass(Class<?> p_class) {
        if (p_class.isEnum()) {
            return ENUM;
        }

        for (FieldType fieldType : FieldType.values()) {
            if (fieldType.m_name.equals(p_class.getCanonicalName())) {
                return fieldType;
            }
        }

        return p_class.isArray() ? OBJECT_ARRAY : OBJECT;
    }

    @Override
    public String toString() {
        return m_name;
    }

    private static final class HiddenField {}
}
