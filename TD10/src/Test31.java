import java.util.HashSet;

// Test de l'algorithme de Dijkstra bidirectionnel naif
public class Test31 {
	private final static int smSource = 2;
	private final static int smDestination = 7;
	private final static int smLength = 7;

//	private final static int source = 190637; // pour qu'il soit claire que sourceBis n'est pas la même que source 
	private final static int sourceBis = 190636;
	private final static int dest = 187333;
	private final static int naive = 35489;

	public static void main(String args[]) throws Exception {
		// Pour s'assurer que les assert's sont activés
		if (!Test31.class.desiredAssertionStatus()) {
			System.err.println("Vous devez activer l'option -ea de la JVM");
			System.err.println("(Run As -> Run configurations -> Arguments -> VM Arguments)");
			System.exit(1);
		}

		System.out.println("Test 3.1 : test de l'algorithme de Dijkstra bidirectionnel naif");

		System.out.println();
		String name = "mini";
		Graph g = new Graph("data/" + name + ".gr");

		testBiDijkstra(g, name, smSource, smDestination);
		testFlip(g, name, smSource, smDestination);
		testOneStep(g, name, smSource, smDestination);
		testIsOver(g, name, smSource, smDestination);
		testCompute(g, name, smSource, smDestination, smLength);
		// Le test de getMinPath() répose sur l'hypothèse que compute() est correct
		testGetMinPath(g, name, smSource, smDestination, -1);

		// gros graphe
		System.out.println();
		name = "USA-road-d-NY";
		g = new Graph("data/" + name + ".gr");
		g.setCoordinates("data/" + name + ".co");
		
		testGetMinPath(g, name, sourceBis, dest, naive);
	}

	static void testBiDijkstra(Graph g, String name, int source, int dest) throws Exception {
		System.out.print("Test du constructeur BiDijkstra(Graph, int, int)...\t");

		Graph gr = g.reverse();
		Dijkstra f = new Dijkstra(g, source, dest);
		Dijkstra b = new Dijkstra(gr, dest, source);
		BiDijkstra bothways = new BiDijkstra(g, source, dest);

		assert bothways.forward != null
				&& bothways.forward.equals(f) : "\n\n'forward' n'est pas initialisé correctement\n";
		assert bothways.backward != null
				&& bothways.backward.equals(b) : "\n\n'backward' n'est pas initialisé correctement. "
						+ "Auriez-vous oublié d'inverser le graph ?\n";

		HashSet<Dijkstra> dijk = new HashSet<>();
		dijk.add(bothways.currentDijkstra);
		dijk.add(bothways.otherDijkstra);
		assert dijk.contains(bothways.forward) && dijk.contains(bothways.backward) : 
			"\n\nInitialisation incorrecte pour 'currentDijkstra' ou 'otherDijkstra'\n";

		assert bothways.getLast() == bothways.currentDijkstra.source : "\n\nInitialisation incorrecte pour 'last'\n";

		System.out.println("[OK]");
	}

	static void testFlip(Graph g, String name, int source, int dest) throws Exception {
		System.out.print("Test de la methode flip()...\t\t\t\t");
		BiDijkstra bothways = new BiDijkstra(g, source, dest);

		Dijkstra currentOld = bothways.currentDijkstra;
		Dijkstra otherOld = bothways.otherDijkstra;

		bothways.flip();

		assert bothways.currentDijkstra != bothways.otherDijkstra : 
			"\n\nAuriez-vous oublié le une case \"temp\" pour transposer 'currentDijkstra' et 'otherDijkstra' ?\n";
		assert bothways.currentDijkstra == otherOld
				&& bothways.otherDijkstra == currentOld : "\n\nflip() doit transposer 'currentDijkstra' et 'otherDijkstra'\n";

		System.out.println("[OK]");
	}

	static void testOneStep(Graph g, String name, int source, int dest) throws Exception {
		System.out.print("Test de la methode oneStep()...\t\t\t\t");

		BiDijkstra bothways = new BiDijkstra(g, source, dest);
		bothways.oneStep();
		bothways.oneStep();
		assert (bothways.getLast() != source) : "\n\n'last' n'a pas changé après deux itérations : " + "\nsource = "
				+ source + "\nlast = " + bothways.getLast() + "\nlast devrait être différent de source"
				+ "\nn'oubliez pas de le mettre à jour dans oneStep()\n";

		System.out.println("[OK]");
	}

