package de.hhu.bsinfo.skema.benchmark.state;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.unsafe.UnsafeByteBufferInput;
import com.esotericsoftware.kryo.unsafe.UnsafeByteBufferOutput;
import com.esotericsoftware.kryo.unsafe.UnsafeInput;
import com.esotericsoftware.kryo.unsafe.UnsafeOutput;
import de.hhu.bsinfo.skema.benchmark.util.Constants;

public class KryoState extends BaseState {

    public final Kryo kryo = new Kryo();

    public final UnsafeOutput onHeapOutput = new UnsafeOutput(
            Constants.STATIC_BUFFER_SIZE, Constants.STATIC_BUFFER_SIZE
    );

    public final UnsafeInput onHeapInput = new UnsafeInput(onHeapOutput.getBuffer());

    public final UnsafeByteBufferOutput offHeapOutput = new UnsafeByteBufferOutput(
            Constants.STATIC_BUFFER_SIZE, Constants.STATIC_BUFFER_SIZE
    );

    public final UnsafeByteBufferInput offHeapInput = new UnsafeByteBufferInput(offHeapOutput.getByteBuffer());


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
