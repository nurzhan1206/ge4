import java.util.*;

public class SCCFinder {

    private Stack<Integer> stack;
    private boolean[] visited;
    private int[] component;
    private int sccCount;
    private List<List<Integer>> sccList;
    private Metrics metrics;
    private Map<Integer, List<Integer>> componentNodeMap;

    public SCCFinder(Metrics metrics) {
        this.metrics = metrics;
    }

    private void fillOrder(Graph g, int u) {
        visited[u] = true;
        metrics.increment("SCC_DFS_Visits");
        for (Edge edge : g.adj.get(u)) {
            metrics.increment("SCC_DFS_Edges");
            if (!visited[edge.to]) {
                fillOrder(g, edge.to);
            }
        }
        stack.push(u);
    }

    private void collectSCC(Graph gT, int u) {
        visited[u] = true;
        metrics.increment("SCC_Transpose_DFS_Visits");
        component[u] = sccCount;
        sccList.get(sccCount).add(u);
        componentNodeMap.get(sccCount).add(u);

        for (Edge edge : gT.adj.get(u)) {
            metrics.increment("SCC_Transpose_DFS_Edges");
            if (!visited[edge.to]) {
                collectSCC(gT, edge.to);
            }
        }
    }

    public CondensationGraph findSCCs(Graph originalGraph) {
        metrics.startTimer();
        stack = new Stack<>();
        visited = new boolean[originalGraph.n];

        for (int i = 0; i < originalGraph.n; i++) {
            if (!visited[i]) {
                fillOrder(originalGraph, i);
            }
        }

        Graph gT = originalGraph.getTranspose();

        Arrays.fill(visited, false);
        component = new int[originalGraph.n];
        sccList = new ArrayList<>();
        componentNodeMap = new HashMap<>();
        sccCount = 0;

        while (!stack.isEmpty()) {
            int u = stack.pop();
            if (!visited[u]) {
                sccList.add(new ArrayList<>());
                componentNodeMap.put(sccCount, new ArrayList<>());
                collectSCC(gT, u);
                sccCount++;
            }
        }

        CondensationGraph condensationGraph = new CondensationGraph(sccCount, true);
        condensationGraph.setComponentMap(componentNodeMap);
        Set<String> edgesAdded = new HashSet<>();

        for (int u = 0; u < originalGraph.n; u++) {
            for (Edge edge : originalGraph.adj.get(u)) {
                int v = edge.to;
                int sccU = component[u];
                int sccV = component[v];

                if (sccU != sccV) {
                    String edgeKey = sccU + "->" + sccV;
                    if (edgesAdded.add(edgeKey)) {
                        condensationGraph.addEdge(sccU, sccV, edge.weight);
                    }
                }
            }
        }
        metrics.stopTimer("SCC_and_Condensation");

        System.out.println("Found " + sccCount + " Strongly Connected Components:");
        for (List<Integer> scc : sccList) {
            System.out.println("  Component: " + scc + ", Size: " + scc.size());
        }

        return condensationGraph;
    }

    public List<List<Integer>> getSccList() { return sccList; }
    public int[] getComponentMap() { return component; }
}