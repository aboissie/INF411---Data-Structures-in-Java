
public class Test31bis {
	private final static int source = 190637;
	private final static int dest = 187333;

	public static void main(String[] args) throws Exception {
		String name = "USA-road-d-NY";
		Graph g = new Graph("data/" + name + ".gr");
		g.setCoordinates("data/" + name + ".co");
				
		// algorithmes de Dijkstra dans les deux sens
		Dijkstra forward = new Dijkstra(g, source, dest);
		Dijkstra backward = new Dijkstra(g.reverse(), dest, source);

		// algorithme de Dijkstra bidirectionnel naif
		BiDijkstra bothways = new BiDijkstra(g, source, dest);
		
		int forwardPath = forward.compute();
		int backwardPath = backward.compute();

		Fenetre f;
		f = new Fenetre("data/NY_Metropolitan.png", "Dijkstra", -73.9987, -73.9437, 40.7523, 40.78085);
		g.drawGraph(f);
		f.repaint();

		bothways.setFenetre(f);
		int bothwaysPath = bothways.compute();
		bothways.draw();

		// resultats
		System.out.println("longueur chemin forward    entre " + source + " et " + dest + " = " + forwardPath);
		System.out.println("longueur chemin backward   entre " + source + " et " + dest + " = " + backwardPath);
		System.out.println("longueur chemin BiDijkstra entre " + source + " et " + dest + " = " + bothwaysPath);
	}
}
