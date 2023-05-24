package de.hhu.bsinfo.skema.benchmark.state;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.unsafe.UnsafeByteBufferInput;
import com.esotericsoftware.kryo.unsafe.UnsafeByteBufferOutput;
import com.esotericsoftware.kryo.unsafe.UnsafeInput;
import com.esotericsoftware.kryo.unsafe.UnsafeOutput;
import de.hhu.bsinfo.skema.benchmark.util.Constants;

import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;
import java.nio.ByteBuffer;

public class KryoState extends BaseState {

    private final Arena arena = Arena.openConfined();

    private final MemorySegment baseSegment = MemorySegment.allocateNative(
            Constants.STATIC_BUFFER_SIZE, Constants.MEMORY_ALIGNMENT, arena.scope());

    private final ByteBuffer byteBuffer = baseSegment.asByteBuffer();

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
        offHeapOutput.setBuffer(byteBuffer, Constants.STATIC_BUFFER_SIZE);

        // Prepare off-heap input
        offHeapInput = new UnsafeByteBufferInput(offHeapOutput.getByteBuffer());
    }

    @Override
    protected void onSetup(Object benchmarkObject) {
        kryo.setRegistrationRequired(false);

        // Fill inputs with dummy data for deserialization
        kryo.writeObject(onHeapOutput, benchmarkObject);
        kryo.writeObject(offHeapOutput, benchmarkObject);
    }

    @Override
    protected void onTeardown() {
        // Release memory
        arena.close();
    }

    @Override
    protected long serializeOnHeap(Object object) {
        onHeapOutput.reset();
        kryo.writeObject(onHeapOutput, object);

        return onHeapOutput.total();
    }

    @Override
    protected long serializeOffHeap(Object object) {
        offHeapOutput.reset();
        kryo.writeObject(offHeapOutput, object);

        return offHeapOutput.total();
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
