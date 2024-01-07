/* TD10. Plus courts chemins */

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.concurrent.LinkedBlockingDeque;

// Algorithme de Dijkstra
class Dijkstra {
	final Graph g; // le graphe de travail
	final int source; // source du plus court chemin recherche
	final int dest; // destination du plus court chemin recherche
	private Fenetre f; // fenetre pour la visualisation
	public int[] dist;
	public int[] pred;
	public boolean[] settled;
	public PriorityQueue<Node> unsettled;

	// Questions 1.1, 1.2 et 4
	
	/* Méthodes à compléter */
	
	// Questions 1.1 et 1.2
	
	// constructeur
	Dijkstra(Graph g, int source, int dest) {
		this.g = g;
		this.source = source;
		this.dest = dest;

		this.dist = new int[g.nbVertices];
		this.settled = new boolean[g.nbVertices];
		this.pred = new int[g.nbVertices];
		
		for(int i = 0; i < g.nbVertices; i++){
			this.dist[i] = Integer.MAX_VALUE;
			this.pred[i] = -1;
			this.settled[i] = false;
		}

		this.dist[source] = 0;
		this.pred[source] = source;

		this.unsettled = new PriorityQueue<>();
		this.unsettled.add(new Node(source, 0));
	}
	
	// Question 2.1 et 2.2

	// mise a jour de la distance, de la priorite, et du predecesseur d'un sommet
	void update(int succ, int current) {
		if(this.dist[current] + this.g.weight(current, succ) < this.dist[succ]){
			this.dist[succ] = this.dist[current] + this.g.weight(current, succ);
			this.pred[succ] = current;

			this.unsettled.add(new Node(succ, this.dist[current] + this.g.weight(current, succ)));
			g.drawUnsettledPoint(f, succ);
		}
	}
	
	// Question 2.1

	// trouve le prochain sommet de unsettled non traite
	int nextNode() {
		if(this.unsettled.isEmpty()) return -1;
		int v = this.unsettled.poll().id;
		if(this.settled[v]) return this.nextNode();
		return v;
	}
	
	// Questions 2.1, 2.2 et 4

	// une etape de l'algorithme Dijkstra
	int oneStep() {
		slow();
		int current = this.nextNode();
		if(current == -1) return -1;
		
		for(int succ:this.g.successors(current)){
			if(!this.settled[succ]) this.update(succ, current);
		}
		
		this.settled[current] = true;
		g.drawSettledPoint(f, current);
		return current;
	}
	
	// Question 2.1

	// algorithme de Dijkstra complet
	int compute() {
		while(!unsettled.isEmpty() && !this.settled[dest]) oneStep();		
		return (this.settled[dest])? this.dist[dest]:-1;
	}
	
	// Question 4
	
	public int getSteps() {
		throw new Error("Méthode Dijkstra.getSteps() à implémenter (Question 4)");
				
	}
	
	/* Méthodes à ne pas changer */
	
	// ralentisseur visualisation
	void slow(){
		if(f == null) return;
		try {
			Thread.sleep(5);
		} catch (InterruptedException e) {}
	}
	
	void setFenetre (Fenetre f) { this.f = f; }

	// Cette fonction vérifie si le vecteur 'int[] name' est 
	// présent dans la classe et le renvoie. Sinon, renvoie null 
	private int[] getIntArray(String name) {
		Field field = null;
		for (Field f : getClass().getDeclaredFields()) {
			if (f.getName().equals(name)) {
				field = f;
				break;
			}
		}
		if (field == null)
			return null;
		
		int[] result = null;
		try {
			result = (int[]) field.get(this);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public int[] getPred() {
		return getIntArray("pred");
	}
	
	public int[] getDist() {
		return getIntArray("dist");
	}	
		
	public void draw () {
		g.drawSourceDestination(f, source, dest);
		g.drawPath(f, getPred(), dest);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null || !(obj instanceof Dijkstra))
			return false;
		
		Dijkstra that = (Dijkstra) obj;
		return g.equals(that.g) && source == that.source && dest == that.dest;
	}
}
