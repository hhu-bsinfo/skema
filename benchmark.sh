#!/bin/bash
./gradlew --no-daemon skema:benchmarkJar && java -cp skema/build/libs/skema-benchmark.jar de.hhu.bsinfo.skema.SkemaBenchmark
