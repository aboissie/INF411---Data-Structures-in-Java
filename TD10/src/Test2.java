import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Timer;
import java.util.TimerTask;

// Test de l'algorithme de Dijkstra basique 
public class Test2 {
	private static final int source = 2;
	private static final int dest = 7;

	public static void main(String args[]) throws Exception {
		// Pour s'assurer que les assert's sont activés
		if (!Test2.class.desiredAssertionStatus()) {
			System.err.println("Vous devez activer l'option -ea de la JVM");
			System.err.println("(Run As -> Run configurations -> Arguments -> VM Arguments)");
			System.exit(1);
		}

		String name = "mini.gr";
		System.out.println(
				"Test 2 : test de l'algorithme de Dijkstra unidirectionnel utilisant le graphe dans '" + name + "'...");
		System.out.println("*** ATTENTION : dans l'affichage, la numérotation des nœeuds commence à 0 ***\n"
				+ "***             (et non à 1 comme dans '" + name + "')                         ***");		
		Graph g = new Graph("data/" + name);
		
		testUpdate(g, name);
		testNextNode(g, name);
		testOneStep(g, name);
		testCompute(g, name);
	}

	static void testUpdate(Graph g, String name) throws Exception {
		System.out.print("Test de la methode update(int, int)...\t");
		// petit graphe
		Dijkstra d = new Dijkstra(g, source, dest);
		int[] distOld = d.getDist().clone();
		int[] predOld = d.getPred().clone();
		boolean[] settled = Test.get_boolean_array(d, "settled");
		boolean[] settledOld = settled.clone();

		int succ = 4;
		int current = source;
		int dist = 2;

		d.update(succ, current);

		boolean ok = true;
		for (int i = 0; i < d.getDist().length; i++) {
			if (i == succ)
				continue;

			ok = ok && d.getDist()[i] == distOld[i];
		}

		assert d.getDist()[source] == distOld[source] : "\n\n'dist' incorrect après une itération de update() : dist[source] = "
				+ d.getDist()[source] + " au lieu de 0\n"
				+ "Auriez-vous confondu 'succ' et 'current' lors de la mise à jour de 'dist' \n"
				+ "Parametres utilisés : (succ = " + succ + ", current = " + current + ")\n"
				+ "Graphe donné dans le fichier " + name + " :\n" + g;

		assert ok : "\n\n'dist' incorrect après une itération de update() : au moins une case autre que 'succ' modifiée\n";

		assert d.getDist()[succ] != distOld[succ] : "\n\nAuriez-vous oublié de mettre à jour dist[succ] ?\n";

		assert d.getDist()[succ] != Integer.MAX_VALUE : "\n\nDistance mal calculée après une itération de update() : "
				+ d.getDist()[succ] + " au lieu de " + g.weight(current, succ) + "\n"
				+ "Vérifier l'ordre des arguments dans l'appel à weight()\n" + "Parametres utilisés : (succ = " + succ
				+ ", current = " + current + ")\n" + "Graphe donné dans le fichier " + name + " :\n" + g;

		assert d.getDist()[succ] == dist : "\n\nDistance mal calculée après une itération de update() : " + d.getDist()[succ]
				+ " au lieu de " + g.weight(current, succ) + "\n" + "Parametres utilisés : (succ = " + succ
				+ ", current = " + current + ")\n" + "Graphe donné dans le fichier " + name + " :\n" + g;

		for (int i = 0; i < d.getPred().length; i++) {
			if (i == succ)
				continue;

			ok = ok && d.getPred()[i] == predOld[i];
		}

		assert ok : "\n\n'pred' incorrect après une itération de update() : au moins une case autre que 'succ' modifiée\n";

		assert d.getPred()[succ] == current : "\n\nPrédécesseur incorrect après une itération de update()\n";

		for (int i = 0; i < d.getDist().length; i++)
			ok = ok && settled[i] == settledOld[i];

		assert ok : "\n\n'settled' ne devrait pas changer lors d'un appel à update()\n";

		int succ1 = 3;
		int current1 = 4;
		int dist1 = 5;

		d.update(succ1, current1);

		assert d.getDist()[succ1] == dist1 : "\n\nDistance mal calculée après deux itérations de update : " + d.getDist()[succ1]
				+ " au lieu de " + dist1 + "\n" + "Auriez-vous oublié dist[current] ?\n"
				+ "Parametres utilisés : (succ = " + succ + ", current = " + current + "), " + "puis (succ = " + succ1
				+ ", current = " + current1 + ")\n" + "Graphe donné dans le fichier " + name + " :\n" + g;
		System.out.println("[OK]");

		PriorityQueue<Node> unsettled = Test.getPriQueue(d, "unsettled");

		assert unsettled
				.size() == 3 : "\n\nNombre incorrect de nœuds dans la file de priorités 'unsettled' après deux apples à update(): "
						+ unsettled.size() + " au lieu de 3\n";
		assert unsettled.poll().equals(new Node(current, 0)) && unsettled.poll().equals(new Node(succ, dist))
				&& unsettled.poll().equals(new Node(succ1,
						dist1)) : "\n\nNœuds incorrects dans la file de priorités 'unsettled' après deux apples à update()";
		
		d.getDist()[succ1] = 0;
		d.getPred()[succ1] = 0;
		distOld = d.getDist().clone();
		predOld = d.getPred().clone();
		
		d.update(succ1, current1);
		
		for (int i = 0; i < g.nbVertices; i++) 
			ok = ok && d.getPred()[i] == predOld[i] && d.getDist()[i] == distOld[i];
		
		assert ok : "\n\nLa mise à jour ne doit se faire que si la nouvelle distance est inférieure à celle déjà dans 'dist'\n";
	}

