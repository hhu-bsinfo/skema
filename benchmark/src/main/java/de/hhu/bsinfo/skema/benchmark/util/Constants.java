package de.hhu.bsinfo.skema.benchmark.util;

public final class Constants {

    public static final long BENCHMARK_SEED = 1786321781782361892L;

    public static final int STATIC_BUFFER_SIZE = 1024 * 1024; // 1KB

    public static final int MEMORY_ALIGNMENT = 4096; // 4KB (Page-Aligned)

    public static final class MemoryType {
        public static final String ON_HEAP = "on-heap";
        public static final String OFF_HEAP = "off-heap";
    }
}
