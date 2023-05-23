package de.hhu.bsinfo.skema.benchmark.suite;

import de.hhu.bsinfo.skema.Skema;
import de.hhu.bsinfo.skema.benchmark.state.KryoState;
import de.hhu.bsinfo.skema.benchmark.state.SkemaState;
import de.hhu.bsinfo.skema.util.Operation;
import org.openjdk.jmh.annotations.Benchmark;

public class BenchmarkSuite {

    @Benchmark
    public long serializeKryo(KryoState state) {
        return state.serialize();
    }

    @Benchmark
    public long serializeSkema(SkemaState state) {
        return state.serialize();
    }

//    @Benchmark
//    public byte[] serializeSkemaFullOnHeap(SkemaState p_state) {
//        Skema.serialize(p_state.data, p_state.buffer);
//        return p_state.buffer;
//    }
//
//    @Benchmark
//    public byte[] serializeSkemaPartialOnHeap(SkemaState p_state) {
//        p_state.operation.rewind();
//        for (int i = 0; i < p_state.buffer.length; i++) {
//            Skema.serialize(p_state.operation, p_state.buffer, i, 1);
//        }
//        return p_state.buffer;
//    }
//
//    @Benchmark
//    public int serializeSkemaFullOffHeap(SkemaState p_state) {
//        int bytesWritten = Skema.serialize(p_state.data, p_state.offHeapAddress);
//        return bytesWritten;
//    }
//
//    @Benchmark
//    public Operation serializeSkemaPartialOffHeap(SkemaState p_state) {
//        p_state.operation.rewind();
//        for (int i = 0; i < p_state.buffer.length; i++) {
//            Skema.serialize(p_state.operation, p_state.offHeapAddress + i, 1);
//        }
//        return p_state.operation;
//    }
//
//    @Benchmark
//    public Object deserializeSkemaFullOnHeap(SkemaState p_state) {
//        p_state.data = Skema.deserialize(p_state.dataClass, p_state.buffer);
//        return p_state.data;
//    }
//
//    @Benchmark
//    public Object deserializeSkemaPartialOnHeap(SkemaState p_state) {
//        p_state.operation.rewind();
//        for (int i = 0; i < p_state.buffer.length; i++) {
//            Skema.deserialize(p_state.operation, p_state.buffer, i, 1);
//        }
//        return p_state.data;
//    }
//
//    @Benchmark
//    public Object deserializeSkemaFullOffHeap(SkemaState p_state) {
//        p_state.data = Skema.deserialize(p_state.dataClass, p_state.offHeapAddress);
//        return p_state.data;
//    }
//
//    @Benchmark
//    public Object deserializeSkemaPartialOffHeap(SkemaState p_state) {
//        p_state.operation.rewind();
//        for (int i = 0; i < p_state.buffer.length; i++) {
//            Skema.deserialize(p_state.operation, p_state.offHeapAddress + i , 1);
//        }
//        return p_state.data;
//    }
//
//    @Benchmark
//    public byte[] serializeKryo(KryoState p_state) {
//        p_state.kryo.writeObject(p_state.output, p_state.data);
//        p_state.output.reset();
//        return p_state.output.getBuffer();
//    }
//
//    @Benchmark
//    public Object deserializeKryo(KryoState p_state) {
//        p_state.data = p_state.kryo.readObject(p_state.input, p_state.dataClass);
//        p_state.input.reset();
//        return p_state.data;
//    }
}
