package de.hhu.bsinfo.skema.benchmark.state;

import de.hhu.bsinfo.skema.Skema;
import de.hhu.bsinfo.skema.benchmark.util.Constants;

import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.SegmentScope;

public class SkemaState extends BaseState {

    private final Arena arena = Arena.openConfined();

    private final MemorySegment baseSegment = MemorySegment.allocateNative(
            Constants.STATIC_BUFFER_SIZE, Constants.MEMORY_ALIGNMENT, arena.scope());

    private final byte[] onHeapBuffer = new byte[Constants.STATIC_BUFFER_SIZE];

    private final long offHeapBuffer = baseSegment.address();

    @Override
    protected void onSetup(Object benchmarkObject) {
        // Fill buffers with dummy data for deserialization
        Skema.serialize(benchmarkObject, onHeapBuffer);
        Skema.serialize(benchmarkObject, offHeapBuffer);
    }

    @Override
    protected void onTeardown() {
        arena.close();
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
