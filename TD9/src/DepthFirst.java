import java.io.FileNotFoundException;

import graphics.BasicOceanCanvas;
import ocean.BasicDirections;
import ocean.Coordinate;
import ocean.Direction;
import ocean.Ocean;
import ocean.Traversal;

/**************************************
 * Question 1 : parcours en profondeur
 **************************************/

/**
 * Parcours en profondeur
 */
class DepthFirst extends Traversal {
	/**
	 * Implémente le parcours en profondeur
	 */
	@Override
	public boolean traverse(Ocean ocean, Coordinate start) {
		if (ocean.isNemoAt(start))
			return true;

		ocean.setMark(start);

		for (Direction direction : ocean.directions()) {
			Coordinate neighbour = start.moveTo(direction);
			if(!ocean.isValid(neighbour) || ocean.isWall(neighbour) || ocean.isMarked(neighbour)) continue;
			if(traverse(ocean, neighbour)){
				ocean.setMark(start, path);
				return true;
			}
		}

		ocean.setMark(start, deadEnd);
		return false;
	}

	/**
	 * Lance la visualisation du parcours de l'océan dont la carte se trouve dans
	 * {@code no-sharks.txt}
	 * 
	 * @param args inutilisé
	 * @throws FileNotFoundException lorsque la carte n'est pas accessible
	 */
	public static void main(String[] args) throws FileNotFoundException {
		String fileName = (args.length < 1) ? "no-sharks.txt" : args[0];

		// on crée un nouvel océan à partir de la carte sans requins
		// et on y associe un nouveau rapporteur qui sera utilisé pour suivre toutes les
		// explorations à la suite
		Ocean noSharks = new Ocean(fileName, "data/" + fileName, BasicDirections.values());

		// on lance le parcours en profondeur
		System.out.println("Test parcours en profondeur (depth first search)");
		noSharks.reporters().add(new BasicOceanCanvas("No Sharks - DFS")).exploreUsing(new DepthFirst());
	}
}
