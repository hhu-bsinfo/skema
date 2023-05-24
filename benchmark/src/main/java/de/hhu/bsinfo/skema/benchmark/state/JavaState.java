package de.hhu.bsinfo.skema.benchmark.state;

import de.hhu.bsinfo.skema.benchmark.util.ByteBufferInputStream;
import de.hhu.bsinfo.skema.benchmark.util.ByteBufferOutputStream;
import de.hhu.bsinfo.skema.benchmark.util.Constants;

import java.io.*;
import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;
import java.nio.ByteBuffer;

public class JavaState extends BaseState {

    private static final int OBJECT_STREAM_HEADER_SIZE = 2 * Short.BYTES;

    private final Arena arena = Arena.openConfined();

    private final MemorySegment baseSegment = MemorySegment.allocateNative(
            Constants.STATIC_BUFFER_SIZE, Constants.MEMORY_ALIGNMENT, arena.scope());

    private final ByteBuffer byteBuffer = baseSegment.asByteBuffer();

    private final byte[] byteArray = new byte[Constants.STATIC_BUFFER_SIZE];

    private final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(Constants.STATIC_BUFFER_SIZE);

    private final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArray);

    private final ByteBufferInputStream byteBufferInputStream = new ByteBufferInputStream(byteBuffer);
    private final ByteBufferOutputStream byteBufferOutputStream = new ByteBufferOutputStream(byteBuffer);

    private final ObjectInputStream onHeapInputStream;
    private final ObjectOutputStream onHeapOutputStream;

    private final ObjectInputStream offHeapInputStream;
    private final ObjectOutputStream offHeapOutputStream;



    public JavaState() {
        try {
            onHeapOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            offHeapOutputStream = new ObjectOutputStream(byteBufferOutputStream);

            // Place MAGIC and version at the beginning of the buffer to trick input stream
            ByteBuffer.wrap(byteArray).putShort((short) 0xaced).putShort((short) 5).rewind();
            byteBuffer.putShort((short) 0xaced).putShort((short) 5).rewind();

            onHeapInputStream = new ObjectInputStream(byteArrayInputStream);
            offHeapInputStream = new ObjectInputStream(byteBufferInputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void onSetup(Object benchmarkObject) {
        try {
            // Serialize benchmark object using Java's serialization mechanism
            onHeapOutputStream.writeObject(benchmarkObject);
            offHeapOutputStream.writeObject(benchmarkObject);
            var serializedObject = byteArrayOutputStream.toByteArray();

            // Reset streams
            offHeapOutputStream.reset();
            onHeapOutputStream.reset();
            byteArrayOutputStream.reset();

            // Copy serialized form into on-heap input stream buffer
            System.arraycopy(serializedObject, 0, byteArray, 0, serializedObject.length);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void onTeardown() {
        try {
            // Close streams
            onHeapOutputStream.close();
            offHeapOutputStream.close();

            // Release memory
            arena.close();
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
            byteBuffer.rewind();
            offHeapOutputStream.reset();
            offHeapOutputStream.writeObject(object);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return byteBuffer.position();
    }

    @Override
    protected Object deserializeOnHeap(Class<?> clazz) {
        try {
            byteArrayInputStream.reset();
            byteArrayInputStream.skip(OBJECT_STREAM_HEADER_SIZE);
            return onHeapInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected Object deserializeOffHeap(Class<?> clazz) {
        try {
            byteBuffer.position(OBJECT_STREAM_HEADER_SIZE);
            return offHeapInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
