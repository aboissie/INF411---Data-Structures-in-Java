/* TD10. Plus courts chemins */

//Algorithme de Dijkstra bidirectionnel
class BiDijkstra {
	final Graph g; // le graphe de travail
	final int source; // source du plus court chemin recherche
	final int dest; // destination du plus court chemin recherche
	final Dijkstra forward; // recherche de plus courts chemins depuis la source
	final Dijkstra backward; // recherche de plus courts chemins depuis la destination
	Dijkstra currentDijkstra, otherDijkstra; // sens de la prochaine iteration et celui oppose 
	
	private int last; // sommet traite par la derniere iteration
	
	private Fenetre f; // fenetre pour la visualisation
		
	/* Méthodes à compléter */ 
	
	// Question 3.1
	
	// constructeur
	BiDijkstra(Graph g, int source, int dest) {
		this.g = g;
		this.source = source;
		this.dest = dest;
				
		
	}

	// Question 3.1
	
	// changer la direction de recherche
	void flip() {
		throw new Error("Méthode BiDijkstra.flip() à compléter (Question 3.1)");
	}

	// Question 3.1
	
	// une iteration de Dijkstra bidirectionnel
	void oneStep() {
		throw new Error("Méthode BiDijkstra.oneStep() à compléter (Question 3.1)");
	}
	
	// Question 3.1
	
	// test de terminaison
	boolean isOver() {
		throw new Error("Méthode BiDijkstra.isOver() à compléter (Question 3.1)");
	}
		
	// Questions 3.1 et 3.2
	
	// renvoyer la longueur du plus cours chemin
	int getMinPath() {
		throw new Error("Méthode BiDijkstra.getMinPath() à compléter (Questions 3.1 et 3.2)");
	}
	
	// Question 3.1
	
	// algorithme de Dijkstra bidirectionnel
	int compute() {
		throw new Error("Méthode BiDijkstra.compute() à compléter (Question 3.1)");
	}
	
	// Question 4
	
	public int getSteps () {
		throw new Error("Méthode BiDijkstra.getSteps() à compléter (Question 4)");
	}

	/* Méthodes à ne pas modifier */ 
	
	int getLast() { return last; }
	
	public void setFenetre (Fenetre f) {
		this.f = f;
		forward.setFenetre(f);
		backward.setFenetre(f);
	}
	
	public void draw () {
	    g.drawSourceDestination(f, source, dest);
	    g.drawPath(f, forward.getPred(), backward.getPred(), last);
	}
}

