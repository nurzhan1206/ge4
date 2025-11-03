import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;

public class DAGShortestPathTest {

    private Metrics metrics;
    private Graph graph;
    private CondensationGraph condensationGraph;
    private List<Integer> topoOrder;
    private int sourceComponent;

    @BeforeEach
    void setUp() {
        metrics = new Metrics();

        Graph originalGraph = new Graph(8, true);
        originalGraph.addEdge(0, 1, 3);
        originalGraph.addEdge(1, 2, 2);
        originalGraph.addEdge(2, 3, 4);
        originalGraph.addEdge(3, 1, 1);
        originalGraph.addEdge(4, 5, 2);
        originalGraph.addEdge(5, 6, 5);
        originalGraph.addEdge(6, 7, 1);

        int originalSource = 4;

        SCCFinder sccFinder = new SCCFinder(metrics);
        condensationGraph = sccFinder.findSCCs(originalGraph);

        TopologicalSorter sorter = new TopologicalSorter(metrics);
        topoOrder = sorter.sort(condensationGraph);

        sourceComponent = sccFinder.getComponentMap()[originalSource];
    }

    @Test
    void testShortestPathsExecution() {
        DAGShortestPath pathFinder = new DAGShortestPath(metrics);
        pathFinder.findShortestPaths(condensationGraph, sourceComponent, topoOrder);
    }

    @Test
    void testLongestPathExecution() {
        DAGShortestPath pathFinder = new DAGShortestPath(metrics);
        pathFinder.findLongestPath(condensationGraph, topoOrder);
    }
}