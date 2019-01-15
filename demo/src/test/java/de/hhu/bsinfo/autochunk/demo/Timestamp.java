package de.hhu.bsinfo.autochunk.demo;

import java.util.Arrays;

public class Timestamp {

    private final int m_id;

    private final long m_value;

    private final int[] m_ints;

    public Timestamp(final int p_id, final long p_value, final int[] p_ints) {
        m_id = p_id;
        m_value = p_value;
        m_ints = p_ints;
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

    @Override
    public String toString() {
        return Arrays.toString(m_ints);
    }
}
