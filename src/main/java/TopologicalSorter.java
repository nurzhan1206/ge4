import java.util.*;

public class TopologicalSorter {

    private Stack<Integer> stack;
    private boolean[] visited;
    private Metrics metrics;

    public TopologicalSorter(Metrics metrics) {
        this.metrics = metrics;
    }

    private void dfs(Graph dag, int u) {
        visited[u] = true;
        metrics.increment("Topo_DFS_Visits");

        for (Edge edge : dag.adj.get(u)) {
            metrics.increment("Topo_DFS_Edges");
            if (!visited[edge.to]) {
                dfs(dag, edge.to);
            }
        }
        stack.push(u);
    }

    public List<Integer> sort(Graph dag) {
        metrics.startTimer();
        stack = new Stack<>();
        visited = new boolean[dag.n];

        for (int i = 0; i < dag.n; i++) {
            if (!visited[i]) {
                dfs(dag, i);
            }
        }

        List<Integer> topologicalOrder = new ArrayList<>();
        while (!stack.isEmpty()) {
            topologicalOrder.add(stack.pop());
        }
        metrics.stopTimer("TopologicalSort");

        System.out.println("Topological order of components: " + topologicalOrder);
        return topologicalOrder;
    }
}