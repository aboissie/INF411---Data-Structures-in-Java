import java.awt.Color;
import java.io.*;
import java.util.*;

// Implementation d'un graphe
public class Graph {
	final static int cHash = 8; // constante utilisee dans la fonction de hachage
	final int nbVertices; // nombre de sommets
	final private int nbEdges; // nombre d'arcs
	final private ArrayList<ArrayList<Integer>> succ; // successeurs de chaque sommet
	final private ArrayList<ArrayList<Integer>> pred; // predecesseurs de chaque sommet
	final private HashMap<Edge, Integer> weights; // poids des aretes
	private double[][] coordinates = null; // coordonnees des sommets pour affichage

	// constructeurs
	
	// constructeur de base avec tous les champs passés en arguments
	public Graph(int nbVertices, int nbEdges,
			ArrayList<ArrayList<Integer>> succ,
			ArrayList<ArrayList<Integer>> pred,
			HashMap<Edge, Integer> weights) 
	{
		this.nbVertices = nbVertices;
		this.nbEdges = nbEdges;
		this.succ = succ;
		this.pred = pred;
		this.weights = weights;
	}

	// constructeur d'un graphe vide fixant juste les nombres de nœuds et d'arrêtes
	public Graph(int nbVertices, int nbEdges) {
		// on appelle d'abord le constructeur de base 
		this(nbVertices, nbEdges, 
				new ArrayList<>(nbVertices),
				new ArrayList<>(nbVertices), 
				new HashMap<>(cHash * nbVertices));

		// puis on initialise succ et pred avec des vecteurs vides pour chaque nœud 
		for (int i = 0; i < nbVertices; i++) {
			succ.add(new ArrayList<>());
			pred.add(new ArrayList<>());
		}

	}

	// constructeur générant un graphe aléatoire
	// p -- la probabilité d'avoir une arrête entre deux nœuds
	// max -- le poids maximal d'une arrête (distribution uniforme)
	public Graph(int nbVertices, double p, int max) {
		// on ne peut pas utiliser les deux constructeurs ci-dessus
		// car on ne conaît pas encore nbEdges, qui est final
		// on initialise donc « à la main »
		this.nbVertices = nbVertices;
		succ = new ArrayList<>(nbVertices);
		pred = new ArrayList<>(nbVertices);
		weights = new HashMap<>(cHash * nbVertices);

		for (int i = 0; i < nbVertices; i++) {
			succ.add(new ArrayList<>());
			pred.add(new ArrayList<>());
		}

		int tmpNbEdges = 0;
		// pour chaque paire de nœuds
		for (int i = 0; i < nbVertices; i++) {
			for (int j = 0; j < nbVertices; j++) {
				// on décide aléatoirement de créer ou pas une arrête
				if (Math.random() < p) {
					tmpNbEdges++;
					// on choisit son poids au hasard
					int val = (int) (max * Math.random());
					// on l'ajoute dans le graphe 
					succ.get(i).add(j);
					pred.get(j).add(i);
					// avec le poids
					addWeightedArc(i, j, val);
				}
			}
		}
		nbEdges = tmpNbEdges;
	}

	// importer un graphe à partir d'un fichier
	public Graph(String file) throws Exception {
		System.out.print("Loading road networks from file " + file + "...\t");
		// on ouvre le fichier en lecture
		BufferedReader br = new BufferedReader(new FileReader(file));

		// on omet toutes les lignes qui NE commencent PAS par 'p'
		String dataLine = br.readLine();
		while (dataLine.charAt(0) != 'p')
			dataLine = br.readLine();

		// la première ligne qui commence par 'p' contient contient
		// le nombres de nœuds et d'arrêtes (en plus de "sp" qu'on ignore)
		String[] tokens = dataLine.split("\\s");
		nbVertices = Integer.parseInt(tokens[2]);
		nbEdges = Integer.parseInt(tokens[3]);

		// on initialise avec de collections vides mais à la bonne capacité
		succ = new ArrayList<>(nbVertices);
		pred = new ArrayList<>(nbVertices);
		weights = new HashMap<>(cHash * nbVertices);

		// pour chaque nœud on initialise avec les vecteurs vides
		for (int i = 0; i < nbVertices; i++) {
			succ.add(new ArrayList<>());
			pred.add(new ArrayList<>());
		}

		// on continue à lire dans le fichier, ligne par ligne
		while ((dataLine = br.readLine()) != null) {
			tokens = dataLine.split("\\s");
			// on jete toutes les lignes qui ne commencent pas par 'a'
			if (tokens[0].equals("a")) {
				// source de l'arrête
				int i = Integer.parseInt(tokens[1]);
				// destination de l'arrête
				int j = Integer.parseInt(tokens[2]);
				// poids de l'arrête
				int v = Integer.parseInt(tokens[3]);
				// dans le fichier, la numérotation commence par 1
				// dans les vecteurs -- par 0
				succ.get(i - 1).add(j - 1);
				pred.get(j - 1).add(i - 1);
				addWeightedArc(i - 1, j - 1, v);
			}
		}
		// on clôt le fichier
		br.close();
		System.out.println("done");
	}

