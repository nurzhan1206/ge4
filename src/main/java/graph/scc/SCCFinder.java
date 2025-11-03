package ru.university.smartcity.graph.scc;

import java.util.*;

// ... (импорты, включая Graph, Edge)

public class SCCFinder {

    private Stack<Integer> stack;
    private boolean[] visited;
    private int[] component; // component[i] = ID компонента для вершины i
    private int sccCount;
    private List<List<Integer>> sccList; // Результат [cite: 13]

    // 1-й проход DFS для заполнения стека (по порядку времени выхода)
    private void fillOrder(Graph g, int u) {
        visited[u] = true;
        for (Edge edge : g.adj.get(u)) {
            if (!visited[edge.to]) {
                fillOrder(g, edge.to);
            }
        }
        stack.push(u);
    }

    // 2-й проход DFS на транспонированном графе
    private void collectSCC(Graph gT, int u) {
        visited[u] = true;
        component[u] = sccCount;
        sccList.get(sccCount).add(u);

        for (Edge edge : gT.adj.get(u)) {
            if (!visited[edge.to]) {
                collectSCC(gT, edge.to);
            }
        }
    }

    /**
     * Находит SCC и строит граф конденсации.
     * [cite: 10, 14]
     */
    public Graph findSCCs(Graph originalGraph) {
        stack = new Stack<>();
        visited = new boolean[originalGraph.n];

        // 1. Первый проход DFS на исходном графе
        for (int i = 0; i < originalGraph.n; i++) {
            if (!visited[i]) {
                fillOrder(originalGraph, i);
            }
        }

        // 2. Получаем транспонированный граф
        Graph gT = originalGraph.getTranspose();

        // 3. Второй проход DFS на gT
        Arrays.fill(visited, false);
        component = new int[originalGraph.n];
        sccList = new ArrayList<>();
        sccCount = 0;

        while (!stack.isEmpty()) {
            int u = stack.pop();
            if (!visited[u]) {
                sccList.add(new ArrayList<>()); // Начинаем новый SCC
                collectSCC(gT, u);
                sccCount++;
            }
        }

        (DAG)
        Graph condensationGraph = new Graph(sccCount, true);
        Set<String> edgesAdded = new HashSet<>(); // Для избежания дубликатов ребер

        for (int u = 0; u < originalGraph.n; u++) {
            for (Edge edge : originalGraph.adj.get(u)) {
                int v = edge.to;
                int sccU = component[u];
                int sccV = component[v];

                // Если ребро соединяет РАЗНЫЕ компоненты
                if (sccU != sccV) {
                    String edgeKey = sccU + "->" + sccV;
                    // (Здесь можно добавить логику выбора веса,
                    // например, мин/макс. Пока просто берем вес ребра)
                    if (edgesAdded.add(edgeKey)) {
                        // Модель весов - "edge"
                        condensationGraph.addEdge(sccU, sccV, edge.weight);
                    }
                }
            }
        }

        // Вывод результатов [cite: 13]
        System.out.println("Найдено " + sccCount + " сильно связанных компонент:");
        for (List<Integer> scc : sccList) {
            System.out.println("Компонент: " + scc + ", Размер: " + scc.size());
        }

        return condensationGraph;
    }

    public List<List<Integer>> getSccList() { return sccList; }
    public int[] getComponentMap() { return component; }
}