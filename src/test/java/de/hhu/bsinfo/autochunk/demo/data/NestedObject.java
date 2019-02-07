package de.hhu.bsinfo.autochunk.demo.data;

import java.util.Arrays;

public class NestedObject {

    private String[] m_strings = {
            "A VERY VERY VERY VERY VERY VERY VERY VERY VERY LONG TEST STRING",
            "A VERY VERY VERY VERY VERY VERY VERY VERY LONG TEST STRING",
            "A VERY VERY VERY VERY VERY VERY VERY LONG TEST STRING",
            "A VERY VERY VERY VERY VERY VERY LONG TEST STRING",
            "A VERY VERY VERY VERY VERY LONG TEST STRING",
            "A VERY VERY VERY VERY LONG TEST STRING",
            "A VERY VERY VERY LONG TEST STRING",
            "A VERY VERY LONG TEST STRING",
            "A VERY LONG TEST STRING"
    };

    @Override
    public boolean equals(Object p_o) {
        if (this == p_o) {
            return true;
        }
        if (p_o == null || getClass() != p_o.getClass()) {
            return false;
        }
        NestedObject that = (NestedObject) p_o;
        return Arrays.equals(m_strings, that.m_strings);
    }

    @Override
    public int hashCode() {
        return 0;
    }
}
