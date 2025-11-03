import com.google.gson.Gson;
import java.io.FileReader;
import java.io.Reader;

public class GraphLoader {

    private static class JsonEdge {
        int u;
        int v;
        int w;
    }

    private static class JsonGraph {
        boolean directed;
        int n;
        JsonEdge[] edges;
        int source;
        String weight_model;
    }

    public static GraphData loadGraph(String filePath) {
        Gson gson = new Gson();
        try (Reader reader = new FileReader(filePath)) {
            JsonGraph jsonGraph = gson.fromJson(reader, JsonGraph.class);

            Graph g = new Graph(jsonGraph.n, jsonGraph.directed);
            for (JsonEdge edge : jsonGraph.edges) {
                g.addEdge(edge.u, edge.v, edge.w);
            }

            return new GraphData(g, jsonGraph.source, jsonGraph.weight_model);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static class GraphData {
        public final Graph graph;
        public final int source;
        public final String weightModel;

        public GraphData(Graph graph, int source, String weightModel) {
            this.graph = graph;
            this.source = source;
            this.weightModel = weightModel;
        }
    }
}