	private static String boolArrayToString(boolean[] array) {
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		for (int i = 0; i < array.length - 1; i++)
			sb.append(i + ":" + array[i] + ", ");
		sb.append((array.length - 1) + ":" + array[array.length - 1] + "]");
		return sb.toString();
	}

	private static void testPrQueue(Graph g, List<Node> nodes, boolean[] goodSettled, int[] result) {
		Dijkstra d = new Dijkstra(g, source, dest);
		PriorityQueue<Node> unsettled = null;
		try {
			unsettled = Test.getPriQueue(d, "unsettled");
		} catch (Exception e) {
			// Exception impossible car éliminée par des tests précédents
			e.printStackTrace(); 
		}
		assert unsettled.size() == 1 && unsettled.poll()
				.equals(new Node(source, 0)) : "\n\nFile de priorités 'unsettled' incorrecte à l'initialisation. "
						+ "Est-ce que votre constructeur Dijkstra(Graph, int, int) a bien passé le test de la Questions 1.2 ?\n";

		int next = -2;
		try {
			next = d.nextNode();
		} catch (NullPointerException e) {
			System.err.println("\n\nAuriez-vous oublié de vérifier que la file 'unsettled' n'est pas vide ?\n");
			throw e;			
		}
		
		assert next == -1 : "\n\nnextNode() doit renvoyer -1 pour une file 'unsettled' vide";
		
		for (Node node : nodes)
			unsettled.add(node);

		boolean[] settled = null;
		try {
			settled = Test.get_boolean_array(d, "settled");
		} catch (Exception e) {
			// Exception impossible car éliminée par des tests précédents
			e.printStackTrace(); 
		}

		for (int i = 0; i < Math.min(settled.length, goodSettled.length); i++)
			settled[i] = goodSettled[i];

		boolean[] settledOld = settled.clone();

		String unsetStr = unsettled.toString();
		int unsetSize = unsettled.size();

		for (int i = 0; i < result.length; i++) {
			assert d.nextNode() == result[i] : "\n\n" + (i + 1) + "ème appel à nextNode() doit renvoyer " + result[i]
					+ " pour la file unsettled = " + unsetStr + "\navec settled = " + boolArrayToString(settled) + "\n";
		}

		assert unsettled.size() == unsetSize - result.length : "\n\nLa file de priorité 'unsettled' contient "
				+ unsettled.size() + " nœud(s) au lieu de " + (unsetSize - result.length) + " après " + result.length
				+ " appel(s) à nextNode(). \n" + "Avez-vous bien utilisé unsettled.poll() et non unsettled.peek() ?\n";

		boolean ok = true;
		for (int i = 0; i < settledOld.length; i++)
			ok = ok && settled[i] == settledOld[i];

		assert ok : "\n\nnextNode() ne doit pas changer 'settled'\n";
	}

