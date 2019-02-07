package de.hhu.bsinfo.autochunk.demo;

import de.hhu.bsinfo.autochunk.demo.data.*;

public class BenchmarkInput {

    private final Object[] m_objects = new Object[] {
            new Measurement(42L, 928.5),
            new PrimitiveCollection(),
            new BoxedCollection(),
            new Profile(42L, "John", "Doe", new long[] { 314L, 592L, 239L },
                    new Storyboard(2732L, 93)),
            new Result(Status.OK),
            Status.OK,

    };

    public Object get(final int p_index) {
        return m_objects[p_index];
    }

}
