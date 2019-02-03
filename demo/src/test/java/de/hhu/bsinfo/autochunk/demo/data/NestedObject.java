package de.hhu.bsinfo.autochunk.demo.data;

import java.util.Arrays;

public class NestedObject {

    private long[] m_array = new long[]{ 42, 56, 29, 91, 20, 54, 23, 85, 64, 10 };

    private String m_string = "A VERY VERY VERY VERY VERY VERY VERY VERY VERY LONG TEST STRING";

//    private BoxedCollection m_tprimitiveCollection = new BoxedCollection();

    @Override
    public boolean equals(Object p_o) {
        if (this == p_o) {
            return true;
        }
        if (p_o == null || getClass() != p_o.getClass()) {
            return false;
        }
        NestedObject that = (NestedObject) p_o;
        return Arrays.equals(m_array, that.m_array) &&
                m_string.equals(that.m_string);
    }

    @Override
    public int hashCode() {
        return 0;
    }
}
