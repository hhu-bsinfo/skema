package de.hhu.bsinfo.autochunk.demo;

public enum FieldType {
    UNKNOWN(0, "unknown"),
    BYTE(1, byte.class.getCanonicalName()),
    SHORT(2, short.class.getCanonicalName()),
    INT(3, int.class.getCanonicalName()),
    LONG(4, long.class.getCanonicalName()),
    BYTE_BOXED(5, Byte.class.getCanonicalName()),
    SHORT_BOXED(6, Short.class.getCanonicalName()),
    INT_BOXED(7, Integer.class.getCanonicalName()),
    LONG_BOXED(8, Long.class.getCanonicalName()),
    BYTE_ARRAY(9, byte[].class.getCanonicalName()),
    SHORT_ARRAY(10, short[].class.getCanonicalName()),
    INT_ARRAY(11, int[].class.getCanonicalName()),
    LONG_ARRAY(12, long[].class.getCanonicalName());

    /**
     * The field type's unique identifier.
     */
    private final int m_id;

    /**
     * The field type's name.
     */
    private final String m_name;

    FieldType(final int p_id, final String p_name) {
        m_id = p_id;
        m_name = p_name;
    }

    public String getName() {
        return m_name;
    }

    public int getId() {
        return m_id;
    }

    public static FieldType fromClass(Class<?> p_class) {
        for (FieldType fieldType : FieldType.values()) {
            if (fieldType.m_name.equals(p_class.getCanonicalName())) {
                return fieldType;
            }
        }

        return UNKNOWN;
    }

    @Override
    public String toString() {
        return m_name;
    }
}
