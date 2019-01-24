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

    private final Byte[] m_byteArray = new Byte[] { 0xA, 0xB, 0xC, 0xD, 0xE, 0xF };

    private final Character[] m_charArray = new Character[] { 'T', 'E', 'S', 'T', 'C', 'H', 'A', 'R' };

    private final Short[] m_shortArray = new Short[] { 1, 2, 4, 8, 16, 32, 64, 128, 256 };

    private final Integer[] m_intArray = new Integer[] { 512, 1024, 2048, 4096, 8192 };

    private final Long[] m_longArray = new Long[] { 0xAAAAAAAAL, 0xBBBBBBBBL, 0xCCCCCCCCL, 0xDDDDDDDDL, 0xEEEEEEEEL };

    private final Float[] m_floatArray = new Float[] { 0.5F, 1.0F, 1.5F, 2.0F, 2.5F, 3.0F, 3.5F };

    private final Double[] m_doubleArray = new Double[] { 0.001, 9.999, 1.001, 10.999, 2.001, 11.999 };

    private final Boolean[] m_booleanArray = new Boolean[] { true, false, true, false, true, false, true, false };

    public Byte[] getByteArray() {
        return m_byteArray;
    }

    public Character[] getCharArray() {
        return m_charArray;
    }

    public Short[] getShortArray() {
        return m_shortArray;
    }

    public Integer[] getIntArray() {
        return m_intArray;
    }

    public Long[] getLongArray() {
        return m_longArray;
    }

    public Float[] getFloatArray() {
        return m_floatArray;
    }

    public Double[] getDoubleArray() {
        return m_doubleArray;
    }

    public Boolean[] getBooleanArray() {
        return m_booleanArray;
    }

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
                Arrays.equals(m_bytes, that.m_bytes) &&
                Arrays.equals(m_byteArray, that.m_byteArray) &&
                Arrays.equals(m_charArray, that.m_charArray) &&
                Arrays.equals(m_shortArray, that.m_shortArray) &&
                Arrays.equals(m_intArray, that.m_intArray) &&
                Arrays.equals(m_longArray, that.m_longArray) &&
                Arrays.equals(m_floatArray, that.m_floatArray) &&
                Arrays.equals(m_doubleArray, that.m_doubleArray) &&
                Arrays.equals(m_booleanArray, that.m_booleanArray);
    }

    @Override
    public int hashCode() {
        return 0;
    }
}
