package de.hhu.bsinfo.skema.benchmark.state;

import com.esotericsoftware.kryo.Kryo;
import de.hhu.bsinfo.skema.benchmark.util.ByteBufferOutputStream;
import de.hhu.bsinfo.skema.benchmark.util.Constants;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.SegmentScope;
import java.nio.ByteBuffer;

public class JavaState extends BaseState {

    private static final MemorySegment BASE_MEMORY = MemorySegment.allocateNative(
            Constants.STATIC_BUFFER_SIZE, Constants.MEMORY_ALIGNMENT, SegmentScope.global());

    private static final ByteBuffer BYTE_BUFFER = BASE_MEMORY.asByteBuffer();

    private final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(Constants.STATIC_BUFFER_SIZE);
    private final ByteBufferOutputStream byteBufferOutputStream = new ByteBufferOutputStream(BYTE_BUFFER);

    public final ObjectOutputStream onHeapOutputStream;
    private final ObjectOutputStream offHeapOutputStream;

    public JavaState() {
        try {
            onHeapOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            offHeapOutputStream = new ObjectOutputStream(byteBufferOutputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void onSetup() {

    }

    @Override
    protected void onTeardown() {
        try {
            onHeapOutputStream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected long serializeOnHeap(Object object) {
        try {
            byteArrayOutputStream.reset();
            onHeapOutputStream.reset();
            onHeapOutputStream.writeObject(object);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return byteArrayOutputStream.size();
    }

    @Override
    protected long serializeOffHeap(Object object) {
        try {
            BYTE_BUFFER.rewind();
            offHeapOutputStream.reset();
            offHeapOutputStream.writeObject(object);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return BYTE_BUFFER.position();
    }

    @Override
    protected Object deserializeOnHeap(Class<?> clazz) {
        return null;
    }

    @Override
    protected Object deserializeOffHeap(Class<?> clazz) {
        return null;
    }
}
