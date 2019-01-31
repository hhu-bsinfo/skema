package de.hhu.bsinfo.autochunk.demo.data;

public class Numbers {

    private byte m_byte = 0x42;

    private char m_char = 'X';

    private short m_short = 127;

    private int m_int = 0xBEEF;

    private long m_long = 0xC0FFEE;

    private float m_float = 3.14159265359F;

    private double m_double = 1.41421356237309504880168872420969807;

    @Override
    public boolean equals(Object p_o) {
        if (this == p_o) {
            return true;
        }
        if (p_o == null || getClass() != p_o.getClass()) {
            return false;
        }
        Numbers numbers = (Numbers) p_o;
        return m_byte == numbers.m_byte &&
                m_char == numbers.m_char &&
                m_short == numbers.m_short &&
                m_int == numbers.m_int &&
                m_long == numbers.m_long &&
                Float.compare(numbers.m_float, m_float) == 0 &&
                Double.compare(numbers.m_double, m_double) == 0;
    }

    @Override
    public int hashCode() {
        return 0;
    }
}