	// mise en place des coordonnees des sommets
	public void setCoordinates(String file) throws Exception {
		System.out.print("Loading geometric coordinates from file " + file + " ... ");
		BufferedReader br = new BufferedReader(new FileReader(file));

		String dataLine = br.readLine();
		while (dataLine.charAt(0) != 'p')
			dataLine = br.readLine();

		String[] tokens = dataLine.split("\\s");
		int nPoints = Integer.parseInt(tokens[4]);
		if (nPoints != this.nbVertices) {
			br.close();
			throw new Error("The number of points does not match the number of nodes in the graph");
		}

		this.coordinates = new double[this.nbVertices][2];

		while ((dataLine = br.readLine()) != null) {
			tokens = dataLine.split("\\s");
			if (tokens[0].equals("v")) {
				int node = Integer.parseInt(tokens[1]);
				double x = Double.parseDouble(tokens[2]);
				double y = Double.parseDouble(tokens[3]);
				this.coordinates[node - 1][0] = x / 1000000.;
				this.coordinates[node - 1][1] = y / 1000000.;
			}
		}
		br.close();
		System.out.println("done");
	}

	public ArrayList<Integer> successors(int i) {
		return succ.get(i);
	}

	public ArrayList<Integer> predecessors(int i) {
		return pred.get(i);
	}

	// ajout d'un nouvel arc pondere
	public void addWeightedArc(int i, int j, int v) {
		this.weights.put(new Edge(i, j), v);
	}

	// poids de l'arc (i,j) s'il existe, très grand sinon
	public int weight(int i, int j) {
		if (!this.weights.containsKey(new Edge(i, j)))
			return Integer.MAX_VALUE;
		return this.weights.get(new Edge(i, j));
	}

	// renvoie le graphe ou toutes les orientations ont ete inversees
	public Graph reverse() {
		HashMap<Edge, Integer> map = new HashMap<>(cHash * nbVertices);
		for (int i = 0; i < nbVertices; i++) {
			for (Integer j : this.succ.get(i)) {
				int val = this.weight(i, j);
				map.put(new Edge(j, i), val);
			}
		}
		Graph rg = new Graph(nbVertices, nbEdges, pred, succ, map);
		rg.coordinates = coordinates;
		return rg;
	}

	// affichage d'un graphe en chaine
	public String toString() {
		String s = "nbVertices=" + nbVertices + '\n' + "nbEdges=" + nbEdges + '\n';
		for (int i = 0; i < nbVertices; i++) {
			s = s + "Node " + i + ":" + '\n';
			for (int j : succ.get(i))
				s = s + "   " + i + " --> " + j + " (" + this.weight(i, j) + ")" + '\n';
		}
		return s;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null || !(obj instanceof Graph))
			return false;
		
		Graph that = (Graph) obj;

		// on vérifie que les nombres de sommets et d'arrêts coincident
		if (nbVertices != that.nbVertices || nbEdges != that.nbEdges)
			return false;
		
		// on vérifie que tous les vecteurs sont null (ou pas) simultanément...
		if (((succ == null) != (that.succ == null)) 
				|| ((pred == null) != (that.pred == null))
				|| ((weights == null) != (that.weights == null))
				)
			return false;
		
		// ...et que leur tailles coincident 
		if (succ.size() != that.succ.size() || pred.size() != that.pred.size())
			return false;
		
