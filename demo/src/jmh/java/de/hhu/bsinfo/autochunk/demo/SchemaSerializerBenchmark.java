package de.hhu.bsinfo.autochunk.demo;

import java.util.concurrent.TimeUnit;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.UnsafeInput;
import com.esotericsoftware.kryo.io.UnsafeOutput;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.TearDown;
import org.openjdk.jmh.annotations.Threads;
import org.openjdk.jmh.annotations.Warmup;

import de.hhu.bsinfo.autochunk.demo.data.Timestamp;
import de.hhu.bsinfo.autochunk.demo.schema.Schema;

public class SchemaSerializerBenchmark {

    static {
        SchemaSerializer.register(Timestamp.class);
    }

    @State(Scope.Thread)
    public static class KryoState {

        public final Kryo kryo = new Kryo();
        public final UnsafeOutput output = new UnsafeOutput(128);

        public Timestamp data = new Timestamp(100, 100, new int[]{1, 2, 3}, new long[]{6, 8, 3});
        public UnsafeInput input;

        @Setup(Level.Trial)
        public void setup() {
            kryo.writeObject(output, data);
            input = new UnsafeInput(output.getBuffer());
            output.clear();
        }

        @TearDown(Level.Trial)
        public void teardown() {

        }
    }

    @State(Scope.Thread)
    public static class SchemaState {

        public Timestamp data = new Timestamp(100, 100, new int[]{1, 2, 3}, new long[]{6, 8, 3});
        public byte[] buffer;
        public Schema schema;

        @Setup(Level.Trial)
        public void setup() {
            schema = SchemaSerializer.getSchema(Timestamp.class);
            buffer = new byte[schema.getSize(data)];
        }

        @TearDown(Level.Trial)
        public void teardown() {

        }
    }

    @Benchmark
    @BenchmarkMode(Mode.Throughput)
    @Warmup(iterations = 3, time = 1000, timeUnit = TimeUnit.MILLISECONDS)
    @Measurement(iterations = 5, time = 5000, timeUnit = TimeUnit.MILLISECONDS)
    @OutputTimeUnit(TimeUnit.SECONDS)
    @Threads(1)
    public byte[] serializeSchema(SchemaState p_state) {
        int size = p_state.schema.getSize(p_state.data);
        byte[] buffer = new byte[size];
        SchemaSerializer.serialize(p_state.data, buffer);
        return buffer;
    }

    @Benchmark
    @BenchmarkMode(Mode.Throughput)
    @Warmup(iterations = 3, time = 1000, timeUnit = TimeUnit.MILLISECONDS)
    @Measurement(iterations = 5, time = 5000, timeUnit = TimeUnit.MILLISECONDS)
    @OutputTimeUnit(TimeUnit.SECONDS)
    @Threads(1)
    public Timestamp deserializeSchema(SchemaState p_state) {
        p_state.data = SchemaSerializer.deserialize(Timestamp.class, p_state.buffer);
        return p_state.data;
    }

    @Benchmark
    @BenchmarkMode(Mode.Throughput)
    @Warmup(iterations = 3, time = 1000, timeUnit = TimeUnit.MILLISECONDS)
    @Measurement(iterations = 5, time = 5000, timeUnit = TimeUnit.MILLISECONDS)
    @OutputTimeUnit(TimeUnit.SECONDS)
    @Threads(1)
    public byte[] serializeKryo(KryoState p_state) {
        p_state.kryo.writeObject(p_state.output, p_state.data);
        p_state.output.clear();
        return p_state.output.getBuffer();
    }

    @Benchmark
    @BenchmarkMode(Mode.Throughput)
    @Warmup(iterations = 3, time = 1000, timeUnit = TimeUnit.MILLISECONDS)
    @Measurement(iterations = 5, time = 5000, timeUnit = TimeUnit.MILLISECONDS)
    @OutputTimeUnit(TimeUnit.SECONDS)
    @Threads(1)
    public Timestamp deserializeKryo(KryoState p_state) {
        p_state.data = p_state.kryo.readObject(p_state.input, Timestamp.class);
        p_state.input.rewind();
        return p_state.data;
    }
}
