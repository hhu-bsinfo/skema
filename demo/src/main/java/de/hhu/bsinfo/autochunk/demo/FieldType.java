package de.hhu.bsinfo.autochunk.demo;

public enum FieldType {
    UNKNOWN(-1, "unknown", -1),
    BYTE(0, byte.class.getCanonicalName(), Byte.BYTES),
    SHORT(1, short.class.getCanonicalName(), Short.BYTES),
    INT(2, int.class.getCanonicalName(), Integer.BYTES),
    LONG(3, long.class.getCanonicalName(), Long.BYTES),
    BYTE_BOXED(4, Byte.class.getCanonicalName(), Byte.BYTES),
    SHORT_BOXED(5, Short.class.getCanonicalName(), Short.BYTES),
    INT_BOXED(6, Integer.class.getCanonicalName(), Integer.BYTES),
    LONG_BOXED(7, Long.class.getCanonicalName(), Long.BYTES);

    /**
     * The field type's unique identifier.
     */
    private final int m_id;

    /**
     * The field type's name.
     */
    private final String m_name;

    /**
     * The field type's size in bytes.
     */
    private final int m_size;

    FieldType(final int p_id, final String p_name, final int p_size) {
        m_id = p_id;
        m_name = p_name;
        m_size = p_size;
    }

    public int getSize() {
        return m_size;
    }

    public String getName() {
        return m_name;
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
