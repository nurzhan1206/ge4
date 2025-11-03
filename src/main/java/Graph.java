import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Graph {
    public final int n;
    public final Map<Integer, List<Edge>> adj;
    public final boolean directed;

    public Graph(int n, boolean directed) {
        this.n = n;
        this.directed = directed;
        this.adj = new HashMap<>();
        for (int i = 0; i < n; i++) {
            adj.put(i, new ArrayList<>());
        }
    }

    public void addEdge(int u, int v, int w) {
        adj.get(u).add(new Edge(v, w));
        if (!directed) {
            adj.get(v).add(new Edge(u, w));
        }
    }

    public Graph getTranspose() {
        Graph gT = new Graph(n, directed);
        for (int u = 0; u < n; u++) {
            for (Edge edge : adj.get(u)) {
                gT.addEdge(edge.to, u, edge.weight);
            }
        }
        return gT;
    }
}