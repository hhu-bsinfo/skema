package de.hhu.bsinfo.autochunk.demo.data;

import de.hhu.bsinfo.autochunk.demo.util.RandomUtil;

public class Measurement {

    private final long m_id;

    private final double m_value;

    public Measurement() {
        this(0, 0);
    }

    public Measurement(long p_id, double p_value) {
        m_id = p_id;
        m_value = p_value;
    }

    public long getId() {
        return m_id;
    }

    public double getValue() {
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
        Measurement that = (Measurement) p_o;
        return m_id == that.m_id &&
                Double.compare(that.m_value, m_value) == 0;
    }

    @Override
    public int hashCode() {
        return Double.hashCode(m_id);
    }

    public static Measurement random() {
        return new Measurement(RandomUtil.randomLong(), RandomUtil.randomDouble());
    }

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }
}
