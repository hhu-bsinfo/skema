package de.hhu.bsinfo.skema;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.UnsafeInput;
import com.esotericsoftware.kryo.io.UnsafeOutput;

import de.hhu.bsinfo.skema.data.*;
import org.openjdk.jmh.annotations.*;

import de.hhu.bsinfo.skema.data.BoxedCollection;
import de.hhu.bsinfo.skema.data.PrimitiveCollection;
import de.hhu.bsinfo.skema.data.Result;
import de.hhu.bsinfo.skema.data.Status;
import de.hhu.bsinfo.skema.util.Operation;
import de.hhu.bsinfo.skema.util.UnsafeProvider;

import org.openjdk.jmh.results.format.ResultFormatType;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.openjdk.jmh.runner.options.TimeValue;

public class SkemaBenchmark {

    private static final sun.misc.Unsafe UNSAFE = UnsafeProvider.getUnsafe();

    private static final long BENCHMARK_SEED = 1786321781782361892L;

    private static final boolean ASSERT = false;

    static {
        Skema.register(PrimitiveCollection.class);
        Skema.register(BoxedCollection.class);
        Skema.register(Result.class);
        Skema.register(Status.class);
        Skema.register(TextMessage.class);
        Skema.register(Message.class);
    }

    @State(Scope.Thread)
    public static class KryoState {

        @Param({"primitive", "boxed", "polymorphic", "enum"})
        public String type;

        private final BenchmarkInput dataSource = new BenchmarkInput(BENCHMARK_SEED);

        public Object data;
        public Class<?> dataClass;

        public final Kryo kryo = new Kryo();
        public final UnsafeOutput output = new UnsafeOutput(1024 * 1024);

        public UnsafeInput input;

        @Setup(Level.Trial)
        public void setup() {
            data = dataSource.get(type);
            dataClass = data.getClass();
            kryo.writeObject(output, data);
            input = new UnsafeInput(output.getBuffer());
            output.clear();
        }

        @TearDown(Level.Trial)
        public void teardown() {

        }
    }

    @State(Scope.Thread)
    public static class SkemaState {

        @Param({"primitive", "boxed", "polymorphic", "enum"})
        public String type;

        private final BenchmarkInput dataSource = new BenchmarkInput(BENCHMARK_SEED);

        public Object data;
        public Class<?> dataClass;
        public long offHeapAddress;

        public byte[] buffer;
        public Operation operation;

        public byte[] expectedBuffer;
        public Object expectedObject;

        @Setup(Level.Trial)
        public void setup() {
            // Get data for this run
            data = dataSource.get(type);
            operation = new Operation(data);
            dataClass = data.getClass();
            int size = Skema.sizeOf(data);

            // Create buffers for serialization/deserialization
            buffer = new byte[size];
            offHeapAddress = UNSAFE.allocateMemory(buffer.length);

            // Fill buffers with dummy data
            Skema.serialize(data, buffer);
            Skema.serialize(data, offHeapAddress);

            // Create expected object and buffer for assertion
            expectedBuffer = new byte[size];
            expectedObject = Skema.newInstance(data.getClass());
            Skema.serialize(data, expectedBuffer);
            Skema.deserialize(expectedObject, expectedBuffer);
        }

        @TearDown(Level.Trial)
        public void teardown() {
            // Release allocated Memory
            UNSAFE.freeMemory(offHeapAddress);
        }
    }

    @Benchmark
    public byte[] serializeSkemaFullOnHeap(SkemaState p_state) {
        Skema.serialize(p_state.data, p_state.buffer);
        if(ASSERT) { assertArrayEqual(p_state.expectedBuffer, p_state.buffer); }
        return p_state.buffer;
    }

    @Benchmark
    public byte[] serializeSkemaPartialOnHeap(SkemaState p_state) {
        p_state.operation.rewind();
        for (int i = 0; i < p_state.buffer.length; i++) {
            Skema.serialize(p_state.operation, p_state.buffer, i, 1);
        }
        if(ASSERT) { assertArrayEqual(p_state.expectedBuffer, p_state.buffer); }
        return p_state.buffer;
    }

    @Benchmark
    public int serializeSkemaFullOffHeap(SkemaState p_state) {
        int bytesWritten = Skema.serialize(p_state.data, p_state.offHeapAddress);
        if(ASSERT) { assertArrayEqual(p_state.expectedBuffer, p_state.offHeapAddress); }
        return bytesWritten;
    }

