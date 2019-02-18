package de.hhu.bsinfo.skema;

import de.hhu.bsinfo.skema.data.*;
import de.hhu.bsinfo.skema.data.BoxedCollection;
import de.hhu.bsinfo.skema.data.Measurement;
import de.hhu.bsinfo.skema.data.PrimitiveCollection;
import de.hhu.bsinfo.skema.data.Result;
import de.hhu.bsinfo.skema.data.Status;

public class BenchmarkInput {

    private final Object[] m_objects = new Object[] {
            new Measurement(42L, 928.5, 28378237L),
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
