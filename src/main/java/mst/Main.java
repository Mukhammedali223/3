package mst;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileWriter;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Main {
    // Configuration: number of runs to average
    private static final int RUNS = 5;
    private static final String INPUT_FILE = "input_example.json";
    private static final String OUTPUT_FILE = "output_results.json";

    public static void main(String[] args) throws Exception {
        String json = new String(Files.readAllBytes(Paths.get(INPUT_FILE)));
        Gson gson = new Gson();
        JsonModels.InputRoot inRoot = gson.fromJson(json, JsonModels.InputRoot.class);

        if (inRoot == null || inRoot.graphs == null) {
            System.err.println("Input JSON missing 'graphs' field or file is empty.");
            return;
        }

        JsonModels.OutputRoot outRoot = new JsonModels.OutputRoot();

        for (JsonModels.InputGraph ig : inRoot.graphs) {
            JsonModels.Result res = new JsonModels.Result();
            res.graph_id = ig.id;
            res.input_stats = new JsonModels.InputStats();
            res.input_stats.vertices = ig.nodes.size();
            res.input_stats.edges = ig.edges.size();

            // map node name -> index
            Map<String, Integer> idx = new HashMap<>();
            for (int i = 0; i < ig.nodes.size(); i++) idx.put(ig.nodes.get(i), i);

            // build adjacency and edge list
            List<List<PrimAlgorithm.AdjEdge>> adj = new ArrayList<>();
            for (int i = 0; i < ig.nodes.size(); i++) adj.add(new ArrayList<>());
            List<KruskalAlgorithm.Edge> edgeList = new ArrayList<>();

            for (JsonModels.InputEdge ie : ig.edges) {
                int u = idx.get(ie.from);
                int v = idx.get(ie.to);
                // undirected
                adj.get(u).add(new PrimAlgorithm.AdjEdge(u, v, ie.weight, ie.from, ie.to));
                adj.get(v).add(new PrimAlgorithm.AdjEdge(v, u, ie.weight, ie.to, ie.from));
                edgeList.add(new KruskalAlgorithm.Edge(u, v, ie.weight, ie.from, ie.to));
            }

            // Run Prim averaged over RUNS
            JsonModels.AlgResult primResult = PrimAlgorithm.runAveraged(adj, ig.nodes, RUNS);
            // Run Kruskal averaged over RUNS
            JsonModels.AlgResult kruskalResult = KruskalAlgorithm.runAveraged(edgeList, ig.nodes, RUNS);

            res.prim = primResult;
            res.kruskal = kruskalResult;
            outRoot.results.add(res);
        }

        Gson pretty = new GsonBuilder().setPrettyPrinting().create();
        try (Writer w = new FileWriter(OUTPUT_FILE)) {
            pretty.toJson(outRoot, w);
        }

        System.out.println("Done. Output written to " + OUTPUT_FILE);
    }
}
