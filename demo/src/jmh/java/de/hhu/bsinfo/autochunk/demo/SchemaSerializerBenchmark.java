package de.hhu.bsinfo.autochunk.demo;

import java.util.concurrent.TimeUnit;

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

public class SchemaSerializerBenchmark {

    static {
        SchemaSerializer.register(Timestamp.class);
    }

    @State(Scope.Thread)
    public static class SerializerState {

        public final Timestamp data = new Timestamp(100, 100, new int[]{1, 2, 3}, new long[]{6, 8, 3});
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
    @Warmup(iterations = 1, time = 1000, timeUnit = TimeUnit.MILLISECONDS)
    @Measurement(iterations = 5, time = 5000, timeUnit = TimeUnit.MILLISECONDS)
    @OutputTimeUnit(TimeUnit.SECONDS)
    @Threads(12)
    public byte[] serialize(SerializerState p_state) {
        int size = p_state.schema.getSize(p_state.data);
        byte[] buffer = new byte[size];
        SchemaSerializer.serialize(p_state.data, buffer);
        return buffer;
    }

    @Benchmark
    @BenchmarkMode(Mode.Throughput)
    @Warmup(iterations = 1, time = 1000, timeUnit = TimeUnit.MILLISECONDS)
    @Measurement(iterations = 5, time = 5000, timeUnit = TimeUnit.MILLISECONDS)
    @OutputTimeUnit(TimeUnit.SECONDS)
    @Threads(12)
    public Timestamp deserialize(SerializerState p_state) {
        SchemaSerializer.deserialize(p_state.data, p_state.buffer);
        return p_state.data;
    }
}