    @Benchmark
    public Operation serializeSkemaPartialOffHeap(SkemaState p_state) {
        p_state.operation.rewind();
        for (int i = 0; i < p_state.buffer.length; i++) {
            Skema.serialize(p_state.operation, p_state.offHeapAddress + i, 1);
        }
        if(ASSERT) { assertArrayEqual(p_state.expectedBuffer, p_state.offHeapAddress); }
        return p_state.operation;
    }

    @Benchmark
    public Object deserializeSkemaFullOnHeap(SkemaState p_state) {
        p_state.data = Skema.deserialize(p_state.dataClass, p_state.buffer);
        if(ASSERT) { assertObjectEquals(p_state.expectedObject, p_state.data); }
        return p_state.data;
    }

    @Benchmark
    public Object deserializeSkemaPartialOnHeap(SkemaState p_state) {
        p_state.operation.rewind();
        for (int i = 0; i < p_state.buffer.length; i++) {
            Skema.deserialize(p_state.operation, p_state.buffer, i, 1);
        }
        if(ASSERT) { assertObjectEquals(p_state.expectedObject, p_state.data); }
        return p_state.data;
    }

    @Benchmark
    public Object deserializeSkemaFullOffHeap(SkemaState p_state) {
        p_state.data = Skema.deserialize(p_state.dataClass, p_state.offHeapAddress);
        if(ASSERT) { assertObjectEquals(p_state.expectedObject, p_state.data); }
        return p_state.data;
    }

    @Benchmark
    public Object deserializeSkemaPartialOffHeap(SkemaState p_state) {
        p_state.operation.rewind();
        for (int i = 0; i < p_state.buffer.length; i++) {
            Skema.deserialize(p_state.operation, p_state.offHeapAddress + i , 1);
        }
        if(ASSERT) { assertObjectEquals(p_state.expectedObject, p_state.data); }
        return p_state.data;
    }

    @Benchmark
    public byte[] serializeKryo(KryoState p_state) {
        p_state.kryo.writeObject(p_state.output, p_state.data);
        p_state.output.clear();
        return p_state.output.getBuffer();
    }

    @Benchmark
    public Object deserializeKryo(KryoState p_state) {
        p_state.data = p_state.kryo.readObject(p_state.input, p_state.dataClass);
        p_state.input.rewind();
        return p_state.data;
    }

    private static void assertArrayEqual(final byte[] p_first, final byte[] p_second) {
        if (p_first.length != p_second.length) {
            throw new AssertionError();
        }

        for (int i = 0; i < p_first.length; i++) {
            if (p_first[i] != p_second[i]) {
                throw new AssertionError();
            }
        }
    }

    private static void assertArrayEqual(final byte[] p_array, final long p_address) {
        for (int i = 0; i < p_array.length; i++) {
            if (p_array[i] != UNSAFE.getByte(p_address + i)) {
                throw new AssertionError();
            }
        }
    }

    private static void assertObjectEquals(final Object p_first, final Object p_second) {
        if (!p_first.equals(p_second)) {
            throw new AssertionError();
        }
    }

    private static void printWarning() {
        if (ASSERT) {
            System.out.println();
            System.out.println("##############################################################");
            System.out.println("#                                                            #");
            System.out.println("#                         WARNING                            #");
            System.out.println("#      ------------------------------------------------      #");
            System.out.println("#                 Assertions are enabled.                    #");
            System.out.println("#               Results should NOT be used!                  #");
            System.out.println("#                                                            #");
            System.out.println("##############################################################");
            System.out.println();
        }
    }

    public static void main(String[] p_args) throws RunnerException {
        Options options = new OptionsBuilder()
                .include(SkemaBenchmark.class.getSimpleName())
                .warmupIterations(3)
                .warmupTime(TimeValue.seconds(5))
                .measurementIterations(5)
                .measurementTime(TimeValue.seconds(10))
                .threads(24)
                .forks(5)
                .mode(Mode.Throughput)
                .timeUnit(TimeUnit.SECONDS)
                .resultFormat(ResultFormatType.CSV)
                .result("skema_result.csv")
                .build();

        printWarning();

        new Runner(options).run();

        printWarning();
    }
}
