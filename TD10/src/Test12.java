// Test de l'initialisation de la classe Dijkstra
public class Test12 {
	public static void main(String args[]) throws Exception {
		// Pour s'assurer que les assert's sont activés
		if (!Test12.class.desiredAssertionStatus()) {
			System.err.println("Vous devez activer l'option -ea de la JVM");
			System.err
					.println("(Run As -> Run configurations -> Arguments -> VM Arguments)");
			System.exit(1);
		}
		
		System.out.println("Test 1.2 : Initialisation de la classe Dijkstra");
		Graph g = new Graph("data/mini.gr");
		Dijkstra d = new Dijkstra(g, 2, 7);
		
		System.out.print("Test de unsettled (ensemble W)...\t");
		Test.test_node_pri_queue(d, "unsettled", new Node(2, 0));		
		System.out.println("[OK]");
	}
}
