package de.hhu.bsinfo.skema.benchmark.state;

import de.hhu.bsinfo.skema.Skema;
import de.hhu.bsinfo.skema.benchmark.util.Constants;

public class SkemaState extends BaseState {

    private final byte[] onHeapBuffer = new byte[Constants.STATIC_BUFFER_SIZE];

    private final long offHeapBuffer = Skema.allocate(Constants.STATIC_BUFFER_SIZE);

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
