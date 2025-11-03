import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SCCFinderTest {

    private Metrics metrics;
    private SCCFinder sccFinder;

    @BeforeEach
    void setUp() {
        metrics = new Metrics();
        sccFinder = new SCCFinder(metrics);
    }

    @Test
    void testSimpleCycle() {
        Graph g = new Graph(3, true);
        g.addEdge(0, 1, 1);
        g.addEdge(1, 2, 1);
        g.addEdge(2, 0, 1);

        sccFinder.findSCCs(g);
        List<List<Integer>> sccs = sccFinder.getSccList();

        assertEquals(1, sccs.size());
        assertEquals(3, sccs.get(0).size());
    }

    @Test
    void testTwoComponents() {
        Graph g = new Graph(4, true);
        g.addEdge(0, 1, 1);
        g.addEdge(1, 0, 1);

        g.addEdge(2, 3, 1);
        g.addEdge(3, 2, 1);

        g.addEdge(1, 2, 5);

        sccFinder.findSCCs(g);
        List<List<Integer>> sccs = sccFinder.getSccList();

        assertEquals(2, sccs.size());
    }

    @Test
    void testProvidedData() {
        Graph g = new Graph(8, true);
        g.addEdge(0, 1, 3);
        g.addEdge(1, 2, 2);
        g.addEdge(2, 3, 4);
        g.addEdge(3, 1, 1);
        g.addEdge(4, 5, 2);
        g.addEdge(5, 6, 5);
        g.addEdge(6, 7, 1);

        sccFinder.findSCCs(g);
        List<List<Integer>> sccs = sccFinder.getSccList();

        assertEquals(6, sccs.size());

        boolean foundCycle = false;
        for (List<Integer> scc : sccs) {
            if (scc.size() == 3) {
                foundCycle = true;
                assertTrue(scc.containsAll(List.of(1, 2, 3)));
            }
        }
        assertTrue(foundCycle);
    }
}