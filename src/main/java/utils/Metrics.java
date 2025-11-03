package utils;

public class Metrics {
    private long startTime;
    private long endTime;
    private int dfsVisits = 0; // Для КСС (Kosaraju/Tarjan)
    private int kahnQueueOps = 0; // Для Топологической сортировки (Kahn: pops/pushes)
    private int relaxations = 0; // Для Кратчайших путей в ОАГ

    public void startTimer() {
        this.startTime = System.nanoTime();
    }

    public void stopTimer() {
        this.endTime = System.nanoTime();
    }

    public double getTimeInSeconds() {
        return (endTime - startTime) / 1_000_000_000.0;
    }

    public void incrementDfsVisits() {
        dfsVisits++;
    }

    public void incrementKahnQueueOps() {
        kahnQueueOps++;
    }

    public void incrementRelaxations() {
        relaxations++;
    }

    @Override
    public String toString() {
        return String.format(
                "Time: %.6f s\n" +
                        "DFS/Visits (Kosaraju): %d\n" +
                        "Kahn Queue Operations: %d\n" +
                        "Relaxations (DAG-SP): %d",
                getTimeInSeconds(), dfsVisits, kahnQueueOps, relaxations
        );
    }
}
