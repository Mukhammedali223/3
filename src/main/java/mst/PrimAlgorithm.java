package mst;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

public class PrimAlgorithm {
    // adjacency edge for Prim
    public static class AdjEdge {
        public int u, v;
        public int w;
        public String from, to;
        public AdjEdge(int u, int v, int w, String from, String to) { this.u = u; this.v = v; this.w = w; this.from = from; this.to = to; }
    }

    // Single-run result container
    private static class Single {
        public List<JsonModels.InputEdge> mst;
        public int totalCost;
        public long operations;
        public double timeMs;
    }

    // Run Prim once
    private static Single runOnce(List<List<AdjEdge>> adj, List<String> nodes) {
        int n = nodes.size();
        boolean[] visited = new boolean[n];
        List<JsonModels.InputEdge> mst = new ArrayList<>();
        int totalCost = 0;

        long edgeInspections = 0;
        long heapPushes = 0;
        long heapPops = 0;

        class Item { int w, from, to; Item(int w, int from, int to){ this.w = w; this.from = from; this.to = to; } }

        PriorityQueue<Item> pq = new PriorityQueue<>(Comparator.comparingInt(a -> a.w));

        // start from vertex 0
        visited[0] = true;
        for (AdjEdge e : adj.get(0)) {
            pq.offer(new Item(e.w, e.u, e.v));
            heapPushes++;
            edgeInspections++;
        }

        int added = 0;
        long start = System.nanoTime();
        while (!pq.isEmpty() && added < n - 1) {
            heapPops++;
            Item it = pq.poll();
            if (visited[it.to]) continue;
            visited[it.to] = true;
            mst.add(new JsonModels.InputEdge(nodes.get(it.from), nodes.get(it.to), it.w));

            totalCost += it.w;
            added++;
            for (AdjEdge e : adj.get(it.to)) {
                edgeInspections++;
                if (!visited[e.v]) {
                    pq.offer(new Item(e.w, e.u, e.v));
                    heapPushes++;
                }
            }
        }
        long end = System.nanoTime();

        Single s = new Single();
        s.mst = mst;
        s.totalCost = totalCost;
        s.operations = edgeInspections + heapPushes + heapPops;
        s.timeMs = (end - start) / 1_000_000.0;
        return s;
    }

    // Run averaged over runs times (returns AlgResult with averages and sample MST)
    public static JsonModels.AlgResult runAveraged(List<List<AdjEdge>> adj, List<String> nodes, int runs) {
        double sumTime = 0.0;
        double sumOps = 0.0;
        JsonModels.AlgResult out = new JsonModels.AlgResult();
        for (int i = 0; i < runs; i++) {
            Single s = runOnce(adj, nodes);
            if (i == 0) {
                out.mst_edges = s.mst;
                out.total_cost = s.totalCost;
            } else {
                // sanity check: MST total cost should match first run; if not, still keep first run's MST.
            }
            sumTime += s.timeMs;
            sumOps += s.operations;
        }
        out.execution_time_ms = sumTime / runs;
        out.operations_count = Math.round(sumOps / runs);
        return out;
    }
}
