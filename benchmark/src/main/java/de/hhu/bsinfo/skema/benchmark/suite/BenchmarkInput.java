package de.hhu.bsinfo.skema.benchmark.suite;

import de.hhu.bsinfo.skema.Skema;
import de.hhu.bsinfo.skema.benchmark.data.BoxedCollection;
import de.hhu.bsinfo.skema.benchmark.data.PrimitiveCollection;
import de.hhu.bsinfo.skema.benchmark.data.Result;
import de.hhu.bsinfo.skema.benchmark.data.TextMessage;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class BenchmarkInput {

    private final Map<String, Object> data = new HashMap<>();

    public BenchmarkInput(final long seed) {
        Random random = new Random(seed);
        data.put("primitive", Skema.newRandomInstance(PrimitiveCollection.class, random));
        data.put("boxed", Skema.newRandomInstance(BoxedCollection.class, random));
        data.put("polymorphic", Skema.newRandomInstance(TextMessage.class, random));
        data.put("enum", Skema.newRandomInstance(Result.class, random));
    }

    public Object get(final String type) {
        return data.get(type);
    }

}
