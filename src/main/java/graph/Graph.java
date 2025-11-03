import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Класс для ребра
class Edge {
    int to;
    int weight;
    public Edge(int to, int weight) {
        this.to = to;
        this.weight = weight;
    }
}

// Класс для графа
class Graph {
    int n; // Число вершин
    Map<Integer, List<Edge>> adj; // Списки смежности
    boolean directed;

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
        // Если граф неор., добавить обратное ребро
        if (!directed) {
            adj.get(v).add(new Edge(u, w));
        }
    }

    // Метод для получения графа-транспозиции (нужен для Kosaraju)
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