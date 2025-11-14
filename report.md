Analytical Report: Optimization of a City Transportation Network (MST)
1. Summary of Input Data and Algorithm Results

We tested two graphs provided in input_example.json. The algorithms implemented were Prim’s algorithm and Kruskal’s algorithm. The results, including execution time and operation counts, are summarized below:

| Graph ID | Algorithm | Vertices | Edges | MST Edges | Total Cost | Operations Count | Execution Time (ms) |
| -------- | --------- | -------- | ----- | --------- | ---------- | ---------------- | ------------------- |
| 1        | Prim      | 5        | 7     | 4         | 16         | 26               | 0.01914             |
| 1        | Kruskal   | 5        | 7     | 4         | 16         | 37               | 1.08234             |
| 2        | Prim      | 4        | 5     | 3         | 6          | 18               | 0.0147              |
| 2        | Kruskal   | 4        | 5     | 3         | 6          | 25               | 0.04214             |

Notes:

MST Edges indicates the number of edges in the Minimum Spanning Tree.

Execution time is measured in milliseconds.

2. Comparison of Prim’s and Kruskal’s Algorithms

Total Cost Consistency:

Both algorithms produce identical total costs for all graphs, confirming the correctness of MST construction.

Efficiency and Operations:

Prim’s algorithm tends to perform fewer operations in these examples (26 vs. 37 for graph 1; 18 vs. 25 for graph 2).

Execution time is significantly faster for Prim on small graphs due to simpler edge selection and adjacency representation.

Kruskal shows higher operation count, as it requires sorting all edges and performing union-find operations.

Graph Structure Differences:

Although total costs are equal, the specific edges included in MST may differ because multiple MSTs are possible in graphs with edges of equal weight.

3. Conclusions

Prim’s algorithm is preferable for dense graphs where adjacency matrices or lists allow quick selection of minimal edges.

Kruskal’s algorithm performs efficiently on sparse graphs, especially when edges can be sorted once, and union-find operations are fast.

For small graphs, both algorithms produce similar results with negligible execution time differences.

Implementation complexity is slightly higher for Kruskal due to the union-find structure.