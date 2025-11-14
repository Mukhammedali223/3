package mst;

import java.util.concurrent.atomic.AtomicLong;

public class UnionFind {
    private final int[] parent;
    private final int[] rank;
    private final AtomicLong findCount = new AtomicLong(0);
    private final AtomicLong unionCount = new AtomicLong(0);

    public UnionFind(int n) {
        parent = new int[n];
        rank = new int[n];
        for (int i = 0; i < n; i++) parent[i] = i;
    }

    public int find(int x) {
        findCount.incrementAndGet();
        if (parent[x] == x) return x;
        parent[x] = find(parent[x]);
        return parent[x];
    }

    public boolean union(int a, int b) {
        int ra = find(a);
        int rb = find(b);
        if (ra == rb) return false;
        unionCount.incrementAndGet();
        if (rank[ra] < rank[rb]) parent[ra] = rb;
        else if (rank[rb] < rank[ra]) parent[rb] = ra;
        else { parent[rb] = ra; rank[ra]++; }
        return true;
    }

    public long getFindCount() { return findCount.get(); }
    public long getUnionCount() { return unionCount.get(); }
}
