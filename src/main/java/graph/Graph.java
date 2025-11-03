// Файл: src/main/java/graph/Graph.java

package graph;

import java.util.ArrayList;
import java.util.List;

// Общий класс для представления графа
public class Graph {
    public int V;
    public List<List<Edge>> adj;

    // Структура ребра
    // Сделан 'public static', чтобы к нему можно было обращаться как Graph.Edge
    public static class Edge {
        public int target;
        public int weight;

        public Edge(int target, int weight) {
            this.target = target;
            this.weight = weight;
        }
    }

    // Конструктор Графа
    public Graph(int V) {
        this.V = V;
        adj = new ArrayList<>(V);
        for (int i = 0; i < V; ++i)
            adj.add(new ArrayList<>());
    }

    // Метод добавления ребра (он понадобится)
    public void addEdge(int u, int v, int w) {
        adj.get(u).add(new Edge(v, w));
    }
}