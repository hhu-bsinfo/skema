package de.hhu.bsinfo.skema.benchmark;

import de.hhu.bsinfo.skema.benchmark.command.BenchmarkCommand;
import picocli.CommandLine;

@CommandLine.Command(
        name = "benchmark",
        description = "",
        subcommands = {
                BenchmarkCommand.class
        }
)
public final class App {

    private App() {}

    @SuppressWarnings("CallToSystemExit")
    public static void main(String... args) {
        var exitCode = new CommandLine(new App())
                .setCaseInsensitiveEnumValuesAllowed(true)
                .execute(args);

        System.exit(exitCode);
    }
}
