
// comparaisons du nombre d'etapes
public class Test4 {
	public static void main(String args[]) throws Exception {
		// Pour s'assurer que les assert's sont activés
		if (!Test4.class.desiredAssertionStatus()) {
			System.err.println("Vous devez activer l'option -ea de la JVM");
			System.err.println("(Run As -> Run configurations -> Arguments -> VM Arguments)");
			System.exit(1);
		}

		System.out.println("Test 4 : comparaisons du nombre d'étapes");

		Graph g = new Graph("data/USA-road-d-NY.gr");
	    int source = 190636, destination = 187333;

	    Dijkstra forward = new Dijkstra(g, source, destination);
	    Test.test_int_field(forward, "steps", 0);	 
	    int fLength = forward.compute();

	    Dijkstra backward = new Dijkstra(g.reverse(), destination, source);
	    int bLength = backward.compute();

	    BiDijkstra bothways = new BiDijkstra(g, source, destination);
	    int bothLength = bothways.compute();
	    		
		System.out.print("Test de la methode getMinPath()...\t");
		assert forward.getSteps() != 0 : "\n\nAuriez-vous oublié d'incrémenter 'steps' dans oneStep() ?\n";
		assert bothways.getSteps() != 1242 : "\n\nAuriez-vous incrémenté 'steps' dans BiDijkstra.oneStep() ?\n";
		assert forward.getSteps() == 1252 && backward.getSteps() == 1204 : "\n\nValeur de Dijkstra.getSteps() incorrecte\n"; 
		assert bothways.getSteps() == 621 : "\n\nValeur de BiDijkstra.getSteps() incorrecte\n";			
		System.out.println("[OK]");

		// resultats
		System.out.println();
		System.out.println("longueur chemin forward    entre " + source + " et " + destination + " = " + fLength + "\tnombre d'étapes = " + forward.getSteps());
		System.out.println("longueur chemin backward   entre " + source + " et " + destination + " = " + bLength + "\tnombre d'étapes = " + backward.getSteps());
		System.out.println("longueur chemin BiDijkstra entre " + source + " et " + destination + " = " + bothLength + "\tnombre d'étapes = " + bothways.getSteps());
	}
}
