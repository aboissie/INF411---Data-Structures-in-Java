import java.io.FileNotFoundException;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Comparator;

import graphics.BasicOceanCanvas;
import ocean.BasicDirections;
import ocean.Coordinate;
import ocean.Direction;
import ocean.Ocean;

/***************************************
 * Question 4 : question complementaire
 ***************************************/

public class Bonus extends BackTrackingTraversal {

	@Override
	public boolean traverse(Ocean ocean, Coordinate start) {
		// Define a comparator for the priority queue based on the number of sharks encountered
		Comparator<Coordinate> comparator = (c1, c2) -> {
			return Integer.compare(c1.getSharkCount(), c2.getSharkCount());
		};

		PriorityQueue<Coordinate> Q = new PriorityQueue<>(comparator);
		start.setSharkCount(0); // Initialize shark count for start coordinate
		Q.add(start);
		ocean.setMark(start);

		while (!Q.isEmpty()) {
			Coordinate v = Q.poll();

			if (ocean.isNemoAt(v)) {
				backTrack(ocean, v);
				return true;
			}

			for (Direction d : ocean.directions()) {
				Coordinate n = v.moveTo(d);
				if (ocean.isValid(n) && !ocean.isMarked(n) && !ocean.isWall(n)) {
					ocean.setMark(n, d.getOpposite());
					n.setSharkCount(v.getSharkCount() + (ocean.isThereASharkAt(n) ? 1 : 0));
					Q.add(n);
				}
			}
		}

		return false;
	}

	/**
	 * Lance la visualisation du parcours de l'océan dont la carte se trouve dans
	 * {@code one-shark.txt}
	 * 
	 * @param args inutilisé
	 * @throws FileNotFoundException lorsque la carte n'est pas accessible
	 */
	public static void main(String[] args) throws FileNotFoundException {
		String fileName = (args.length < 1) ? "many-sharks.txt" : args[0];

		// on crée un nouvel océan à partir de la carte avec requins one-shark.txt
		// et on y associe un nouveau rapporteur qui sera utilisé pour suivre toutes les
		// explorations à la suite
		Ocean sharks = new Ocean(fileName, "data/" + fileName, BasicDirections.values());

		System.out.println("Test plus court chemin evitant le plus de requins (avoid sharks)");
		sharks.reporters().add(new BasicOceanCanvas("Avoid Sharks - Shortest Path")).exploreUsing(new Bonus());
	}
}

