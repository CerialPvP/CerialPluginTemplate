package top.faved.particlelib;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import java.util.TreeMap;

public class MathUtils {
    private static final TreeMap<Double, Double> sinValues = new TreeMap<>();
    private static final TreeMap<Double, Double> cosValues = new TreeMap<>();
    private static final int THREADS = Runtime.getRuntime().availableProcessors();
    private static final Logger logger = LoggerFactory.getLogger("ParticleLib");
    public static final int accuracy = 7;

    /**
     * Precalculates all sin/cos values for panning.<br>
     * <b>NOTE:</b> This should be called on plugin load, and then never to be called again.
     */
    public static void precalculateSinCos() {
        long t = System.currentTimeMillis();
        ExecutorService executor = Executors.newFixedThreadPool(THREADS);
        logger.info("Precalculating sin/cos values with {} decimal accuracy...", accuracy);

        double step = 360.0d / Math.pow(10, accuracy);
        double angleRangePerThread = 360.0d / THREADS;

        for (int i = 0; i < THREADS; i++) {
            double startAngle = i * angleRangePerThread;
            double endAngle = (i + 1) * angleRangePerThread;
            executor.submit(() -> {
                for (double angle = startAngle; angle < endAngle; angle += step) {
                    double rad = Math.toRadians(angle);
                    double sin = Math.sin(rad);
                    double cos = Math.cos(rad);
                    sinValues.put(angle, sin);
                    cosValues.put(angle, cos);
                }
            });
        }

        executor.shutdown();
        try {
            //noinspection ResultOfMethodCallIgnored
            executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException ignored) {}

        logger.info("Calculations have been completed in {}ms.", System.currentTimeMillis() - t);
    }

    public static double getSin(double angle) {
        Double floorKey = sinValues.floorKey(angle);
        Double ceilingKey = sinValues.ceilingKey(angle);

        if (floorKey == null && ceilingKey == null) {
            return 0; // Or throw an exception, but this shouldn't happen if precalculation is correct
        } else if (floorKey == null) {
            return sinValues.get(ceilingKey);
        } else if (ceilingKey == null) {
            return sinValues.get(floorKey);
        } else {
            double diffFloor = Math.abs(angle - floorKey);
            double diffCeiling = Math.abs(angle - ceilingKey);

            if (diffFloor <= diffCeiling) {
                return sinValues.get(floorKey);
            } else {
                return sinValues.get(ceilingKey);
            }
        }
    }

    public static double getCos(double angle) {
        Double floorKey = cosValues.floorKey(angle);
        Double ceilingKey = cosValues.ceilingKey(angle);

        if (floorKey == null && ceilingKey == null) {
            return 0; // Or throw an exception
        } else if (floorKey == null) {
            return cosValues.get(ceilingKey);
        } else if (ceilingKey == null) {
            return cosValues.get(floorKey);
        } else {
            double diffFloor = Math.abs(angle - floorKey);
            double diffCeiling = Math.abs(angle - ceilingKey);

            if (diffFloor <= diffCeiling) {
                return cosValues.get(floorKey);
            } else {
                return cosValues.get(ceilingKey);
            }
        }
    }
}