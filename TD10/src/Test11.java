// Test de l'initialisation de la classe Dijkstra
public class Test11 {
	public static void main(String args[]) throws Exception {
		// Pour s'assurer que les assert's sont activés
		if (!Test11.class.desiredAssertionStatus()) {
			System.err.println("Vous devez activer l'option -ea de la JVM");
			System.err
					.println("(Run As -> Run configurations -> Arguments -> VM Arguments)");
			System.exit(1);
		}
		System.out.println("Test 1.1 : Initialisation de la classe Dijkstra");
		Graph g = new Graph("data/mini.gr");
		Dijkstra d = new Dijkstra(g, 2, 7);
		System.out.print("Test de source...\t\t\t");
		assert (d.source == 2) : "\n\nVous n'avez pas identifié correctement la source de Dijkstra";
		System.out.println("[OK]");
		
		System.out.print("Test de dest...\t\t\t\t");		
		assert (d.dest == 7) : "\n\nVous n'avez pas identifié correctement la destination de Dijkstra";
		System.out.println("[OK]");

		System.out.print("Test de dist, pred et settled...\t");			
		Test.test_int_array_field(g, d, "dist", Integer.MAX_VALUE, 0);
		Test.test_int_array_field(g, d, "pred", -1, 2);
		Test.test_bool_array_field(g, d, "settled", false, false);
		System.out.println("[OK]");
	}

}
