
// arc d'un graphe
public class Edge {
	int origin; // origine de l'arc
	int destination; // destination de l'arc
	
	// constructeur
	public Edge(int origin, int destination) {
		this.origin = origin;
		this.destination = destination;
	}

	// redefinition de la fonction equals
	public boolean equals(Object o) {
		Edge that = (Edge)o;
		return this.origin == that.origin && this.destination == that.destination;
	}
	
	// redefinition du hashCode
	public int hashCode() {
		return (Graph.cHash * this.origin) + this.destination;
	}
	
}
