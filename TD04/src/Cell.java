import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


/**
 * Cette classe modélise une case du labyrinthe
 */
public abstract class Cell {

	private boolean isMarked; // si la case est marquée
	private boolean isExit; // si la case est la sortie du labyrinthe
	/** liste des cases voisines de la cellule */
	private ArrayList<Cell> neighbors; // 
	public Cell next;
	private ArrayList<Boolean> walls; // pour chacune des cases voisines, s'il y a un mur dans sa direction
	protected Maze maze; // référence au labyrinthe (pour la visualisation)


	Cell() {}
	
	/**
	 * Initialise une case du labyrinthe
	 */
	Cell(Maze maze) { 
		isMarked = false;
		isExit = false;
		neighbors = new ArrayList<>();
		walls = new ArrayList<>();
		this.maze = maze;
	}

	/**
	 * Renvoie la liste des cases voisines (accessibles ou pas, selon la valeur de l'argument)
	 * 
	 * @return renvoie uniquement la liste des cases vers lesquelles un passage existe, si ignoreWalls==false. <br>
	 *         Si ignoreWalls==true, alors toutes les cases voisines sont renvoyées.
	 */
	List<Cell> getNeighbors(boolean ignoreWalls) {
		if(ignoreWalls)
			return new ArrayList<>(neighbors);
		else
			return neighbors.stream().filter(cell -> this.hasPassageTo(cell)).collect(Collectors.toList());
	}

	/**
	 * Teste si la case 'c' est une voisine et il n'y a pas de mur dans sa direction
	 * 
	 * @return  true ssi la case 'c' est une voisine et il n'y a pas de mur dans sa direction
	 */
	boolean hasPassageTo(Cell c) {
		if(c == null || this.neighbors.indexOf(c) == -1 || c.neighbors.indexOf(this) == -1)
			return false;

		return !this.walls.get(this.neighbors.indexOf(c));
	}

	/**
	 * Supprime le mur dans la direction de la case 'c' (si la case courante et la case 'c' sont adjacentes)
	 */
	void breakWall(Cell c) {
		if(c == null || this.neighbors.indexOf(c) == -1 || c.neighbors.indexOf(this) == -1)
			throw new IllegalArgumentException("cells are not neighbors");

		this.walls.set(this.neighbors.indexOf(c), false);
		c.walls.set(c.neighbors.indexOf(this), false);
	}

	/**
	 * Teste si la case est marquée
	 * 
	 * @return  true ssi ssi la case est marquée
	 */
	boolean isMarked() {
		return isMarked;
	}

	/**
	 * Ajoute une marque si b==true, enlève la marque si b==false
	 */
	void setMarked(boolean b) {
		isMarked = b;
	}

	/**
	 * Teste si la case est une sortie
	 * @return  true ssi la case est une sortie
	 */
	boolean isExit() {
		return isExit;
	}

	/**
	 * Déclare la case comme sortie si b==true, <br> déclare la case comme non-sortie si b==false
	 */
	void setExit(boolean b) {
		isExit = b;
	}

	/**
	 * Teste la case est isolée (i.e. a des murs dans toutes les directions)
	 * 
	 * @return  true ssi la case est isolée (i.e. a des murs dans toutes les directions)
	 */
	boolean isIsolated() {
		return getNeighbors(false).isEmpty();
	}

	/**
	 * Rajoute une case dans la liste des cases voisines
	 */
	void addNeighbor(Cell n) {
		neighbors.add(n);
		walls.add(true);
	}

	/**
	 * Teste s'il existe un chemin de la case actuelle vers une sortie
	 * 
	 * @return  true ssi il existe un chemin de la case actuelle vers une sortie
	 */
	abstract boolean searchPath();

	/**
	 * Génère un labyrinthe parfait par rebroussement récursif
	 */
	abstract void generateRec();
	
}
