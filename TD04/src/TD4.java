/**
 *  TD4. Rebroussement, solution et génération de labyrinthes <br>
 * Ce fichier contient 2 classes: <br>
 * 	- ExtendCell fournit à une case du labyrinthe des opérations pour calcuelr un chemin vers la sortier et générer un labyrinthe de manière récursive <br>
 * 	- Maze modélise un labyrinthe.
 */

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.io.IOException;

import java.nio.charset.StandardCharsets;

import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Cette classe permet d'étendre et enrichir la représentation d'une case du labyrinthe. <br>
 * Elle fournit à une case les opérations permettant de: <br>
 * -) trouver un chemin vers la sortie <br>
 * -) Génerer un labyrinthe de manière récursive
 * 
 */

class Coord{
	public int i, j;

	Coord(int i, int j){
		this.i = i;
		this.j = j;
	}
}

class ExtendedCell extends Cell {
	
	public ExtendedCell(Maze maze) {
		super(maze);
	}
	
	// Question 1

	/**
	 * Teste s'il existe un chemin de la case actuelle vers une sortie
	 * 
	 * @return  true ssi il existe un chemin de la case actuelle vers une sortie
	 */
	boolean searchPath() {
		maze.slow(); // ralentit l'animation de la recherche (pour aider à debugger)

		//throw new Error("Methode searchPath() a completer (Question 1)");
		setMarked(true);
		if(isExit()) return true;
		
		for(Cell c:getNeighbors(false)){
			if(!c.isMarked() && c.searchPath()) return true;
		}

		setMarked(false);
		return false;
	}

	// Question 2

	/**
	 * Génère un labyrinthe parfait par rebroussement récursif
	 */
	void generateRec() {
		maze.slow();
		if(isExit()) return;

		List<Cell> neighbors = getNeighbors(true);
		Collections.shuffle(neighbors);

		for(Cell c:neighbors){
			if(c.isIsolated()){
				breakWall(c);
				c.generateRec();
			}	
		}

		return;
	}

}

/**
 * Cette classe modélise un labyrinthe
 */
class Maze {

	private int height, width;
	/** La grille (tableau de cellules) représentant le labyrinthe */
	private Cell[][] grid;


	// Question 3

	/**
	 * Génère un labyrinthe parfait par rebroussement itératif
	 */
	void generateIter(int selectionMethod) {
		Bag cells = new Bag(selectionMethod);
		cells.add(getFirstCell());

		while(!cells.isEmpty()) {
			slow();
			Cell c = cells.peek();
			List<Cell> neighbors = c.getNeighbors(true);
			Collections.shuffle(neighbors);
			boolean pop = true;
			for(Cell n:neighbors){
				if(n.isIsolated()){
					c.breakWall(n);
					cells.add(n);
					pop = false;
					break;
				}
			}

			if(pop) cells.pop();
		}

	}


	// Question 4
	
	/**
	 * Génère un labyrinthe avec l'algorithme de Wilson
	 */


	void generateWilson() {
		// 1. Récupère toutes les cellules
		ArrayList<Cell> cells = new ArrayList<Cell>();
		Cell current;
		for (Cell[] row : grid)
			for (Cell c : row)
				cells.add(c);
		Collections.shuffle(cells);
		// 2. Initialiser un labyrinthe partiel en marquant une case
		cells.get(0).setMarked(true);
		// 3. Tant qu'il reste une case `cell` non marquée...
		for (Cell cell : cells) {
			if (!cell.isMarked()) {
				// Marche aléatoire partant de `cell`, jusqu'à tomber sur une case marquée 
				current = cell;
				while (!current.isMarked()) {
					List<Cell> neighbors = current.getNeighbors(true);
					Collections.shuffle(neighbors);
					current.next = neighbors.get(0); 
					current = current.next; // Si une cellule est revisitée, son  
          //champs next est mis à jour, c'est ce qui supprime implicitement
          //les boucles dans la seconde phase. 
				}
				//On parcourt le chemin en partant de `cell`: à chque étape on  casse le mur qui sépare une case de son successeur (next). 
        //On note que le chemin ne peut pas contenir de boucles qui ne contient pas de cases non marquées, sinon on ne serait pas sorti de la boucle précédente.    
				current = cell;
				while (!current.isMarked()) {
					slow();
					current.breakWall(current.next);
					current.setMarked(true); // Les cellules le long du chemin sont 
          // «ajoutées au labyrinthe»
					current = current.next;
				}
			}
		}

	}

	/**
	 * Renvoie la cellule avec les coordonnées (i, j)
	 * 
	 * @return  la cellule avec les coordonnées (i, j)
	 */
	Cell getCell(int i, int j) {
		if(i < 0 || i >= height || j < 0 || j >= width)
			throw new IllegalArgumentException("invalid indices");

		return grid[i][j];
	}

	/**
	 * Renvoie la cellule avec les coordonnées (0, 0)
	 * 
	 * @return  la cellule avec les coordonnées (0, 0)
	 */
	Cell getFirstCell() {
		return getCell(0, 0);
	}

	// traduit coordonnées en numéro de cellule
	int coordToInt(int i, int j) {
		if(i < 0 || i >= height || j < 0 || j >= width)
			throw new IndexOutOfBoundsException();

		return i*width + j;
	}

	// traduit un numéro de cellule en Coordinate
	Coordinate intToCoord(int x) {
		if(x < 0 || x >= height*width)
			throw new IndexOutOfBoundsException();

		return new Coordinate(x/width, x%width);
	}


