package de.hhu.bsinfo.skema.util;

import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

public class RandomUtil {

    private RandomUtil() {}

    public static long randomLong() {
        return randomLong(Long.MIN_VALUE, Long.MAX_VALUE);
    }

    public static long randomLong(long p_origin, long p_bound) {
        return ThreadLocalRandom.current().nextLong(p_origin, p_bound);
    }

    public static double randomDouble() {
        return randomDouble(Double.MIN_VALUE, Double.MAX_VALUE);
    }

    public static double randomDouble(double p_origin, double p_bound) {
        return ThreadLocalRandom.current().nextDouble(p_origin, p_bound);
    }

    public static int randomInt() {
        return randomInt(Integer.MIN_VALUE, Integer.MAX_VALUE);
    }

    public static int randomInt(int p_origin, int p_bound) {
        return ThreadLocalRandom.current().nextInt(p_origin, p_bound);
    }

    public static long[] randomLongArray(int p_length) {
        return LongStream.generate(() -> RandomUtil.randomLong(Long.MIN_VALUE, Long.MAX_VALUE))
                .limit(randomInt(1, p_length))
                .toArray();
    }

    public static int[] randomIntArray(int p_length) {
        return IntStream.generate(() -> RandomUtil.randomInt(Integer.MIN_VALUE, Integer.MAX_VALUE))
                .limit(randomInt(1, p_length))
                .toArray();
    }
}
