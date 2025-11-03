import java.util.List;

public class Main {
    public static void main(String[] args) {
        String filename = "data/tasks.json";

        if (args.length > 0) {
            filename = args[0];
        }

        System.out.println("Processing file: " + filename);

        Metrics metrics = new Metrics();
        GraphLoader.GraphData graphData = GraphLoader.loadGraph(filename);

        if (graphData == null) {
            System.err.println("Error loading graph.");
            return;
        }

        Graph originalGraph = graphData.graph;
        int originalSource = graphData.source;
        System.out.println("Weight model: " + graphData.weightModel);
        System.out.println("N=" + originalGraph.n + ", M=" + getEdgeCount(originalGraph));

        System.out.println("\n--- 1. SCC Search ---");
        SCCFinder sccFinder = new SCCFinder(metrics);
        CondensationGraph condensationGraph = sccFinder.findSCCs(originalGraph);
        int[] componentMap = sccFinder.getComponentMap();

        System.out.println("\n--- 2. Topological Sort ---");
        TopologicalSorter sorter = new TopologicalSorter(metrics);
        List<Integer> topoOrder = sorter.sort(condensationGraph);

        System.out.println("\n--- 3. DAG Pathfinding ---");
        DAGShortestPath pathFinder = new DAGShortestPath(metrics);

        int sourceComponent = componentMap[originalSource];
        System.out.println("(Original source node " + originalSource +
                " belongs to component " + sourceComponent + ")");

        pathFinder.findShortestPaths(condensationGraph, sourceComponent, topoOrder);

        pathFinder.findLongestPath(condensationGraph, topoOrder);

        System.out.println("\n--- 4. Final Metrics ---");
        metrics.report();
    }

    private static int getEdgeCount(Graph g) {
        int count = 0;
        for (List<Edge> edges : g.adj.values()) {
            count += edges.size();
        }
        return count;
    }
}