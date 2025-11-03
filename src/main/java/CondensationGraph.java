import java.util.List;
import java.util.Map;

public class CondensationGraph extends Graph {

    private Map<Integer, List<Integer>> componentMap;

    public CondensationGraph(int n, boolean directed) {
        super(n, directed);
    }

    public void setComponentMap(Map<Integer, List<Integer>> map) {
        this.componentMap = map;
    }

    public List<Integer> getOriginalNodes(int componentId) {
        return componentMap.get(componentId);
    }
}