import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TopologicalSorterTest {

    private Metrics metrics;
    private TopologicalSorter sorter;

    @BeforeEach
    void setUp() {
        metrics = new Metrics();
        sorter = new TopologicalSorter(metrics);
    }

    @Test
    void testSimpleLineGraph() {
        Graph dag = new Graph(3, true);
        dag.addEdge(0, 1, 1);
        dag.addEdge(1, 2, 1);

        List<Integer> order = sorter.sort(dag);

        assertEquals(3, order.size());
        assertEquals(0, order.get(0));
        assertEquals(1, order.get(1));
        assertEquals(2, order.get(2));
    }

    @Test
    void testDiamondGraph() {
        Graph dag = new Graph(4, true);
        dag.addEdge(0, 1, 1);
        dag.addEdge(0, 2, 1);
        dag.addEdge(1, 3, 1);
        dag.addEdge(2, 3, 1);

        List<Integer> order = sorter.sort(dag);

        assertEquals(4, order.size());
        assertEquals(0, order.get(0));
        assertEquals(3, order.get(3));

        boolean correctMiddle = (order.get(1) == 1 && order.get(2) == 2) ||
                (order.get(1) == 2 && order.get(2) == 1);
        assertTrue(correctMiddle);
    }
}