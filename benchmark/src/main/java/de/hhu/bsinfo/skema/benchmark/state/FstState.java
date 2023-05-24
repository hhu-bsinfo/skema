package de.hhu.bsinfo.skema.benchmark.state;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.unsafe.UnsafeByteBufferInput;
import com.esotericsoftware.kryo.unsafe.UnsafeByteBufferOutput;
import com.esotericsoftware.kryo.unsafe.UnsafeInput;
import com.esotericsoftware.kryo.unsafe.UnsafeOutput;
import de.hhu.bsinfo.skema.Skema;
import de.hhu.bsinfo.skema.benchmark.util.Constants;
import org.nustaq.serialization.FSTConfiguration;
import org.nustaq.serialization.simpleapi.DefaultCoder;
import org.nustaq.serialization.simpleapi.OffHeapCoder;
import org.nustaq.serialization.simpleapi.OnHeapCoder;

import java.io.IOException;
import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;
import java.nio.ByteBuffer;

public class FstState extends BaseState {

    private final Arena arena = Arena.openConfined();

    private final MemorySegment baseSegment = MemorySegment.allocateNative(
            Constants.STATIC_BUFFER_SIZE, Constants.MEMORY_ALIGNMENT, arena.scope());

    private final byte[] byteArray = new byte[Constants.STATIC_BUFFER_SIZE];

    private final ByteBuffer byteBuffer = baseSegment.asByteBuffer();

    private final OnHeapCoder onHeapCoder = new OnHeapCoder();

    private final OffHeapCoder offHeapCoder = new OffHeapCoder();

    public FstState() {}

    @Override
    protected void onSetup(Object benchmarkObject) {
        // Fill buffers with dummy data for deserialization
        serializeOnHeap(benchmarkObject);
        serializeOffHeap(benchmarkObject);
    }

    @Override
    protected void onTeardown() {
        // Release memory
        arena.close();
    }

    @Override
    protected long serializeOnHeap(Object object) {
        return onHeapCoder.toByteArray(object, byteArray, 0, byteArray.length);
    }

    @Override
    protected long serializeOffHeap(Object object) {
        try {
            return offHeapCoder.toMemory(object, baseSegment.address(), (int) baseSegment.byteSize());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected Object deserializeOnHeap(Class<?> clazz) {
        return onHeapCoder.toObject(byteArray);
    }

    @Override
    protected Object deserializeOffHeap(Class<?> clazz) {
        try {
            return offHeapCoder.toObject(baseSegment.address(), (int) baseSegment.byteSize());
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
