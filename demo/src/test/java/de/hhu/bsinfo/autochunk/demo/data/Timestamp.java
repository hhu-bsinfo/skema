package de.hhu.bsinfo.autochunk.demo.data;

import java.util.Arrays;

import de.hhu.bsinfo.autochunk.demo.util.RandomUtil;

public class Timestamp {

    private final int m_id;

    private final long m_value;

    private final int[] m_ints;

    private final long[] m_longs;

    public Timestamp(final int p_id, final long p_value, final int[] p_ints, final long[] p_longs) {
        m_id = p_id;
        m_value = p_value;
        m_ints = p_ints;
        m_longs = p_longs;
    }

    public int getId() {
        return m_id;
    }

    public long getValue() {
        return m_value;
    }

    public int[] getInts() {
        return m_ints;
    }

    public long[] getLongs() {
        return m_longs;
    }

    @Override
    public boolean equals(Object p_o) {
        if (this == p_o) {
            return true;
        }
        if (p_o == null || getClass() != p_o.getClass()) {
            return false;
        }
        Timestamp that = (Timestamp) p_o;
        return m_id == that.m_id &&
                m_value == that.m_value;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(m_id);
    }

    public static Timestamp random() {
        return new Timestamp(RandomUtil.randomInt(), RandomUtil.randomLong(), RandomUtil.randomIntArray(128), RandomUtil.randomLongArray(128));
    }

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }
}
