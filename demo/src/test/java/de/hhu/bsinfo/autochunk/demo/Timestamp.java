package de.hhu.bsinfo.autochunk.demo;

public class Timestamp {

    @Schemable
    private final int m_id;

    @Schemable
    private final long m_value;

    public Timestamp(final int p_id, final long p_value) {
        m_id = p_id;
        m_value = p_value;
    }

    public int getId() {
        return m_id;
    }

    public long getValue() {
        return m_value;
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
}
