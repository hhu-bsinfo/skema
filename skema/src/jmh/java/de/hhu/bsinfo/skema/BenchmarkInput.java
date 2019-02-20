package de.hhu.bsinfo.skema;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import de.hhu.bsinfo.skema.data.*;
import de.hhu.bsinfo.skema.data.BoxedCollection;
import de.hhu.bsinfo.skema.data.Measurement;
import de.hhu.bsinfo.skema.data.PrimitiveCollection;
import de.hhu.bsinfo.skema.data.Result;
import de.hhu.bsinfo.skema.data.Status;

public class BenchmarkInput {

    private final Map<String, Object> m_data = new HashMap<>();

    public BenchmarkInput(final long p_seed) {
        Random random = new Random(p_seed);
        m_data.put("primitive", Skema.newRandomInstance(PrimitiveCollection.class, random));
        m_data.put("boxed", Skema.newRandomInstance(BoxedCollection.class, random));
        m_data.put("polymorphic", Skema.newRandomInstance(TextMessage.class, random));
        m_data.put("enum", Skema.newRandomInstance(Result.class, random));
    }

    public Object get(final String p_type) {
        return m_data.get(p_type);
    }

}
