package de.hhu.bsinfo.skema.benchmark.state;

import de.hhu.bsinfo.skema.Skema;
import de.hhu.bsinfo.skema.benchmark.data.PrimitiveCollection;
import de.hhu.bsinfo.skema.benchmark.util.Constants;
import de.hhu.bsinfo.skema.benchmark.util.MemoryType;
import org.openjdk.jmh.annotations.*;

import java.util.Random;

import static de.hhu.bsinfo.skema.benchmark.util.Constants.MemoryType.OFF_HEAP;
import static de.hhu.bsinfo.skema.benchmark.util.Constants.MemoryType.ON_HEAP;

@State(Scope.Thread)
public abstract class BaseState {

    static {
        Skema.enableAutoRegistration();
    }

    @Param({ON_HEAP, OFF_HEAP})
    public String memoryType;

    public Object data;
    public Class<?> dataClass;
    private MemoryType memoryTarget;

    private final Random random = new Random(Constants.BENCHMARK_SEED);

    @Setup(Level.Trial)
    public void setup() {
        data = Skema.newRandomInstance(PrimitiveCollection.class, random);
        dataClass = data.getClass();
        memoryTarget = fromString(memoryType);

        onSetup();
    }

    @TearDown(Level.Trial)
    public void teardown() {

    }

    public long serialize() {
        return switch (memoryTarget) {
            case ON_HEAP -> serializeOnHeap(data);
            case OFF_HEAP -> serializeOffHeap(data);
        };
    }

    public Object deserialize() {
        return switch (memoryTarget) {
            case ON_HEAP -> deserializeOnHeap(dataClass);
            case OFF_HEAP -> deserializeOffHeap(dataClass);
        };
    }

    protected abstract long serializeOnHeap(Object object);

    protected abstract long serializeOffHeap(Object object);

    protected abstract Object deserializeOnHeap(Class<?> clazz);

    protected abstract Object deserializeOffHeap(Class<?> clazz);

    protected abstract void onSetup();

    protected abstract void onTeardown();

    private static final MemoryType fromString(String memoryType) {
        return switch (memoryType) {
            case ON_HEAP -> MemoryType.ON_HEAP;
            case OFF_HEAP -> MemoryType.OFF_HEAP;
            default -> null;
        };
    }
}
