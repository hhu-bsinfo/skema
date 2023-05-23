package de.hhu.bsinfo.skema.benchmark.util;

import de.hhu.bsinfo.skema.Skema;
import de.hhu.bsinfo.skema.benchmark.data.BoxedCollection;
import de.hhu.bsinfo.skema.benchmark.data.PrimitiveCollection;
import de.hhu.bsinfo.skema.benchmark.data.Result;
import de.hhu.bsinfo.skema.benchmark.data.TextMessage;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static de.hhu.bsinfo.skema.benchmark.util.Constants.DataType.*;


public class BenchmarkInput {

    static {
        Skema.enableAutoRegistration();
    }

    private final Map<String, Object> data = new HashMap<>();

    public BenchmarkInput(final long seed) {
        Random random = new Random(seed);
        data.put(PRIMITIVE, Skema.newRandomInstance(PrimitiveCollection.class, random));
        data.put(BOXED, Skema.newRandomInstance(BoxedCollection.class, random));
        data.put(POLYMORPHIC, Skema.newRandomInstance(TextMessage.class, random));
        data.put(ENUM, Skema.newRandomInstance(Result.class, random));
    }

    public Object get(final String type) {
        return data.get(type);
    }
}
