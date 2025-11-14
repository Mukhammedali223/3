package mst;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

public class KruskalAlgorithm {
    public static class Edge {
        public int u, v;
        public int w;
        public String from, to;
        public Edge(int u, int v, int w, String from, String to) { this.u = u; this.v = v; this.w = w; this.from = from; this.to = to; }
    }

    private static class Single {
        public List<JsonModels.InputEdge> mst;
        public int totalCost;
        public long operations;
        public double timeMs;
    }

    private static Single runOnce(List<Edge> edges, List<String> nodes) {
        // Copy edges
        List<Edge> edgesCopy = new ArrayList<>(edges);
        AtomicLong sortComparisons = new AtomicLong(0);

        Comparator<Edge> cmp = (a, b) -> {
            sortComparisons.incrementAndGet();
            return Integer.compare(a.w, b.w);
        };

        long start = System.nanoTime();
        edgesCopy.sort(cmp);

        UnionFind dsu = new UnionFind(nodes.size());
        List<JsonModels.InputEdge> mst = new ArrayList<>();
        int totalCost = 0;
        int added = 0;

        for (Edge e : edgesCopy) {
            if (added >= nodes.size() - 1) break;
            int ru = dsu.find(e.u);
            int rv = dsu.find(e.v);
            if (ru != rv) {
                dsu.union(ru, rv);
                mst.add(new JsonModels.InputEdge(nodes.get(e.u), nodes.get(e.v), e.w));

                totalCost += e.w;
                added++;
            }
        }
        long end = System.nanoTime();

        Single s = new Single();
        s.mst = mst;
        s.totalCost = totalCost;
        s.operations = sortComparisons.get() + dsu.getFindCount() + dsu.getUnionCount();
        s.timeMs = (end - start) / 1_000_000.0;
        return s;
    }

    public static JsonModels.AlgResult runAveraged(List<Edge> edges, List<String> nodes, int runs) {
        double sumTime = 0.0;
        double sumOps = 0.0;
        JsonModels.AlgResult out = new JsonModels.AlgResult();
        for (int i = 0; i < runs; i++) {
            Single s = runOnce(edges, nodes);
            if (i == 0) {
                out.mst_edges = s.mst;
                out.total_cost = s.totalCost;
            }
            sumTime += s.timeMs;
            sumOps += s.operations;
        }
        out.execution_time_ms = sumTime / runs;
        out.operations_count = Math.round(sumOps / runs);
        return out;
    }
}
