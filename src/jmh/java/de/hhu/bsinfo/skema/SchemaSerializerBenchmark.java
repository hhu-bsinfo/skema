package de.hhu.bsinfo.skema;

import java.util.concurrent.TimeUnit;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.UnsafeInput;
import com.esotericsoftware.kryo.io.UnsafeOutput;

import de.hhu.bsinfo.skema.data.*;
import org.openjdk.jmh.annotations.*;

import de.hhu.bsinfo.skema.schema.Schema;
import de.hhu.bsinfo.skema.data.BoxedCollection;
import de.hhu.bsinfo.skema.data.PrimitiveCollection;
import de.hhu.bsinfo.skema.data.Result;
import de.hhu.bsinfo.skema.data.Status;
import de.hhu.bsinfo.skema.data.Timestamp;
import de.hhu.bsinfo.skema.schema.SchemaRegistry;

import org.openjdk.jmh.annotations.Measurement;

public class SchemaSerializerBenchmark {

    static {
        SchemaRegistry.register(Timestamp.class);
        SchemaRegistry.register(de.hhu.bsinfo.skema.data.Measurement.class);
        SchemaRegistry.register(PrimitiveCollection.class);
        SchemaRegistry.register(BoxedCollection.class);
        SchemaRegistry.register(Status.class);
        SchemaRegistry.register(Result.class);
        SchemaRegistry.register(Profile.class);
    }

    @State(Scope.Thread)
    public static class KryoState {

        @Param({"0", "1", "2", "3", "4", "5"})
        public int dataIndex;

        private final BenchmarkInput dataSource = new BenchmarkInput();

        public Object data;
        public Class<?> dataClass;

        public final Kryo kryo = new Kryo();
        public final UnsafeOutput output = new UnsafeOutput(1024);

        public UnsafeInput input;

        @Setup(Level.Trial)
        public void setup() {
            data = dataSource.get(dataIndex);
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
    public static class SchemaState {

        @Param({"0", "1", "2", "3", "4", "5"})
        public int dataIndex;

        private final BenchmarkInput dataSource = new BenchmarkInput();

        public Object data;
        public Class<?> dataClass;

        public byte[] buffer;
        public Schema schema;

        @Setup(Level.Trial)
        public void setup() {
            data = dataSource.get(dataIndex);
            dataClass = data.getClass();
            schema = SchemaRegistry.getSchema(dataClass);
            buffer = new byte[schema.getSize(data)];
        }

        @TearDown(Level.Trial)
        public void teardown() {

        }
    }

    @Benchmark
    @BenchmarkMode(Mode.Throughput)
    @Warmup(iterations = 3, time = 1000, timeUnit = TimeUnit.MILLISECONDS)
    @Measurement(iterations = 5, time = 1000, timeUnit = TimeUnit.MILLISECONDS)
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
    public Object deserializeSchema(SchemaState p_state) {
        p_state.data = SchemaSerializer.deserialize(p_state.dataClass, p_state.buffer);
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
    public Object deserializeKryo(KryoState p_state) {
        p_state.data = p_state.kryo.readObject(p_state.input, p_state.dataClass);
        p_state.input.rewind();
        return p_state.data;
    }
}
