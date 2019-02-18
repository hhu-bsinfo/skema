package de.hhu.bsinfo.skema.data;

import java.util.Arrays;

public class TestClass {

    private final byte a;

    private final char x;

    private final byte[] b;

    public TestClass(byte p_a, char p_x, byte[] p_b) {
        a = p_a;
        x = p_x;
        b = p_b;
    }

    @Override
    public boolean equals(Object p_o) {
        if (this == p_o) {
            return true;
        }
        if (p_o == null || getClass() != p_o.getClass()) {
            return false;
        }
        TestClass testClass = (TestClass) p_o;
        return a == testClass.a &&
                x == testClass.x &&
                Arrays.equals(b, testClass.b);
    }

    @Override
    public int hashCode() {
        return 0;
    }
}
