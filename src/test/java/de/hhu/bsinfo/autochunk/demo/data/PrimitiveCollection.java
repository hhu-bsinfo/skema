package de.hhu.bsinfo.autochunk.demo.data;

import java.util.Arrays;

public class PrimitiveCollection {

    private final byte m_byte = 0x42;

    private final char m_char = 'X';

    private final short m_short = 127;

    private final int m_int = 0xBEEF;

    private final long m_long = 0xC0FFEE;

    private final float m_float = 3.14159265359F;

    private final double m_double = 1.41421356237309504880168872420969807;

    private final boolean m_boolean = true;

    private final byte[] m_byteArray = new byte[] { 0xA, 0xB, 0xC, 0xD, 0xE, 0xF };

    private final char[] m_charArray = new char[] { 'T', 'E', 'S', 'T', 'C', 'H', 'A', 'R' };

    private final short[] m_shortArray = new short[] { 1, 2, 4, 8, 16, 32, 64, 128, 256 };

    private final int[] m_intArray = new int[] { 512, 1024, 2048, 4096, 8192 };

    private final long[] m_longArray = new long[] { 0xAAAAAAAA, 0xBBBBBBBB, 0xCCCCCCCC, 0xDDDDDDDD, 0xEEEEEEEE };

    private final float[] m_floatArray = new float[] { 0.5F, 1.0F, 1.5F, 2.0F, 2.5F, 3.0F, 3.5F };

    private final double[] m_doubleArray = new double[] { 0.001, 9.999, 1.001, 10.999, 2.001, 11.999 };

    private final boolean[] m_booleanArray = new boolean[] { true, false, true, false, true, false, true, false };

    public byte getByte() {
        return m_byte;
    }

    public char getChar() {
        return m_char;
    }

    public short getShort() {
        return m_short;
    }

    public int getInt() {
        return m_int;
    }

    public long getLong() {
        return m_long;
    }

    public float getFloat() {
        return m_float;
    }

    public double getDouble() {
        return m_double;
    }

    public boolean isBoolean() {
        return m_boolean;
    }

    public byte[] getByteArray() {
        return m_byteArray;
    }

    public char[] getCharArray() {
        return m_charArray;
    }

    public short[] getShortArray() {
        return m_shortArray;
    }

    public int[] getIntArray() {
        return m_intArray;
    }

    public long[] getLongArray() {
        return m_longArray;
    }

    public float[] getFloatArray() {
        return m_floatArray;
    }

    public double[] getDoubleArray() {
        return m_doubleArray;
    }

    public boolean[] getBooleanArray() {
        return m_booleanArray;
    }

    @Override
    public boolean equals(Object p_o) {
        if (this == p_o) {
            return true;
        }
        if (p_o == null || getClass() != p_o.getClass()) {
            return false;
        }
        PrimitiveCollection that = (PrimitiveCollection) p_o;
        return m_byte == that.m_byte &&
                m_char == that.m_char &&
                m_short == that.m_short &&
                m_int == that.m_int &&
                m_long == that.m_long &&
                Float.compare(that.m_float, m_float) == 0 &&
                Double.compare(that.m_double, m_double) == 0 &&
                m_boolean == that.m_boolean &&
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

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }
}
