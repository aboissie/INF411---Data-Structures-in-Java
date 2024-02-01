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
				
		forward = new Dijkstra(g, source, dest);
		backward = new Dijkstra(g.reverse(), dest, source);

		currentDijkstra = forward;
		otherDijkstra = backward;

		last = source;
	}

	// Question 3.1
	
	// changer la direction de recherche
	void flip() {
		Dijkstra tmp = this.currentDijkstra;
		this.currentDijkstra = this.otherDijkstra;
		this.otherDijkstra = tmp;
	}

	// Question 3.1
	
	// une iteration de Dijkstra bidirectionnel
	void oneStep() {
		this.currentDijkstra.oneStep();
		this.otherDijkstra.oneStep();
		this.last = this.currentDijkstra.unsettled.peek().id;
	}
	
	// Question 3.1
	
	// test de terminaison
	boolean isOver() {
		return this.forward.settled[this.last] && this.backward.settled[this.last];
	}
		
	// Questions 3.1 et 3.2
	// renvoyer la longueur du plus cours chemin
	int getMinPath() {
		
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