	static void testNextNode(Graph g, String name) {
		System.out.print("Test de la methode nextNode()...\t");

		ArrayList<Node> nodes = new ArrayList<>(Arrays.asList(new Node(2, 0)));
		int[] result = { 2 };
		boolean[] settled = {};
		testPrQueue(g, nodes, settled, result);

		nodes = new ArrayList<>(Arrays.asList(new Node(2, 0), new Node(4, 2), new Node(3, 5)));
		result = new int[] { 2, 4, 3 };
		testPrQueue(g, nodes, settled, result);

		nodes = new ArrayList<>(Arrays.asList(new Node(2, 0), new Node(4, 2)));
		settled = new boolean[] { false, false, true };
		result = new int[] { 4, -1 };
		testPrQueue(g, nodes, settled, result);

		System.out.println("[OK]");
	}

	static void testOneStep(Graph g, String name) {
		System.out.print("Test de la methode oneStep()...\t\t");

		Dijkstra d = new Dijkstra(g, source, dest);

		PriorityQueue<Node> unsettled = null;
		try {
			unsettled = Test.getPriQueue(d, "unsettled");
		} catch (Exception e) {
			// Exception impossible car éliminée par des tests précédents
			e.printStackTrace(); 
		}

		assert unsettled.size() == 1 && unsettled.poll()
				.equals(new Node(source, 0)) : "\n\nFile de priorités 'unsettled' incorrecte à l'initialisation.\n"
						+ "Est-ce que votre constructeur Dijkstra(Graph, int, int) a bien passé le test de la Questions 1.2 ?\n";

		assert d.oneStep() == -1 : "\n\noneStep() doit renvoyer -1 pour une file 'unsettled' vide.\n";

		d.settled[source] = true;
		unsettled.add(new Node(source, 0));
		
		assert d.oneStep() == -1 : "\n\noneStep() doit utiliser la méthode 'nextNode'\n";
		
		d.settled[source] = false;
		unsettled.add(new Node(source, 0));

		int[] distOld = d.getDist().clone();
		int[] predOld = d.getPred().clone();
		boolean[] settled = null;
		try {
			settled = Test.get_boolean_array(d, "settled");
		} catch (Exception e) {
			// Exception impossible car éliminée par des tests précédents
			e.printStackTrace(); 
		}
		boolean[] settledOld = settled.clone();

		boolean[] changed = new boolean[g.nbVertices];
		changed[4] = true;
		changed[5] = true;

		int result = d.oneStep();

		assert settled[source] : "\n\noneStep() doit mettre à jour le statut 'settled' du nœud trait\n";

		boolean ok = true;
		for (int i = 0; i < g.nbVertices; i++)
			ok = ok && (d.getDist()[i] != distOld[i]) == changed[i] && (d.getPred()[i] != predOld[i]) == changed[i]
					&& (settled[i] != settledOld[i]) == (i == source);

		assert ok : "\n\noneStep() ne fait pas correctement appel à update()\n"
				+ (d.getDist()[source] != distOld[source] || d.getPred()[source] != predOld[source]
						? "Vérifiez l'ordre des arguments dans l'appel à update()\n"
						: "");

		assert result == source : "\n\noneStep() doit renvoyer le numéro du nœud traité\n";
		
		System.out.println("[OK]");
	}

