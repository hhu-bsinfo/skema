package de.hhu.bsinfo.skema.benchmark.suite;

import de.hhu.bsinfo.skema.Skema;
import de.hhu.bsinfo.skema.benchmark.state.JavaState;
import de.hhu.bsinfo.skema.benchmark.state.KryoState;
import de.hhu.bsinfo.skema.benchmark.state.SkemaState;
import de.hhu.bsinfo.skema.util.Operation;
import org.openjdk.jmh.annotations.*;

import static de.hhu.bsinfo.skema.benchmark.util.Constants.BENCHMARK_SEED;

public class BenchmarkSuite {

    @Benchmark
    public long serializeKryo(KryoState state) {
        return state.serialize();
    }

    @Benchmark
    public long serializeSkema(SkemaState state) {
        return state.serialize();
    }

    @Benchmark
    public long serializeJava(JavaState state) {
        return state.serialize();
    }
}
