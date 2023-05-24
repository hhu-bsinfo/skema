package de.hhu.bsinfo.skema.benchmark.state;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.unsafe.UnsafeByteBufferInput;
import com.esotericsoftware.kryo.unsafe.UnsafeByteBufferOutput;
import com.esotericsoftware.kryo.unsafe.UnsafeInput;
import com.esotericsoftware.kryo.unsafe.UnsafeOutput;
import de.hhu.bsinfo.skema.benchmark.util.Constants;

import java.lang.foreign.MemorySegment;
import java.lang.foreign.SegmentScope;
import java.nio.ByteBuffer;

public class KryoState extends BaseState {

    private static final MemorySegment BASE_MEMORY = MemorySegment.allocateNative(
            Constants.STATIC_BUFFER_SIZE, Constants.MEMORY_ALIGNMENT, SegmentScope.global());

    private static final ByteBuffer BYTE_BUFFER = BASE_MEMORY.asByteBuffer();

    public final Kryo kryo = new Kryo();

    public final UnsafeOutput onHeapOutput;
    public final UnsafeInput onHeapInput;

    private final UnsafeByteBufferOutput offHeapOutput;
    private final UnsafeByteBufferInput offHeapInput;

    public KryoState() {

        // Prepare on-heap output
        onHeapOutput = new UnsafeOutput(
                Constants.STATIC_BUFFER_SIZE, Constants.STATIC_BUFFER_SIZE
        );

        // Prepare on-heap input
        onHeapInput = new UnsafeInput(onHeapOutput.getBuffer());

        // Prepare off-heap output
        offHeapOutput = new UnsafeByteBufferOutput();
        offHeapOutput.setBuffer(BYTE_BUFFER, Constants.STATIC_BUFFER_SIZE);

        // Prepare off-heap input
        offHeapInput = new UnsafeByteBufferInput(offHeapOutput.getByteBuffer());
    }

    @Override
    protected void onSetup() {
        kryo.setRegistrationRequired(false);
    }

    @Override
    protected void onTeardown() {
        offHeapOutput.dispose();
    }

    @Override
    protected long serializeOnHeap(Object object) {
        kryo.writeObject(onHeapOutput, object);

        var total = onHeapOutput.total();
        onHeapOutput.reset();

        return total;
    }

    @Override
    protected long serializeOffHeap(Object object) {
        kryo.writeObject(offHeapOutput, object);

        var total = offHeapOutput.total();
        offHeapOutput.reset();

        return total;
    }

    @Override
    protected Object deserializeOnHeap(Class<?> clazz) {
        onHeapInput.reset();
        return kryo.readObject(onHeapInput, clazz);
    }

    @Override
    protected Object deserializeOffHeap(Class<?> clazz) {
        offHeapInput.reset();
        return kryo.readObject(offHeapInput, clazz);
    }
}
