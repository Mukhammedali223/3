package mst;

import java.util.ArrayList;
import java.util.List;

public class JsonModels {
    // Input models
    public static class InputRoot { public List<InputGraph> graphs; }
    public static class InputGraph { public int id; public List<String> nodes; public List<InputEdge> edges; }
    public static class InputEdge {
        public String from;
        public String to;
        public int weight;

        // Default constructor required for Gson
        public InputEdge() {}

        // Convenient constructor to create edges easily
        public InputEdge(String from, String to, int weight) {
            this.from = from;
            this.to = to;
            this.weight = weight;
        }
    }

    // Output models
    public static class OutputRoot { public List<Result> results = new ArrayList<>(); }
    public static class Result {
        public int graph_id;
        public InputStats input_stats;
        public AlgResult prim;
        public AlgResult kruskal;
    }
    public static class InputStats { public int vertices; public int edges; }
    public static class AlgResult {
        public List<InputEdge> mst_edges = new ArrayList<>();
        public int total_cost;
        public long operations_count;       // averaged count rounded
        public double execution_time_ms;    // averaged time in ms
    }
}