	static void testIsOver(Graph g, String name, int source, int dest) throws Exception {
		System.out.print("Test de la methode isOver()...\t\t\t\t");

		BiDijkstra bothways = new BiDijkstra(g, source, dest);

		assert !bothways.isOver() : 
			"\n\nisOver() doit renvoyer 'true' uniquement si 'last' est \"settled\" pour les deux parcours, 'forward' et 'backward'\n";
		bothways.oneStep();
		assert !bothways.isOver() : 
			"\n\nisOver() doit renvoyer 'true' uniquement si 'last' est \"settled\" pour les deux parcours, 'forward' et 'backward'\n";
		
		boolean[] settled = null;
		try {
			settled = Test.get_boolean_array(bothways.backward, "settled");
		} catch (Exception e) {
			// Exception impossible car éliminée par des tests précédents
			e.printStackTrace(); 
		}

		settled[source] = true;
		assert bothways.isOver() : 
			"\n\nLorsque 'last' est \"settled\" pour les deux parcours, 'forward' et 'backward', isOver() doit renvoyer 'true'\n";

		System.out.println("[OK]");
	}

	private static int countSettled(Dijkstra d) {
		int count = 0;

		boolean[] settled = null;
		try {
			settled = Test.get_boolean_array(d, "settled");
		} catch (Exception e) {
			// Exception impossible car éliminée par des tests précédents
			e.printStackTrace(); 
		}

		for (int i = 0; i < settled.length; i++) {
			if (settled[i])
				count++;
		}
		return count;
	}

	static void testCompute(Graph g, String name, int source, int dest, int length) throws Exception {
		System.out.print("Test de la methode compute()...\t\t\t\t");

		BiDijkstra bothways = new BiDijkstra(g, source, dest);
		int result = -1;
		try {
			result = bothways.compute();
		} catch (ArrayIndexOutOfBoundsException e) {
			StackTraceElement[] stack = e.getStackTrace();
			if (stack[0].getMethodName().equals("isOver")) 
				System.err.println("\n\nAuriez-vous oublié un appel à flip() dans compute() ?\n");
			else if (stack[0].getMethodName().equals("getMinPath"))
				System.err.println("\n\nAuriez-vous oublié  dans compute() ?\n");
			
			throw e;			
		}
		
		assert bothways.isOver() : 
			"\n\ncompute() ne vas pas jusqu'au bout -- vérifier la condition d'arrêt de boucle\n";

		int fSettled = countSettled(bothways.forward);
		int bSettled = countSettled(bothways.backward);
		assert (fSettled - bSettled == 0) || (fSettled - bSettled == 1) : 
			"\n\ncompute() ne fait pas correctement alterner les deux parcours, 'forward' et 'backward'\n";

		assert fSettled + bSettled <= length : "\n\nTrop d'appels à oneStep() dans compute()\n";
		assert fSettled + bSettled >= length : "\n\nPas assez d'appels à oneStep() dans compute()\n";

		assert result == bothways.getMinPath() : 
			"\n\ncompute() renvoie autre chose que la longuer du chemin calculée par getMinPath()\n";

		System.out.println("[OK]");
	}

	static void testGetMinPath(Graph g, String name, int source, int dest, int naive) throws Exception {
		System.out.print("Test de la methode getMinPath()...\t\t\t");

		BiDijkstra bothways = new BiDijkstra(g, source, dest);

		int minPath = bothways.getMinPath();
		assert minPath == Integer.MAX_VALUE : "\n\ngetMinPath() renvoie " + minPath
				+ "dans une situation où le chemin de source à dest n'a pas encore été trouvé\n";

		minPath = bothways.compute();
		assert minPath == bothways.forward.getDist()[bothways.getLast()] + bothways.backward.getDist()[bothways.getLast()] :
			"\n\nValeur renvoyée par getMinPath() n'est pas la somme de distances à 'last' de 'source' et de 'dest'\n";
		
		System.out.println("[OK]");
		
		if (minPath < naive)
			System.err.println("\nIl semblerait que vous essayez de tester la version modifiée de la méthode getMinPath()"
					+ " avec Test31 au lieu de Test32\n");
	}

}
