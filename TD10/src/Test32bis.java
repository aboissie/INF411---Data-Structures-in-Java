
public class Test32bis {
	private final static int source = 190637;
	private final static int sourceBis = 190636;
	private final static int dest = 187333;

	public static void main(String[] args) throws Exception {
		String name = "USA-road-d-NY";
		Graph g=new Graph("data/" + name + ".gr");
		g.setCoordinates("data/" + name + ".co");
		
		
		// preparation de la fenetre
		Fenetre f;
		f = new Fenetre("data/NY_Metropolitan.png", "Dijkstra", -73.9987, -73.9437, 40.7523, 40.78085);
		g.drawGraph(f);
		f.repaint();

		// algorithme de Dijkstra
		Dijkstra forward = new Dijkstra(g, sourceBis, dest);
		int forwardPath = forward.compute();
		Dijkstra backward = new Dijkstra(g.reverse(), dest, sourceBis);
		int backwardPath = backward.compute();

		// algorithme de Dijkstra bidirectionel
		BiDijkstra bothways = new BiDijkstra(g, sourceBis, dest);
		
		bothways.setFenetre(f);
		int bothwaysPath = bothways.compute();
		bothways.draw();
		
		// resultats
		System.out.println("longueur chemin forward    entre " + source + " et " + dest + " = " + forwardPath);
		System.out.println("longueur chemin backward   entre " + source + " et " + dest + " = " + backwardPath);
		System.out.println("longueur chemin bidijkstra entre " + source + " et " + dest + " = " + bothwaysPath);
	}
}
