import java.io.FileNotFoundException;
import java.util.Deque;
import java.util.LinkedList;

import graphics.BasicOceanCanvas;
import ocean.BasicDirections;
import ocean.Coordinate;
import ocean.Direction;
import ocean.Ocean;

/**********************************
 * Question 3 : éviter les requins
 **********************************/

/**
 * Parcours utilisant une Dequeue pour éviter les requins
 */
class AvoidSharks extends BackTrackingTraversal {
	/**
	 * Implémente le parcours évitant les requins
	 */
	@Override
	public boolean traverse(Ocean ocean, Coordinate start) {
		LinkedList<Coordinate> Q = new LinkedList<>();
		Q.add(start);
		ocean.setMark(start);

		while(!Q.isEmpty()){
			Coordinate v = Q.poll();
			
			if(ocean.isNemoAt(v)){
				backTrack(ocean, v);
				return true;
			}

			for(Direction d:ocean.directions()){
				Coordinate n = v.moveTo(d);
				if(ocean.isValid(n) && !ocean.isMarked(n) && !ocean.isWall(n)){
					ocean.setMark(n, d.getOpposite());
					if(!ocean.isThereASharkAt(n)) Q.addFirst(n);
					else Q.addLast(n);
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
		String fileName = (args.length < 1) ? "one-shark.txt" : args[0];

		// on crée un nouvel océan à partir de la carte avec requins one-shark.txt
		// et on y associe un nouveau rapporteur qui sera utilisé pour suivre toutes les
		// explorations à la suite
		Ocean sharks = new Ocean(fileName, "data/" + fileName, BasicDirections.values());

		System.out.println("Test evitement des requins (avoid sharks)");
		sharks.reporters().add(new BasicOceanCanvas("Finding Nemo - With Sharks")).exploreUsing(new AvoidSharks());
	}
}