		// pour chaque sommet, on vérifie que les successeurs et les prédécesseurs conincident
		for (int i = 0; i < nbVertices; i++) {
			ArrayList<Integer> succi = succ.get(i);
			ArrayList<Integer> tsucci = that.succ.get(i);
			ArrayList<Integer> predi = pred.get(i);
			ArrayList<Integer> tpredi = that.pred.get(i);
			if (succi.size() != tsucci.size() || predi.size() != tpredi.size())
				return false;
			
			for (int j = 0; j < succi.size(); j++)
				if (succi.get(j) != tsucci.get(j))
					return false;
			
			for (int j = 0; j < predi.size(); j++)
				if (predi.get(j) != tpredi.get(j))
					return false;
		}

		// on vérifie que les ensembles d'arrêtes ont la même taille 
		if (weights.keySet().size() != that.weights.keySet().size())
			return false; 
		
		// pour chaque arrête de this on vérifie qu'elle est aussi présente dans that
		// et que les poids coincident
		for (Edge edge : weights.keySet()) 
			if (!weights.get(edge).equals(that.weights.get(edge)))
				return false;
			
		// do not compare coordinates since they do not affect the structure of the graph 
		// but only its representation
		return true;
	}
	
	// fonctions d'affichage

	// dessin du graphe
	public void drawGraph(Fenetre f) {
		if (f == null || this.coordinates == null) // verification donnees geometriques
			return;

		for (int i = 0; i < this.nbVertices; i++) {
			double x1 = this.coordinates[i][0];
			double y1 = this.coordinates[i][1];
			for (Integer j : this.pred.get(i)) {
				double x2 = this.coordinates[j][0];
				double y2 = this.coordinates[j][1];
				f.addSegment(x1, y1, x2, y2, 1, Color.BLACK);
			}
			f.addPoint(x1, y1, 1, Color.BLACK);
		}
	}

	// dessiner un point traite (rouge)
	public void drawSettledPoint(Fenetre f, int p) {
		if (f != null && this.coordinates != null) { // verification donnees geometriques
			f.addPoint(this.coordinates[p][0], this.coordinates[p][1], 3, Color.RED);
		}
	}

	// dessiner un point visité et a traiter (vert)
	public void drawUnsettledPoint(Fenetre f, int p) {
		if (f != null && this.coordinates != null) { // verification donnees geometriques
			f.addPoint(this.coordinates[p][0], this.coordinates[p][1], 3, Color.GREEN);
		}
	}

	// dessiner la source et la destination
	public void drawSourceDestination(Fenetre f, int origin, int destination) {
		if (f != null && this.coordinates != null) { // verification donnees geometriques
			f.addPoint(this.coordinates[origin][0], this.coordinates[origin][1], 6, Color.BLUE);
			f.addPoint(this.coordinates[destination][0], this.coordinates[destination][1], 6, Color.BLUE);
		}
	}

	// dessiner un point special
	public void drawSpecialPoint(Fenetre f, int p) {
		if (f != null && this.coordinates != null) { // verification donnees geometriques
			f.addPoint(this.coordinates[p][0], this.coordinates[p][1], 6, Color.BLUE);
		}
	}

	// dessiner le chemin en utilisant l'information contenue dans pred
	public void drawPath(Fenetre f, int[] pred, int i) {
		if (f == null || pred == null || this.coordinates == null) // verification donnees geometriques
			return;

		double x1 = this.coordinates[i][0];
		double y1 = this.coordinates[i][1];
		while (pred[i] != -1 && pred[i] != i) {
			double x2 = this.coordinates[pred[i]][0];
			double y2 = this.coordinates[pred[i]][1];
			f.addSegment(x1, y1, x2, y2, 10, Color.BLUE);
			x1 = x2;
			y1 = y2;
			i = pred[i];
		}
	}

	// dessiner le chemin forme de deux morceaux se rejoignant
	public void drawPath(Fenetre f, int[] predF, int[] predB, int x) {
		if (f != null && this.coordinates != null) { // verification donnees geometriques
			drawPath(f, predF, x);
			drawPath(f, predB, x);
			drawSpecialPoint(f, x);
		}
	}
}
