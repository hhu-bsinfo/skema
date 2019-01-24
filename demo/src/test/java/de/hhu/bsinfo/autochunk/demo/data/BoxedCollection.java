package de.hhu.bsinfo.autochunk.demo.data;

import java.util.Arrays;

public class BoxedCollection {

    private final Byte m_byte = 0x42;

    private final Character m_char = 'X';

    private final Short m_short = 127;

    private final Integer m_int = 0xBEEF;

    private final Long m_long = 0xC0FFEEL;

    private final Float m_float = 3.14159265359F;

    private final Double m_double = 1.41421356237309504880168872420969807;

    private final Boolean m_boolean = true;

    private final Byte[] m_bytes = new Byte[] { 0xA, 0xB, 0xC, 0xD, 0xE, 0xF };

    public Byte[] getBytes() {
        return m_bytes;
    }

    public Byte getByte() {
        return m_byte;
    }

    public Character getChar() {
        return m_char;
    }

    public Short getShort() {
        return m_short;
    }

    public Integer getInt() {
        return m_int;
    }

    public Long getLong() {
        return m_long;
    }

    public Float getFloat() {
        return m_float;
    }

    public Double getDouble() {
        return m_double;
    }

    public Boolean getBoolean() {
        return m_boolean;
    }

    @Override
    public boolean equals(Object p_o) {
        if (this == p_o) {
            return true;
        }
        if (p_o == null || getClass() != p_o.getClass()) {
            return false;
        }
        BoxedCollection that = (BoxedCollection) p_o;
        return m_byte.equals(that.m_byte) &&
                m_char.equals(that.m_char) &&
                m_short.equals(that.m_short) &&
                m_int.equals(that.m_int) &&
                m_long.equals(that.m_long) &&
                m_float.equals(that.m_float) &&
                m_double.equals(that.m_double) &&
                m_boolean.equals(that.m_boolean) &&
                Arrays.equals(m_bytes, that.m_bytes);
    }

    @Override
    public int hashCode() {
        return 0;
    }
}
