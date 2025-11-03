import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class Metrics {
    private final Map<String, Long> counters = new HashMap<>();
    private final Map<String, Long> timers = new HashMap<>();
    private long startTime;

    public void startTimer() {
        startTime = System.nanoTime();
    }

    public void stopTimer(String operationName) {
        long endTime = System.nanoTime();
        timers.put(operationName, endTime - startTime);
    }

    public void increment(String counterName) {
        counters.put(counterName, counters.getOrDefault(counterName, 0L) + 1);
    }

    public long getCounter(String counterName) {
        return counters.getOrDefault(counterName, 0L);
    }

    public void report() {
        System.out.println("--- Metrics Report ---");

        System.out.println("Execution Time (in ms):");
        timers.forEach((name, nanoTime) -> {
            System.out.printf("  %s: %.4f ms\n",
                    name, (double)nanoTime / TimeUnit.MILLISECONDS.toNanos(1));
        });

        System.out.println("Operation Counters:");
        counters.forEach((name, count) -> {
            System.out.printf("  %s: %d\n", name, count);
        });
        System.out.println("-------------------------");
    }
}