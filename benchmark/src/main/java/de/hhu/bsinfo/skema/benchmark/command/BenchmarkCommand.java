package de.hhu.bsinfo.skema.benchmark.command;

import lombok.extern.slf4j.Slf4j;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.results.format.ResultFormatType;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.openjdk.jmh.runner.options.TimeValue;
import picocli.CommandLine;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@CommandLine.Command(
        name = "run",
        description = "Runs the benchmark suite."
)
public class BenchmarkCommand implements Runnable {

    private static final int[] DEFAULT_THREAD_COUNT = { 1 };

    private static final String DEFAULT_PREFIX = "benchmark";

    private static DateTimeFormatter FORMATTER = DateTimeFormatter
            .ofPattern("uuuuMMddHHmmss")
            .withZone(ZoneId.of("UTC"));

    @CommandLine.Option(
            names = "--include",
            split = ",",
            description = "The names of benchmarks to execute")
    private List<String> includes = Collections.emptyList();

    @CommandLine.Option(
            names = {"-p", "--prefix"},
            description = "Prefix used for output file(s)")
    private String prefix = DEFAULT_PREFIX;

    @CommandLine.Option(
            names = {"-t", "--threads"},
            split = ",",
            description = "The server to connect to")
    private int[] threads = DEFAULT_THREAD_COUNT;

    @Override
    public void run() {

        // Execute benchmarks for each number of threads
        for (var threadCount : threads) {

            // Prepare output file name
            var outputFile = String.format("%s_%s_%d-threads.csv",
                    prefix, FORMATTER.format(Instant.now()), threadCount);

            // Create base options
            var options = new OptionsBuilder()
                    .resultFormat(ResultFormatType.CSV)
                    .result(outputFile)
                    .threads(threadCount)
                    .forks(1)
                    .warmupIterations(3)
                    .warmupTime(TimeValue.seconds(2))
                    .measurementIterations(5)
                    .measurementTime(TimeValue.seconds(3))
                    .mode(Mode.Throughput)
                    .timeUnit(TimeUnit.SECONDS)
                    .detectJvmArgs();

            // Include the specified benchmarks
            for (var benchmark : includes) {
                options = options.include(benchmark);
            }

            try {
                new Runner(options.build()).run();
            } catch (RunnerException e) {
                log.error("Running benchmark(s) failed", e);
            }
        }
    }
}
