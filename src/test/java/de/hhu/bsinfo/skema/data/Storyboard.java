package de.hhu.bsinfo.skema.data;

import de.hhu.bsinfo.skema.util.RandomUtil;

public class Storyboard {

    private final long m_id;

    private final int m_postCount;

    public Storyboard() {
        this(0, 0);
    }

    public Storyboard(long p_id, int p_postCount) {
        m_id = p_id;
        m_postCount = p_postCount;
    }

    public long getId() {
        return m_id;
    }

    public int getPostCount() {
        return m_postCount;
    }

    public static Storyboard random() {
        return new Storyboard(RandomUtil.randomLong(), RandomUtil.randomInt());
    }

    @Override
    public boolean equals(Object p_o) {
        if (this == p_o) {
            return true;
        }
        if (p_o == null || getClass() != p_o.getClass()) {
            return false;
        }
        Storyboard that = (Storyboard) p_o;
        return m_id == that.m_id &&
                m_postCount == that.m_postCount;
    }

    @Override
    public int hashCode() {
        return Long.hashCode(m_id);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }
}
