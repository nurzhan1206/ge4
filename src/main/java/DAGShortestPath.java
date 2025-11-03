import java.util.*;

public class DAGShortestPath {

    private Metrics metrics;

    public DAGShortestPath(Metrics metrics) {
        this.metrics = metrics;
    }

    public void findShortestPaths(Graph dag, int sourceNode, List<Integer> topoOrder) {
        metrics.startTimer();
        int n = dag.n;
        long[] dist = new long[n];
        int[] parent = new int[n];
        Arrays.fill(dist, Long.MAX_VALUE);
        Arrays.fill(parent, -1);

        dist[sourceNode] = 0;

        for (int u : topoOrder) {
            if (dist[u] == Long.MAX_VALUE) continue;

            for (Edge edge : dag.adj.get(u)) {
                int v = edge.to;
                int weight = edge.weight;

                metrics.increment("SP_Relaxations");
                if (dist[v] > dist[u] + weight) {
                    dist[v] = dist[u] + weight;
                    parent[v] = u;
                }
            }
        }
        metrics.stopTimer("DAG_ShortestPath");

        System.out.println("--- Shortest Paths from component " + sourceNode + " ---");
        for (int i = 0; i < n; i++) {
            String d = (dist[i] == Long.MAX_VALUE) ? "UNREACHABLE" : String.valueOf(dist[i]);
            System.out.println("  To " + i + ": " + d);
        }
        reconstructPath(parent, sourceNode, n-1, "Shortest path to (n-1):");
    }

    public void findLongestPath(Graph dag, List<Integer> topoOrder) {
        metrics.startTimer();
        int n = dag.n;
        long[] dist = new long[n];
        int[] parent = new int[n];
        Arrays.fill(dist, 0);
        Arrays.fill(parent, -1);

        long maxLength = 0;
        int endNode = -1;

        for (int u : topoOrder) {
            for (Edge edge : dag.adj.get(u)) {
                int v = edge.to;
                int weight = edge.weight;

                metrics.increment("LP_Relaxations");
                if (dist[v] < dist[u] + weight) {
                    dist[v] = dist[u] + weight;
                    parent[v] = u;

                    if (dist[v] > maxLength) {
                        maxLength = dist[v];
                        endNode = v;
                    }
                }
            }
        }
        metrics.stopTimer("DAG_LongestPath");

        System.out.println("--- Critical (Longest) Path ---");
        System.out.println("Length: " + maxLength);
        reconstructPath(parent, -1, endNode, "Path (by components):");
    }

    private void reconstructPath(int[] parent, int start, int end, String title) {
        if (end == -1) {
            System.out.println(title + " (Path not found)");
            return;
        }
        List<Integer> path = new ArrayList<>();
        int curr = end;
        while (curr != -1 && curr != start) {
            path.add(curr);
            curr = parent[curr];
        }
        if (curr == start) path.add(start);

        Collections.reverse(path);

        if (path.isEmpty() || (start != -1 && path.get(0) != start)) {
            System.out.println(title + " (Path does not exist)");
        } else {
            System.out.println(title + " " + path);
        }
    }
}