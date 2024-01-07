
public class Test2bis {	
	public static void main(String args[]) throws Exception {
		System.out.println("Test de l'algorithme de Dijkstra unidirectionel sur un gros graphe");
		
		// preparation de la fenetre
		Graph g=new Graph("data/USA-road-d-NY.gr");
		g.setCoordinates("data/USA-road-d-NY.co");
		Fenetre f;
		f = new Fenetre("data/NY_Metropolitan.png", "Dijkstra", -73.9987, -73.9437, 40.7523, 40.78085);
		g.drawGraph(f);
		f.repaint();

		// algorithme de Dijkstra
		int source = 190637, destination = 187333;
		Dijkstra d = new Dijkstra(g, source, destination);
		d.setFenetre(f);
		System.out.println("plus court chemin entre " + source + " et " + destination + " = " + d.compute());
		d.draw();
	}
}