	// ralentit l'affichage du labyrinthe si une fenêtre graphique est ouverte
	void slow(){
		if (frame == null) return;

		try {
			Thread.sleep(10);
			frame.repaint();
		} catch (InterruptedException e) {}
	}

	private MazeFrame frame;
	private static final int step = 20;

	Maze(int height, int width) {
		this(height, width, true);
	}

	Maze(int height, int width, boolean window) {
		if((height <= 0) || (width <= 0))
			throw new IllegalArgumentException("height and width of a Maze must be positive");

		this.height = height;
		this.width = width;

		grid = new Cell[height][width];

		for(int i = 0; i < height; ++i)
			for(int j = 0; j < width; ++j)
				grid[i][j] = new ExtendedCell(this);

		for(int i = 0; i < height; ++i) {
			for(int j = 0; j < width; ++j) {
				if(i < height - 1) {
					grid[i][j].addNeighbor(grid[i+1][j]);
					grid[i+1][j].addNeighbor(grid[i][j]);
				}

				if(j < width - 1) {
					grid[i][j].addNeighbor(grid[i][j+1]);
					grid[i][j+1].addNeighbor(grid[i][j]);
				}
			}
		}

		grid[height-1][width-1].setExit(true);

		if(window)
			frame = new MazeFrame(grid, height, width, step);
	}

	Maze(String path) throws IOException {
		this(Files.readAllLines(Paths.get(path), StandardCharsets.UTF_8));
	}

	Maze(String path, boolean window) throws IOException {
		this(Files.readAllLines(Paths.get(path), StandardCharsets.UTF_8), window);
	}

	Maze(List<String> lines) {
		this(lines, true);
	}

	Maze(List<String> lines, boolean window) {
		if(lines.size() < 2)
			throw new IllegalArgumentException("too few lines");

		this.height = Integer.parseInt(lines.get(0));
		this.width = Integer.parseInt(lines.get(1));

		this.grid = new Cell[height][width];
		for(int i = 0; i < height; ++i)
			for(int j = 0; j < width; ++j)
				grid[i][j] = new ExtendedCell(this);

		for(int i = 0; i < height; ++i) {
			for(int j = 0; j < width; ++j) {
				if(i < height - 1) {
					grid[i][j].addNeighbor(grid[i+1][j]);
					grid[i+1][j].addNeighbor(grid[i][j]);
				}

				if(j < width - 1) {
					grid[i][j].addNeighbor(grid[i][j+1]);
					grid[i][j+1].addNeighbor(grid[i][j]);
				}
			}
		}

		grid[height-1][width-1].setExit(true);

		int i = 0;
		int j = 0;

		for(String line : lines.subList(2, lines.size())) {

			for(int k = 0; k < line.length(); ++k) {
				switch(line.charAt(k)) {
					case 'N':
						grid[i][j].breakWall(grid[i-1][j]);
						break;
					case 'E':
						grid[i][j].breakWall(grid[i][j+1]);
						break;
					case 'S':
						grid[i][j].breakWall(grid[i+1][j]);
						break;
					case 'W':
						grid[i][j].breakWall(grid[i][j-1]);
						break;
					case '*':
						grid[i][j].setMarked(true);
						break;
					default:
						throw new IllegalArgumentException("illegal character");
				}
			}
			++j;
			if(j >= width) {
				j = 0;
				++i;
			}
		}

		if(window)
			frame = new MazeFrame(grid, height, width, step);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		sb.append(height);
		sb.append('\n');
		sb.append(width);
		sb.append('\n');

		for(int i = 0; i < height; ++i) {
			for(int j = 0; j < width; ++j) {
				if(i > 0 && grid[i][j].hasPassageTo(grid[i-1][j]))
					sb.append('N');
				if(j < width-1 && grid[i][j].hasPassageTo(grid[i][j+1]))
					sb.append('E');
				if(i < height-1 && grid[i][j].hasPassageTo(grid[i+1][j]))
					sb.append('S');
				if(j > 0 && grid[i][j].hasPassageTo(grid[i][j-1]))
					sb.append('W');
				if(grid[i][j].isMarked())
					sb.append('*');
				sb.append('\n');
			}
		}

		return sb.toString();
	}

	@Override
	public boolean equals(Object o) {
		if(!(o instanceof Maze))
			return false;
		Maze that = (Maze)o;

		return this.toString().equals(that.toString());
	}

	@Override
	public int hashCode() {
		return this.toString().hashCode();
	}

	boolean isPerfect() {
		UnionFind uf = new UnionFind(height*width);

		// union find cycle detection
		for(int i = 0; i < height; ++i) {
			// horizontal edges
			for(int j = 0; j < width-1; ++j) {
				if(grid[i][j].hasPassageTo(grid[i][j+1])) {
					if(uf.sameClass(coordToInt(i,j), coordToInt(i,j+1)))
						return false;
					uf.union(coordToInt(i,j), coordToInt(i,j+1));
				}
			}

			// there are no vertical edges in last row, so we're done
			if(i == height-1)
				continue;

			// vertical edges
			for(int j = 0; j < width; ++j) {
				if(grid[i][j].hasPassageTo(grid[i+1][j])) {
					if(uf.sameClass(coordToInt(i,j), coordToInt(i+1,j)))
						return false;
					uf.union(coordToInt(i,j), coordToInt(i+1,j));
				}
			}
		}

		// check if connected
		return (uf.getSize(0) == height*width);
	}

	void clearMarks() {
		for (Cell[] row : grid)
			for (Cell c : row)
				c.setMarked(false);
	}
}

