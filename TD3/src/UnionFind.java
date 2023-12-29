class UnionFind{
    private int[] link;
	private int[] rank;
	private int[] cardinality; 

    UnionFind(int n){
        if (n < 0)
			throw new IllegalArgumentException();

		link = new int[n];
		cardinality = new int[n];
		for (int i = 0; i < n; i++) {
			link[i] = i;
			cardinality[i] = 1;
		}
		rank = new int[n];
    }

    int find(int i) {
		if (i < 0 || i >= link.length)
			throw new ArrayIndexOutOfBoundsException(i);
		int p = link[i];
		if (p == i)
			return i;
		int r = find(p);
		this.link[i] = r;
		return r;
	}

    void union(int i, int j) {
		int ri = find(i);
		int rj = find(j);
		if (ri == rj)
			return;
		if (rank[ri] < rank[rj]) {
			link[ri] = rj;
			cardinality[rj] += cardinality[ri];
			cardinality[ri] = 0;
		} else {
			link[rj] = ri;
			cardinality[ri] += cardinality[rj];
			cardinality[rj] = 0;
			if (rank[ri] == rank[rj])
				rank[ri]++;
		}
	}

    public int classCardinality(int i) {
		return cardinality[find(i)];
	}
}