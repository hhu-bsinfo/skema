package de.hhu.bsinfo.skema.benchmark.state;

import de.hhu.bsinfo.skema.Skema;
import de.hhu.bsinfo.skema.benchmark.util.Constants;

import java.lang.foreign.MemorySegment;
import java.lang.foreign.SegmentScope;

public class SkemaState extends BaseState {

    private static final MemorySegment BASE_MEMORY = MemorySegment.allocateNative(
            Constants.STATIC_BUFFER_SIZE, Constants.MEMORY_ALIGNMENT, SegmentScope.global());

    private final byte[] onHeapBuffer = new byte[Constants.STATIC_BUFFER_SIZE];

    private final long offHeapBuffer = BASE_MEMORY.address();

    @Override
    protected void onSetup() {
        Skema.enableAutoRegistration();
    }

    @Override
    protected void onTeardown() {
        Skema.free(offHeapBuffer);
    }

    @Override
    protected long serializeOnHeap(Object object) {
        return Skema.serialize(object, onHeapBuffer);
    }

    @Override
    protected long serializeOffHeap(Object object) {
        return Skema.serialize(object, offHeapBuffer);
    }

    @Override
    protected Object deserializeOnHeap(Class<?> clazz) {
        return Skema.deserialize(clazz, onHeapBuffer);
    }

    @Override
    protected Object deserializeOffHeap(Class<?> clazz) {
        return Skema.deserialize(clazz, offHeapBuffer);
    }
}
