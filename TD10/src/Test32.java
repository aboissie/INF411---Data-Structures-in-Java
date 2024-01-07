
// Test de l'algorithme de Dijkstra bidirectionnel
public class Test32 {
	private final static int source = 190637;
	private final static int sourceBis = 190636;
	private final static int dest = 187333;

	public static void main(String args[]) throws Exception {
		// Pour s'assurer que les assert's sont activés
		if (!Test32.class.desiredAssertionStatus()) {
			System.err.println("Vous devez activer l'option -ea de la JVM");
			System.err.println("(Run As -> Run configurations -> Arguments -> VM Arguments)");
			System.exit(1);
		}

		System.out.println("Test 3.2 : test de l'algorithme de Dijkstra bidirectionnel");

		String name = "USA-road-d-NY";
		Graph g=new Graph("data/" + name + ".gr");
		
		System.out.print("Test de la methode getMinPath()...\t");

		// algorithme de Dijkstra
		Dijkstra forward = new Dijkstra(g, sourceBis, dest);
		int forwardPath = forward.compute();

		Dijkstra backward = new Dijkstra(g.reverse(), dest, sourceBis);
		int backwardPath = backward.compute();

		// algorithme de Dijkstra bidirectionel
		BiDijkstra bothways = new BiDijkstra(g, sourceBis, dest);		
		int bothwaysPath = bothways.compute();

		assert forwardPath == bothwaysPath : "\n\nLongueur de chemin incorrecte\n";
		System.out.println("[OK]");
		
		// resultats
		System.out.println();
		System.out.println("longueur chemin forward    entre " + source + " et " + dest + " = " + forwardPath);
		System.out.println("longueur chemin backward   entre " + source + " et " + dest + " = " + backwardPath);
		System.out.println("longueur chemin bidijkstra entre " + source + " et " + dest + " = " + bothwaysPath);
		
	}
}
