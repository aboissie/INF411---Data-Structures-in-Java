
// Test de la classe Graph
public class Test0 {
	public static void main(String[] args) throws Exception {
		System.out.println("Test 0 : test de la classe Graph");
		
		Graph g;

		// petit graphe
		g = new Graph("data/mini.gr");
//		System.out.println(g);

		// gros graphe
		g = new Graph("data/USA-road-d-NY.gr");
		g.setCoordinates("data/USA-road-d-NY.co");
		Fenetre f;
		f = new Fenetre("data/NY_Metropolitan.png", "Dijkstra", -73.9987, -73.9437, 40.7523, 40.78085);
		g.drawGraph(f);
	}
}
