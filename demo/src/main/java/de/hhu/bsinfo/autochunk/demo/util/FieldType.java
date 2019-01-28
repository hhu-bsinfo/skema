package de.hhu.bsinfo.autochunk.demo.util;

/**
 * Indicates a field's type.
 */
public enum FieldType {
    OBJECT(0, Object.class.getCanonicalName(), false),
    BYTE(1, byte.class.getCanonicalName(), true),
    CHAR(2, char.class.getCanonicalName(), true),
    SHORT(3, short.class.getCanonicalName(), true),
    INT(4, int.class.getCanonicalName(), true),
    LONG(5, long.class.getCanonicalName(), true),
    FLOAT(6, float.class.getCanonicalName(), true),
    DOUBLE(7, double.class.getCanonicalName(), true),
    BOOLEAN(8, boolean.class.getCanonicalName(), true),
    CHAR_ARRAY(9, char[].class.getCanonicalName(), false),
    BYTE_ARRAY(10, byte[].class.getCanonicalName(), false),
    SHORT_ARRAY(11, short[].class.getCanonicalName(), false),
    INT_ARRAY(12, int[].class.getCanonicalName(), false),
    LONG_ARRAY(13, long[].class.getCanonicalName(), false),
    FLOAT_ARRAY(14, float[].class.getCanonicalName(), false),
    DOUBLE_ARRAY(15, double[].class.getCanonicalName(), false),
    BOOLEAN_ARRAY(16, boolean[].class.getCanonicalName(), false),
    OBJECT_ARRAY(17, Object[].class.getCanonicalName(), false),
    ENUM(18, Enum.class.getCanonicalName(), true);

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

    FieldType(final int p_id, final String p_name, final boolean p_hasConstantSize) {
        m_id = p_id;
        m_name = p_name;
        m_hasConstantSize = p_hasConstantSize;
    }

    public String getName() {
        return m_name;
    }

    public int getId() {
        return m_id;
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


}
