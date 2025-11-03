import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class DataGenerator {

    // Класс для ребер в JSON
    private static class JsonEdge {
        int u;
        int v;
        int w;
        JsonEdge(int u, int v, int w) {
            this.u = u;
            this.v = v;
            this.w = w;
        }
    }

    // Класс для всей структуры JSON (вместо анонимного объекта)
    // Это исправляет ошибку "Illegal self reference"
    private static class JsonData {
        boolean directed = true;
        int n;
        List<JsonEdge> edges;
        int source;
        String weight_model = "edge";

        JsonData(int n, List<JsonEdge> edges, int source) {
            this.n = n;
            this.edges = edges;
            this.source = source;
        }
    }

    public static void generateAndSave(String path, int n, int maxEdges, boolean forceCycle) {
        List<JsonEdge> edges = new ArrayList<>();
        Set<String> edgeSet = new HashSet<>();
        Random rand = new Random();

        int edgeCount = Math.min(maxEdges, n * (n - 1));

        if (forceCycle && n >= 3) {
            int u = rand.nextInt(n);
            int v = rand.nextInt(n);
            int w = rand.nextInt(n);
            while (u == v) v = rand.nextInt(n);
            while (w == u || w == v) w = rand.nextInt(n);

            edges.add(new JsonEdge(u, v, rand.nextInt(10) + 1));
            edges.add(new JsonEdge(v, w, rand.nextInt(10) + 1));
            edges.add(new JsonEdge(w, u, rand.nextInt(10) + 1));
            edgeSet.add(u + "->" + v);
            edgeSet.add(v + "->" + w);
            edgeSet.add(w + "->" + u);
        }

        while (edges.size() < edgeCount) {
            int u = rand.nextInt(n);
            int v = rand.nextInt(n);
            if (u != v && edgeSet.add(u + "->" + v)) {
                edges.add(new JsonEdge(u, v, rand.nextInt(10) + 1));
            }
        }

        // Создаем объект с данными
        int sourceNode = rand.nextInt(n);
        JsonData data = new JsonData(n, edges, sourceNode);

        // Сохраняем
        try (FileWriter writer = new FileWriter(path)) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            gson.toJson(data, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        System.out.println("Generating datasets...");

        generateAndSave("data/small_dag.json", 8, 10, false);
        generateAndSave("data/small_cycle_1.json", 10, 15, true);
        generateAndSave("data/small_cycle_2.json", 9, 20, true);

        generateAndSave("data/medium_1.json", 15, 30, true);
        generateAndSave("data/medium_2.json", 20, 50, true);
        generateAndSave("data/medium_3.json", 18, 40, false);

        generateAndSave("data/large_1.json", 30, 100, true);
        generateAndSave("data/large_2.json", 40, 150, true);
        generateAndSave("data/large_3.json", 50, 300, true);

        System.out.println("Generation complete.");
    }
}