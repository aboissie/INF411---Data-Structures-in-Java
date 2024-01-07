// Noeud d'un graphe
class Node implements Comparable<Object> {

	final int id; // identifiant
	final int val; // valeur

	// constructeur
	Node(int id, int val) {
		this.id = id;
		this.val = val;
	}

	@Override
	// fonction de comparaison
	public int compareTo(Object o) {
		Node that = (Node) o;

		if (this.val == that.val)
			return (this.id - that.id);

		return this.val - that.val;
	}

	@Override
	public boolean equals(Object o) {
		if (o == null || !(o instanceof Node))
			return false;

		Node that = (Node) o;
		return id == that.id && val == that.val;
	}
	
	@Override
	public String toString() {
		return "(" + id + ", " + val + ")";
	}
}