	static void testCompute(Graph g, String name) throws Exception {
		System.out.print("Test de la methode compute()...\t\t");
		int localSource = 2;
		int localDest = 3;
		int localDist = 5;
		int localSettled = 3;
		int maxSettled = 7;

		Dijkstra d = new Dijkstra(g, localSource, localDest);

		boolean[] settled = Test.get_boolean_array(d, "settled");

		boolean ok = true;
		for (int i = 0; i < g.nbVertices; i++)
			ok = ok && !settled[i];

		assert ok : "\n\nLe tableau 'settled' incorrect à l'initialisation.\n"
				+ "Est-ce que votre constructeur Dijkstra(Graph, int, int) a bien passé le test de la Questions 1.2 ?\n";

		PriorityQueue<Node> unsettled = null;
		try {
			unsettled = Test.getPriQueue(d, "unsettled");
		} catch (Exception e) {
			// Exception impossible car éliminée par des tests précédents
			e.printStackTrace(); 
		}

		assert unsettled.size() == 1 && unsettled.poll()
				.equals(new Node(source, 0)) : "\n\nFile de priorités 'unsettled' incorrecte à l'initialisation.\n"
						+ "Est-ce que votre constructeur Dijkstra(Graph, int, int) a bien passé le test de la Questions 1.2 ?\n";

		int result;
		Timer t = new Timer();
		t.schedule(new TimerTask() {
			public void run() {
				System.out.println("\n\n compute() semble être rentré dans une boucle infinie. "
						+ "Vérifier les conditions d'arrêt de boucle dans compute()\n");
				System.exit(1);
			}
		}, 2000L);
		result = d.compute();
		t.cancel();
	
		assert result == -1 : "\n\ncompute() doit renvoyer -1 lorsqu'un chemin de source à dest ne peut pas être trouvé.\n";

		unsettled.add(new Node(localSource, 0));

		result = d.compute();
		int countSettled = 0;

		for (int i = 0; i < g.nbVertices; i++)
			if (settled[i])
				countSettled++;

		assert countSettled != maxSettled : "\n\noneStep() est appelé " + countSettled + " fois au lieu de "
				+ localSettled + ".\n" + "Vérifier la condition d'arrêt de la boucle dans compute()\n";

		assert (result == localDist) : "\n\nLe plus court chemin entre " + localSource + " et " + localDest
				+ " devrait avoir la longueur " + localDist + " (et non pas " + result + ") dans le graph\n" + g;

		localSource = 7;
		localDest = 2;
		d = new Dijkstra(g, localSource, localDest);

		try {
			unsettled = Test.getPriQueue(d, "unsettled");
		} catch (Exception e) {
			// Exception impossible car éliminée par des tests précédents
			e.printStackTrace(); 
		}

		assert unsettled.size() == 1 && unsettled.peek().equals(new Node(dest, 0)) : 
				"\n\nFile de priorités 'unsettled' incorrecte à l'initialisation.\n"
				+ "Est-ce que votre constructeur Dijkstra(Graph, int, int) a bien passé le test de la Questions 1.2 ?\n";

		result = d.compute();

		assert (result == -1) : "\n\nIl n'y a pas de chemin entre " + localSource + " et " + localDest
				+ ", mais vous en trouvez un de longueur " + result + "\n";

		localSource = 9;
		localDest = 11;
		localDist = 2;
		d = new Dijkstra(g, localSource, localDest);

		result = d.compute();

		assert (result == localDist) : "\n\nLe plus court chemin entre " + localSource + " et " + localDest
		+ " devrait avoir la longueur " + localDist + " (et non pas " + result + ") dans le graph\n" + g;

		System.out.println("[OK]");
	}
}
