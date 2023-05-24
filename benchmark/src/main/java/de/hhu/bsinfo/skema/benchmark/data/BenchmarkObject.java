package de.hhu.bsinfo.skema.benchmark.data;

import java.io.Serializable;
import java.util.Arrays;

public class BenchmarkObject implements Serializable {

    private final byte byteValue = 0x42;

    private final char charValue = 'X';

    private final short shortValue = 127;

    private final int intValue = 0xBEEF;

    private final long longValue = 0xC0FFEE;

    private final float floatValue = 3.14159265359F;

    private final double doubleValue = 1.41421356237309504880168872420969807;

    private final boolean booleanValue = true;

    private final byte[] byteArray = new byte[] { 0xA, 0xB, 0xC, 0xD, 0xE, 0xF };

    private final char[] charArray = new char[] { 'T', 'E', 'S', 'T', 'C', 'H', 'A', 'R' };

    private final short[] shortArray = new short[] { 1, 2, 4, 8, 16, 32, 64, 128, 256 };

    private final int[] intArray = new int[] { 512, 1024, 2048, 4096, 8192 };

    private final long[] longArray = new long[] { 0xAAAAAAAA, 0xBBBBBBBB, 0xCCCCCCCC, 0xDDDDDDDD, 0xEEEEEEEE };

    private final float[] floatArray = new float[] { 0.5F, 1.0F, 1.5F, 2.0F, 2.5F, 3.0F, 3.5F };

    private final double[] doubleArray = new double[] { 0.001, 9.999, 1.001, 10.999, 2.001, 11.999 };

    private final boolean[] booleanArray = new boolean[] { true, false, true, false, true, false, true, false };

    public byte getByte() {
        return byteValue;
    }

    public char getChar() {
        return charValue;
    }

    public short getShort() {
        return shortValue;
    }

    public int getInt() {
        return intValue;
    }

    public long getLong() {
        return longValue;
    }

    public float getFloat() {
        return floatValue;
    }

    public double getDouble() {
        return doubleValue;
    }

    public boolean isBoolean() {
        return booleanValue;
    }

    public byte[] getByteArray() {
        return byteArray;
    }

    public char[] getCharArray() {
        return charArray;
    }

    public short[] getShortArray() {
        return shortArray;
    }

    public int[] getIntArray() {
        return intArray;
    }

    public long[] getLongArray() {
        return longArray;
    }

    public float[] getFloatArray() {
        return floatArray;
    }

    public double[] getDoubleArray() {
        return doubleArray;
    }

    public boolean[] getBooleanArray() {
        return booleanArray;
    }

    @Override
    public boolean equals(Object p_o) {
        if (this == p_o) {
            return true;
        }
        if (p_o == null || getClass() != p_o.getClass()) {
            return false;
        }
        BenchmarkObject that = (BenchmarkObject) p_o;
        return byteValue == that.byteValue &&
                charValue == that.charValue &&
                shortValue == that.shortValue &&
                intValue == that.intValue &&
                longValue == that.longValue &&
                Float.compare(that.floatValue, floatValue) == 0 &&
                Double.compare(that.doubleValue, doubleValue) == 0 &&
                booleanValue == that.booleanValue &&
                Arrays.equals(byteArray, that.byteArray) &&
                Arrays.equals(charArray, that.charArray) &&
                Arrays.equals(shortArray, that.shortArray) &&
                Arrays.equals(intArray, that.intArray) &&
                Arrays.equals(longArray, that.longArray) &&
                Arrays.equals(floatArray, that.floatArray) &&
                Arrays.equals(doubleArray, that.doubleArray) &&
                Arrays.equals(booleanArray, that.booleanArray);
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
